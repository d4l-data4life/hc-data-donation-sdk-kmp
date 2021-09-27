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

internal class Fhir3QuestionnaireResponseItemAnswerWrapper(
    private var itemAnswer: Fhir3QuestionnaireResponseItemAnswer
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val valueString: String?
        get() = itemAnswer.valueString

    override val valueDateTime: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?
        get() {
            return if (itemAnswer.valueDateTime is Fhir3DateTime) {
                Fhir3DateTimeWrapper(itemAnswer.valueDateTime!!)
            } else {
                null
            }
        }

    override val item: CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
        get() {
            return if (itemAnswer.item is List<*>) {
                Fhir3QuestionnaireResponseItemListWrapper(itemAnswer.item!!)
            } else {
                null
            }
        }

    override fun copy(
        valueString: String?,
        valueDateTime: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?,
        item: CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>?
    ): CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemAnswerWrapper(
            itemAnswer.copy(
                valueString = valueString,
                valueDateTime = valueDateTime?.unwrap(),
                item = item?.unwrap()
            )
        )
    }

    override fun unwrap(): Fhir3QuestionnaireResponseItemAnswer {
        return itemAnswer
    }
}

internal class Fhir4QuestionnaireResponseItemAnswerWrapper(
    private var itemAnswer: Fhir4QuestionnaireResponseItemAnswer
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val valueString: String?
        get() = itemAnswer.valueString

    override val valueDateTime: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?
        get() {
            return if (itemAnswer.valueDateTime is Fhir4DateTime) {
                Fhir4DateTimeWrapper(itemAnswer.valueDateTime!!)
            } else {
                null
            }
        }

    override val item: CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
        get() {
            return if (itemAnswer.item is List<*>) {
                Fhir4QuestionnaireResponseItemListWrapper(itemAnswer.item!!)
            } else {
                null
            }
        }

    override fun copy(
        valueString: String?,
        valueDateTime: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?,
        item: CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>?
    ): CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemAnswerWrapper(
            itemAnswer.copy(
                valueString = valueString,
                valueDateTime = valueDateTime?.unwrap(),
                item = item?.unwrap()
            )
        )
    }

    override fun unwrap(): Fhir4QuestionnaireResponseItemAnswer {
        return itemAnswer
    }
}
