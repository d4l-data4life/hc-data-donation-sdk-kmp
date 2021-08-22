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

import care.data4life.datadonation.donation.fhir.anonymization.AnonymizationContract.BlurRuleResolver.Companion.REFERENCE_SEPARATOR
import care.data4life.datadonation.donation.fhir.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationGlobalBlur
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceBlur
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceConfiguration
import care.data4life.hl7.fhir.stu3.model.FhirQuestionnaireResponse

internal object BlurRuleResolver :
    AnonymizationContract.BlurRuleResolver {
    private fun assembleReferences(
        baseUrl: String,
        versions: List<String>?
    ): List<String> {
        return if (versions is List<*>) {
            versions.map { version -> "$baseUrl$REFERENCE_SEPARATOR$version" }
        } else {
            listOf(baseUrl)
        }
    }

    private fun findByFhirReference(
        reference: String?,
        programFhirResourceConfigurations: List<ProgramFhirResourceConfiguration>
    ): ProgramFhirResourceBlur? {
        return programFhirResourceConfigurations.find { resource ->
            assembleReferences(
                resource.url,
                resource.versions
            ).contains(reference)
        }?.fhirBlur
    }

    private fun mergeRuleSets(
        fhirResourceRule: ProgramFhirResourceBlur?,
        programRuleGlobal: ProgramAnonymizationGlobalBlur?
    ): BlurRule? {
        val location = fhirResourceRule?.targetTimeZone ?: programRuleGlobal?.targetTimeZone

        return if (location is String) {
            BlurRule(
                targetTimeZone = location,
                questionnaireResponseAuthored = fhirResourceRule?.questionnaireResponseAuthored ?: programRuleGlobal?.questionnaireResponseAuthored,
                researchSubject = programRuleGlobal?.researchSubject,
                questionnaireResponseItemBlurMapping = fhirResourceRule?.itemBlurs ?: emptyList()
            )
        } else {
            null
        }
    }

    override fun resolveBlurRule(
        fhirResource: FhirQuestionnaireResponse?,
        programRuleGlobal: ProgramAnonymizationGlobalBlur?,
        programFhirResourceConfigurations: List<ProgramFhirResourceConfiguration>
    ): BlurRule? {
        val resourceRule = findByFhirReference(
            fhirResource?.questionnaire?.reference,
            programFhirResourceConfigurations
        )

        return if (resourceRule == null && programRuleGlobal == null) {
            null
        } else {
            mergeRuleSets(resourceRule, programRuleGlobal)
        }
    }
}
