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
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
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
        researchSubject: ResearchSubject,
        programRule: ProgramBlur?
    ): ResearchSubject {
        val rule = researchSubjectResponseBlurResolver.resolveBlurRule(
            programRule = programRule,
        )

        return researchSubjectAnonymizer.anonymize(
            researchSubject,
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
            )
            else -> fhirResource
        }
    }
}
