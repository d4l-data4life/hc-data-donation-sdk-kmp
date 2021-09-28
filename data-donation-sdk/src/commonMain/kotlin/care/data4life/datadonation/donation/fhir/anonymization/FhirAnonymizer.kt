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
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.FhirWrapper
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponse
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.ResearchSubject
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion

internal class FhirAnonymizer(
    private val researchSubjectResponseBlurResolver: AnonymizationContract.ResearchSubjectBlurRuleResolver,
    private val questionnaireResponseBlurResolver: AnonymizationContract.QuestionnaireResponseBlurRuleResolver,
    private val questionnaireResponseAnonymizer: AnonymizationContract.QuestionnaireResponseAnonymizer,
    private val researchSubjectAnonymizer: AnonymizationContract.ResearchSubjectAnonymizer
) : AnonymizationContract.FhirAnonymizer {
    private fun anonymizeQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
        programType: ProgramType,
        globalProgramRule: ProgramBlur?,
        localResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion> {
        val rule = questionnaireResponseBlurResolver.resolveBlurRule(
            questionnaireResponse = questionnaireResponse,
            programRule = globalProgramRule,
            fhirResourceConfigurations = localResourceRule
        )

        return questionnaireResponseAnonymizer.anonymize(
            questionnaireResponse,
            programType,
            rule
        )
    }

    private fun anonymizeResearchSubject(
        researchSubject: ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
        programRule: ProgramBlur?
    ): ResearchSubject<FhirVersion, FhirVersion, FhirVersion> {
        val rule = researchSubjectResponseBlurResolver.resolveBlurRule(
            programRule = programRule,
        )

        return researchSubjectAnonymizer.anonymize(
            researchSubject,
            rule
        )
    }

    override fun anonymize(
        fhirResource: FhirWrapper<FhirVersion>,
        programType: ProgramType,
        globalProgramRule: ProgramBlur?,
        localResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): FhirWrapper<FhirVersion> {
        return when (fhirResource) {
            is QuestionnaireResponse<*, *, *, *> -> anonymizeQuestionnaireResponse(
                fhirResource as QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
                programType,
                globalProgramRule,
                localResourceRule
            )
            is ResearchSubject<*, *, *> -> anonymizeResearchSubject(
                fhirResource as ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
                globalProgramRule
            )
            else -> fhirResource
        }
    }
}
