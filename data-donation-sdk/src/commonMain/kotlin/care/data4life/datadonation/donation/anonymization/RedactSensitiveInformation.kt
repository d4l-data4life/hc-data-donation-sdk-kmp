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

import care.data4life.datadonation.donation.anonymization.AnonymizationContract.Redaction.Companion.REDACTABLE_DOMAINS
import care.data4life.datadonation.donation.anonymization.AnonymizationContract.Redaction.Companion.REDACTABLE_FIELDS
import care.data4life.datadonation.donation.anonymization.AnonymizationContract.Redaction.Companion.REDACTED
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer

internal object RedactSensitiveInformation : AnonymizationContract.Redaction {
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

    private fun extractQuestionnaireReference(questionnaireResponse: QuestionnaireResponse): String? {
        return questionnaireResponse.questionnaire?.reference?.substringBefore('|')
    }

    private fun isRedactableResource(resource: FhirResource): Boolean {
        return when {
            resource !is QuestionnaireResponse -> false
            !REDACTABLE_DOMAINS.contains(extractQuestionnaireReference(resource)) -> false
            else -> true
        }
    }

    private fun mapFhir(resource: FhirResource): FhirResource {
        return if (isRedactableResource(resource)) {
            mapQuestionnaireResponse(resource as QuestionnaireResponse)
        } else {
            resource
        }
    }

    private fun mapQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            item = mapOrNull(questionnaireResponse.item, ::mapQuestionnaireResponseItem)
        )
    }

    private fun filterResponseItemsAnswersByField(
        responseItem: QuestionnaireResponseItem
    ): List<QuestionnaireResponseItemAnswer>? {
        return if (REDACTABLE_FIELDS.contains(responseItem.linkId)) {
            mapOrNull(responseItem.answer, ::mapQuestionnaireResponseItemAnswer)
        } else {
            responseItem.answer
        }
    }

    private fun mapQuestionnaireResponseItem(
        responseItem: QuestionnaireResponseItem
    ): QuestionnaireResponseItem {
        val item = mapOrNull(responseItem.item, ::mapQuestionnaireResponseItem)
        val answer = filterResponseItemsAnswersByField(responseItem)

        return responseItem.copy(
            item = item,
            answer = answer
        )
    }

    private fun mapQuestionnaireResponseItemAnswer(
        itemAnswer: QuestionnaireResponseItemAnswer
    ): QuestionnaireResponseItemAnswer {
        val item = mapOrNull(itemAnswer.item, ::mapQuestionnaireResponseItem)
        val valueString = maskQuestionnaireResponseItemAnswerValueString(itemAnswer.valueString)

        return itemAnswer.copy(
            item = item,
            valueString = valueString
        )
    }

    private fun maskQuestionnaireResponseItemAnswerValueString(valueString: String?): String? {
        return if (valueString is String) {
            REDACTED
        } else {
            null
        }
    }

    override suspend fun redact(resources: List<FhirResource>): List<FhirResource> {
        return resources.map { resource -> mapFhir(resource) }
    }
}
