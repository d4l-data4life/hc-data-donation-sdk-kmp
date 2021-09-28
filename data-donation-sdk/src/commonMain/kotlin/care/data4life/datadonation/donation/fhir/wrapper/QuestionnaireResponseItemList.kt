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
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemList

internal class Fhir3QuestionnaireResponseItemListWrapper(
    private val itemList: List<Fhir3QuestionnaireResponseItem>
) : QuestionnaireResponseItemList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val size: Int
        get() = itemList.size

    override fun contains(
        element: QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>
    ): Boolean {
        return itemList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>>): Boolean {
        return if (elements is Fhir3QuestionnaireResponseItemListWrapper) {
            itemList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir3QuestionnaireResponseItemListWrapper.")
        }
    }

    override fun get(index: Int): QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemWrapper(itemList[index])
    }

    override fun indexOf(element: QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        return itemList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    override fun iterator(): Iterator<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return listIterator()
    }

    override fun lastIndexOf(element: QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        return itemList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemListIteratorWrapper(
            itemList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemListIteratorWrapper(
            itemList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemListWrapper(
            itemList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir3QuestionnaireResponseItem> {
        return itemList
    }

    override fun map(action: (QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>) -> QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): CompatibilityWrapperContract.FhirWrapperList<QuestionnaireResponseItem<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemListWrapper(
            mapFhirList(action)
        )
    }
}

internal class Fhir4QuestionnaireResponseItemListWrapper(
    private val itemList: List<Fhir4QuestionnaireResponseItem>
) : CompatibilityWrapperContract.QuestionnaireResponseItemList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val size: Int
        get() = itemList.size

    override fun contains(
        element: QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>
    ): Boolean {
        return itemList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>>): Boolean {
        return if (elements is Fhir4QuestionnaireResponseItemListWrapper) {
            itemList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir4QuestionnaireResponseItemListWrapper.")
        }
    }

    override fun get(index: Int): QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemWrapper(itemList[index])
    }

    override fun indexOf(element: QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        return itemList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    override fun iterator(): Iterator<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return listIterator()
    }

    override fun lastIndexOf(element: QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        return itemList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemListIteratorWrapper(
            itemList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemListIteratorWrapper(
            itemList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemListWrapper(
            itemList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir4QuestionnaireResponseItem> {
        return itemList
    }

    override fun map(
        action: (QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>) -> QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>
    ): CompatibilityWrapperContract.FhirWrapperList<QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemListWrapper(
            mapFhirList(action)
        )
    }
}
