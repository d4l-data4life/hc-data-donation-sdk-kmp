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

package care.data4life.datadonation.donation.anonymization

import care.data4life.datadonation.donation.anonymization.AnonymizationContract.BlurRuleResolver.Companion.REFERENCE_SEPARATOR
import care.data4life.datadonation.donation.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationBlur
import care.data4life.datadonation.donation.program.model.ProgramResource
import care.data4life.datadonation.donation.program.model.ProgramResourceBlur
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
        programResources: List<ProgramResource>
    ): ProgramResourceBlur? {
        return programResources.find { resource ->
            assembleReferences(
                resource.url,
                resource.versions
            ).contains(reference)
        }?.blur
    }

    private fun mergeRuleSets(
        resourceRule: ProgramResourceBlur?,
        programRule: ProgramAnonymizationBlur?
    ): BlurRule? {
        val location = resourceRule?.location ?: programRule?.location

        return if (location is String) {
            BlurRule(
                location = location,
                authored = resourceRule?.authored ?: programRule?.authored,
                researchSubject = programRule?.researchSubject,
                resourceBlurItems = resourceRule?.items
            )
        } else {
            null
        }
    }

    override fun resolveBlurRule(
        fhirResource: FhirQuestionnaireResponse,
        programRule: ProgramAnonymizationBlur?,
        programResources: List<ProgramResource>
    ): BlurRule? {
        val resourceRule = findByFhirReference(
            fhirResource.questionnaire?.reference,
            programResources
        )

        return if (resourceRule == null && programRule == null) {
            null
        } else {
            mergeRuleSets(resourceRule, programRule)
        }
    }
}
