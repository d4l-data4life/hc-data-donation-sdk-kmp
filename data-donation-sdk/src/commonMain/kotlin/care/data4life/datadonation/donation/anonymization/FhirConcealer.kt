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

import care.data4life.datadonation.donation.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramDonationConfiguration
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.primitive.DateTime

internal class FhirConcealer(
    private val blurResolver: AnonymizationContract.BlurRuleResolver,
    private val dateTimeConcealer: AnonymizationContract.DateTimeConcealer
) : AnonymizationContract.FhirConcealer {
    private fun blurAutored(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: BlurRule
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            authored = questionnaireResponse.authored!!.copy(
                value = dateTimeConcealer.blur(
                    questionnaireResponse.authored!!.value,
                    blurRule.location,
                    blurRule.authored!!
                )
            )
        )
    }

    private fun authoredIsBlurable(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: BlurRule
    ): Boolean {
        return questionnaireResponse.authored is DateTime && blurRule.authored is BlurFunction
    }

    private fun blurLocalizedQuestionnaireResponseFields(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: BlurRule
    ): QuestionnaireResponse {
        return if (authoredIsBlurable(questionnaireResponse, blurRule)) {
            blurAutored(questionnaireResponse, blurRule)
        } else {
            questionnaireResponse
        }
    }

    private fun blurQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse,
        programConfiguration: ProgramDonationConfiguration
    ): QuestionnaireResponse {
        val blurRule = blurResolver.resolveBlurRule(
            fhirResource = questionnaireResponse,
            programRule = programConfiguration.anonymization?.blur,
            programResources = programConfiguration.resources
        )

        return if (blurRule is BlurRule) {
            blurLocalizedQuestionnaireResponseFields(
                questionnaireResponse,
                blurRule
            )
        } else {
            questionnaireResponse
        }
    }

    override fun blurFhirResource(
        fhirResource: FhirResource,
        programConfiguration: ProgramDonationConfiguration
    ): FhirResource {
        return if (fhirResource is QuestionnaireResponse) {
            blurQuestionnaireResponse(fhirResource, programConfiguration)
        } else {
            fhirResource
        }
    }
}
