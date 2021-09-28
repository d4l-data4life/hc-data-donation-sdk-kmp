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
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract
import care.data4life.datadonation.donation.fhir.wrapper.Fhir3QuestionnaireResponseWrapper
import care.data4life.datadonation.donation.fhir.wrapper.Fhir3ResearchSubjectWrapper
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.ResearchSubject

internal class FhirAnonymizer(
    private val researchSubjectResponseBlurResolver: AnonymizationContract.ResearchSubjectBlurRuleResolver,
    private val questionnaireResponseBlurResolver: AnonymizationContract.QuestionnaireResponseBlurRuleResolver,
    private val questionnaireResponseAnonymizer: AnonymizationContract.QuestionnaireResponseAnonymizer,
    private val researchSubjectAnonymizer: AnonymizationContract.ResearchSubjectAnonymizer
) : AnonymizationContract.FhirAnonymizer {
    private fun anonymizeQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse,
        programType: ProgramType,
        globalProgramRule: ProgramBlur?,
        localResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): QuestionnaireResponse {
        val response = Fhir3QuestionnaireResponseWrapper(questionnaireResponse) as CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>

        val rule = questionnaireResponseBlurResolver.resolveBlurRule(
            questionnaireResponse = response,
            programRule = globalProgramRule,
            fhirResourceConfigurations = localResourceRule
        )

        return questionnaireResponseAnonymizer.anonymize(
            response,
            programType,
            rule
        ).unwrap() as QuestionnaireResponse
    }

    private fun anonymizeResearchSubject(
        researchSubject: ResearchSubject,
        programRule: ProgramBlur?
    ): CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion> {
        val rule = researchSubjectResponseBlurResolver.resolveBlurRule(
            programRule = programRule,
        )

        return researchSubjectAnonymizer.anonymize(
            Fhir3ResearchSubjectWrapper(researchSubject) as CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            rule
        )
    }

    override fun anonymize(
        fhirResource: FhirResource,
        programType: ProgramType,
        globalProgramRule: ProgramBlur?,
        localResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): FhirResource {
        return when (fhirResource) {
            is QuestionnaireResponse -> anonymizeQuestionnaireResponse(
                fhirResource,
                programType,
                globalProgramRule,
                localResourceRule
            )
            is ResearchSubject -> anonymizeResearchSubject(
                fhirResource,
                globalProgramRule
            ).unwrap() as FhirResource
            else -> fhirResource
        }
    }
}
