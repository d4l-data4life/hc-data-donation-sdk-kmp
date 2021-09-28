/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.datadonation.donation.fhir.anonymization

import care.data4life.datadonation.donation.fhir.AllowedReference
import care.data4life.datadonation.donation.fhir.anonymization.model.QuestionnaireResponseBlurRule
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponse
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion

internal object QuestionnaireResponseBlurRuleResolver : AnonymizationContract.QuestionnaireResponseBlurRuleResolver {
    private fun findLocalBlurByFhirReference(
        reference: String?,
        fhirResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): QuestionnaireResponseBlur? {
        return if (reference is String) {
            fhirResourceRule[reference]
        } else {
            null
        }
    }

    private fun mergeRuleSets(
        programRule: ProgramBlur?,
        fhirResourceRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponseBlurRule? {
        val location = fhirResourceRule?.targetTimeZone ?: programRule?.targetTimeZone

        return if (location is String) {
            QuestionnaireResponseBlurRule(
                targetTimeZone = location,
                questionnaireResponseAuthored = fhirResourceRule?.authoredBlurFunctionReference
                    ?: programRule?.questionnaireResponseAuthoredBlurFunctionReference,
                questionnaireResponseItems = fhirResourceRule?.questionnaireResponseItemBlurs ?: emptyList()
            )
        } else {
            null
        }
    }

    override fun resolveBlurRule(
        questionnaireResponse: QuestionnaireResponse<out FhirVersion, out FhirVersion, out FhirVersion, out FhirVersion>,
        programRule: ProgramBlur?,
        fhirResourceConfigurations: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): QuestionnaireResponseBlurRule? {
        val resourceRule = findLocalBlurByFhirReference(
            questionnaireResponse.questionnaireReference,
            fhirResourceConfigurations
        )

        return if (resourceRule == null && programRule == null) {
            null
        } else {
            mergeRuleSets(programRule, resourceRule)
        }
    }
}
