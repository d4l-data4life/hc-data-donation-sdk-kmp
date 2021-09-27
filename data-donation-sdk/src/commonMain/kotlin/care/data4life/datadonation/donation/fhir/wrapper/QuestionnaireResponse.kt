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

package care.data4life.datadonation.donation.fhir.wrapper

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.DateTime
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponse
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemList

internal class Fhir3QuestionnaireResponseWrapper(
    private var questionnaireResponse: Fhir3QuestionnaireResponse
) : QuestionnaireResponse<Fhir3QuestionnaireResponse, Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val questionnaireReference: String?
        get() = questionnaireResponse.questionnaire?.reference

    override val item: QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
        get() {
            return if (questionnaireResponse.item is List<*>) {
                Fhir3QuestionnaireResponseItemListWrapper(questionnaireResponse.item!!)
            } else {
                null
            }
        }

    override val authored: DateTime<Fhir3DateTime>?
        get() {
            return if (questionnaireResponse.authored is Fhir3DateTime) {
                Fhir3DateTimeWrapper(questionnaireResponse.authored!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir3QuestionnaireResponse {
        return questionnaireResponse
    }

    override fun copy(
        item: QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?,
        authored: DateTime<Fhir3DateTime>?
    ): QuestionnaireResponse<Fhir3QuestionnaireResponse, Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseWrapper(
            questionnaireResponse.copy(
                item = item?.unwrap(),
                authored = authored?.unwrap()
            )
        )
    }
}

internal class Fhir4QuestionnaireResponseWrapper(
    private var questionnaireResponse: Fhir4QuestionnaireResponse
) : QuestionnaireResponse<Fhir4QuestionnaireResponse, Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val questionnaireReference: String?
        get() = questionnaireResponse.questionnaire

    override val item: QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
        get() {
            return if (questionnaireResponse.item is List<*>) {
                Fhir4QuestionnaireResponseItemListWrapper(questionnaireResponse.item!!)
            } else {
                null
            }
        }

    override val authored: DateTime<Fhir4DateTime>?
        get() {
            return if (questionnaireResponse.authored is Fhir4DateTime) {
                Fhir4DateTimeWrapper(questionnaireResponse.authored!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir4QuestionnaireResponse {
        return questionnaireResponse
    }

    override fun copy(
        item: QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?,
        authored: DateTime<Fhir4DateTime>?
    ): QuestionnaireResponse<Fhir4QuestionnaireResponse, Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseWrapper(
            questionnaireResponse.copy(
                item = item?.unwrap(),
                authored = authored?.unwrap()
            )
        )
    }
}
