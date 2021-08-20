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
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.hl7.fhir.stu3.primitive.DateTime

internal class QuestionnaireResponseAnonymizer(
    private val dateTimeSmearer: AnonymizationContract.DateTimeSmearer
): AnonymizationContract.QuestionnaireResponseAnonymizer {
    private fun <T> mapOrNull(
        list: List<T>?,
        action: (T) -> T
    ): List<T>? {
        return if (list.isNullOrEmpty()) {
            null
        } else {
            list.map { item -> action(item) }
        }
    }

    private fun mapQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            item = mapOrNull(questionnaireResponse.item, ::mapQuestionnaireResponseItem)
        )
    }

    private fun mapQuestionnaireResponseItem(
        responseItem: QuestionnaireResponseItem
    ): QuestionnaireResponseItem {
        val item = mapOrNull(responseItem.item, ::mapQuestionnaireResponseItem)
        val answer = mapOrNull(responseItem.answer, ::mapQuestionnaireResponseItemAnswer)

        return responseItem.copy(
            item = item,
            answer = answer
        )
    }

    private fun mapQuestionnaireResponseItemAnswer(
        itemAnswer: QuestionnaireResponseItemAnswer
    ): QuestionnaireResponseItemAnswer {
        val item = mapOrNull(itemAnswer.item, ::mapQuestionnaireResponseItem)
        val valueString = itemAnswer.valueString

        return itemAnswer.copy(
            item = item,
            valueString = valueString
        )
    }

    private fun blurAutored(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: BlurRule
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            authored = questionnaireResponse.authored!!.copy(
                value = dateTimeSmearer.blur(
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

    override fun anonymize(
        resource: QuestionnaireResponse,
        rule: BlurRule
    ): QuestionnaireResponse {
        val bluredResource = if (authoredIsBlurable(resource, rule)) {
            blurAutored(resource, rule)
        } else {
            resource
        }

        return mapQuestionnaireResponse(bluredResource)
    }
}
