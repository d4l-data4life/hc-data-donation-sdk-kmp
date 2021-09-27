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

internal class Fhir3QuestionnaireResponseItemListWrapper(
    private val item: List<Fhir3QuestionnaireResponseItem>
) : CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun contains(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        TODO("Not yet implemented")
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        TODO("Not yet implemented")
    }

    override fun unwrap(): List<Fhir3QuestionnaireResponseItem> {
        return item
    }
}

internal class Fhir4QuestionnaireResponseItemListWrapper(
    private val item: List<Fhir4QuestionnaireResponseItem>
) : CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val size: Int
        get() = TODO("Not yet implemented")

    override fun contains(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        TODO("Not yet implemented")
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        TODO("Not yet implemented")
    }

    override fun unwrap(): List<Fhir4QuestionnaireResponseItem> {
        return item
    }
}
