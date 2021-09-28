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

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.Coding

internal class Fhir3CodingListWrapper(
    private val itemList: List<Fhir3Coding>
) : CompatibilityWrapperContract.CodingList<Fhir3Coding> {
    override val size: Int
        get() = itemList.size

    override fun contains(
        element: Coding<Fhir3Coding>
    ): Boolean {
        return itemList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<Coding<Fhir3Coding>>): Boolean {
        return if (elements is Fhir3CodingListWrapper) {
            itemList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir3CodingListWrapper.")
        }
    }

    override fun get(index: Int): Coding<Fhir3Coding> {
        return Fhir3CodingWrapper(itemList[index])
    }

    override fun indexOf(element: Coding<Fhir3Coding>): Int {
        return itemList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    override fun iterator(): Iterator<Coding<Fhir3Coding>> {
        return listIterator()
    }

    override fun lastIndexOf(element: Coding<Fhir3Coding>): Int {
        return itemList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<Coding<Fhir3Coding>> {
        return Fhir3CodingListIteratorWrapper(
            itemList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<Coding<Fhir3Coding>> {
        return Fhir3CodingListIteratorWrapper(
            itemList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<Coding<Fhir3Coding>> {
        return Fhir3CodingListWrapper(
            itemList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir3Coding> {
        return itemList
    }
}

internal class Fhir4CodingListWrapper(
    private val itemList: List<Fhir4Coding>
) : CompatibilityWrapperContract.CodingList<Fhir4Coding> {
    override val size: Int
        get() = itemList.size

    override fun contains(
        element: Coding<Fhir4Coding>
    ): Boolean {
        return itemList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<Coding<Fhir4Coding>>): Boolean {
        return if (elements is Fhir4CodingListWrapper) {
            itemList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir4CodingListWrapper.")
        }
    }

    override fun get(index: Int): Coding<Fhir4Coding> {
        return Fhir4CodingWrapper(itemList[index])
    }

    override fun indexOf(element: Coding<Fhir4Coding>): Int {
        return itemList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemList.isEmpty()
    }

    override fun iterator(): Iterator<Coding<Fhir4Coding>> {
        return listIterator()
    }

    override fun lastIndexOf(element: Coding<Fhir4Coding>): Int {
        return itemList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<Coding<Fhir4Coding>> {
        return Fhir4CodingListIteratorWrapper(
            itemList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<Coding<Fhir4Coding>> {
        return Fhir4CodingListIteratorWrapper(
            itemList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<Coding<Fhir4Coding>> {
        return Fhir4CodingListWrapper(
            itemList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir4Coding> {
        return itemList
    }
}
