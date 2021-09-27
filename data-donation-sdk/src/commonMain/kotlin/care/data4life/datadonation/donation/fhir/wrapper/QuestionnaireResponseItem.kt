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

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItem
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemAnswerList
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemList

internal class Fhir3QuestionnaireResponseItemWrapper(
    private var questionnaireResponseItem: Fhir3QuestionnaireResponseItem
) : QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val linkId: String
        get() = questionnaireResponseItem.linkId

    override val answer: QuestionnaireResponseItemAnswerList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
        get() {
            return if (questionnaireResponseItem.answer is List<*>) {
                Fhir3QuestionnaireResponseItemAnswerListWrapper(questionnaireResponseItem.answer!!)
            } else {
                null
            }
        }

    override val item: QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
        get() {
            return if (questionnaireResponseItem.item is List<*>) {
                Fhir3QuestionnaireResponseItemListWrapper(questionnaireResponseItem.item!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir3QuestionnaireResponseItem {
        return questionnaireResponseItem
    }

    override fun copy(
        item: QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?,
        answer: QuestionnaireResponseItemAnswerList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
    ): QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemWrapper(
            questionnaireResponseItem.copy(
                item = item?.unwrap(),
                answer = answer?.unwrap()
            )
        )
    }
}

internal class Fhir4QuestionnaireResponseItemWrapper(
    private var questionnaireResponseItem: Fhir4QuestionnaireResponseItem
) : QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val linkId: String
        get() = questionnaireResponseItem.linkId

    override val answer: QuestionnaireResponseItemAnswerList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
        get() {
            return if (questionnaireResponseItem.answer is List<*>) {
                Fhir4QuestionnaireResponseItemAnswerListWrapper(questionnaireResponseItem.answer!!)
            } else {
                null
            }
        }

    override val item: QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
        get() {
            return if (questionnaireResponseItem.item is List<*>) {
                Fhir4QuestionnaireResponseItemListWrapper(questionnaireResponseItem.item!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir4QuestionnaireResponseItem {
        return questionnaireResponseItem
    }

    override fun copy(
        item: QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?,
        answer: QuestionnaireResponseItemAnswerList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
    ): QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemWrapper(
            questionnaireResponseItem.copy(
                item = item?.unwrap(),
                answer = answer?.unwrap()
            )
        )
    }
}
