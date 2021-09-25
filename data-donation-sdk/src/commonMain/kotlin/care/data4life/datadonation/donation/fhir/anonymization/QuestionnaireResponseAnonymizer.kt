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

import care.data4life.datadonation.donation.fhir.anonymization.model.BlurModelContract.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.hl7.fhir.stu3.primitive.DateTime

internal class QuestionnaireResponseAnonymizer(
    private val dateTimeConcealer: AnonymizationContract.DateTimeConcealer,
    private val redactor: AnonymizationContract.Redactor
) : AnonymizationContract.QuestionnaireResponseAnonymizer {
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
        questionnaireResponse: QuestionnaireResponse,
        programType: ProgramType,
        blurRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            item = mapOrNull(questionnaireResponse.item) { item ->
                mapQuestionnaireResponseItem(item, programType, blurRule)
            }
        )
    }

    private fun mapQuestionnaireResponseItem(
        responseItem: QuestionnaireResponseItem,
        programType: ProgramType,
        blurRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponseItem {
        val item = mapOrNull(responseItem.item) { item ->
            mapQuestionnaireResponseItem(item, programType, blurRule)
        }
        val answer = mapOrNull(responseItem.answer) { answer ->
            mapQuestionnaireResponseItemAnswer(
                answer,
                programType,
                responseItem.linkId,
                blurRule
            )
        }

        return responseItem.copy(
            item = item,
            answer = answer
        )
    }

    private fun determineItemBlur(
        linkId: String,
        blurRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponseItemBlur? {
        return blurRule?.questionnaireResponseItems?.find { itemBlur ->
            itemBlur.linkId == linkId
        }
    }

    private fun blurValueDateTime(
        dateTime: DateTime?,
        linkId: String,
        blurRule: QuestionnaireResponseBlur?,
    ): DateTime? {
        val itemBlur = determineItemBlur(linkId, blurRule)

        return if (itemBlur is QuestionnaireResponseItemBlur && dateTime is DateTime) {
            dateTime.copy(
                value = dateTimeConcealer.blur(
                    dateTime.value,
                    blurRule!!.targetTimeZone,
                    itemBlur.function
                )
            )
        } else {
            dateTime
        }
    }

    private fun redact(valueString: String?, programType: ProgramType): String? {
        return if (programType == ProgramType.DIARY) {
            redactor.redact(valueString)
        } else {
            valueString
        }
    }

    private fun mapQuestionnaireResponseItemAnswer(
        itemAnswer: QuestionnaireResponseItemAnswer,
        programType: ProgramType,
        linkId: String,
        blurRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponseItemAnswer {
        val item = mapOrNull(itemAnswer.item) { item ->
            mapQuestionnaireResponseItem(item, programType, blurRule)
        }
        val valueString = redact(itemAnswer.valueString, programType)
        val valueDateTime = blurValueDateTime(
            itemAnswer.valueDateTime,
            linkId,
            blurRule
        )

        return itemAnswer.copy(
            item = item,
            valueString = valueString,
            valueDateTime = valueDateTime
        )
    }

    private fun isConcealableAuthoredField(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: QuestionnaireResponseBlur?
    ): Boolean {
        return questionnaireResponse.authored is DateTime &&
            blurRule?.questionnaireResponseAuthored is BlurFunction
    }

    private fun blurAuthored(
        questionnaireResponse: QuestionnaireResponse,
        blurRule: QuestionnaireResponseBlur?
    ): QuestionnaireResponse {
        return if (isConcealableAuthoredField(questionnaireResponse, blurRule)) {
            questionnaireResponse.copy(
                authored = questionnaireResponse.authored!!.copy(
                    value = dateTimeConcealer.blur(
                        questionnaireResponse.authored!!.value,
                        blurRule!!.targetTimeZone,
                        blurRule.questionnaireResponseAuthored!!
                    )
                )
            )
        } else {
            questionnaireResponse
        }
    }

    override fun anonymize(
        questionnaireResponse: QuestionnaireResponse,
        programType: ProgramType,
        rule: QuestionnaireResponseBlur?
    ): QuestionnaireResponse {
        return mapQuestionnaireResponse(
            blurAuthored(questionnaireResponse, rule),
            programType,
            rule
        )
    }
}
