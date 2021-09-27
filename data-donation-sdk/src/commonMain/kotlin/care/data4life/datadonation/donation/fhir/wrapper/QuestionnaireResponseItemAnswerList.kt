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

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemAnswer

internal data class Fhir3QuestionnaireResponseItemAnswerListWrapper(
    private val itemAnswerList: List<Fhir3QuestionnaireResponseItemAnswer>
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswerList<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override val size: Int
        get() = itemAnswerList.size

    override fun contains(
        element: QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>
    ): Boolean {
        return itemAnswerList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>>): Boolean {
        return if (elements is Fhir3QuestionnaireResponseItemAnswerListWrapper) {
            itemAnswerList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir3QuestionnaireResponseItemAnswerListWrapper.")
        }
    }

    override fun get(index: Int): QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemAnswerWrapper(itemAnswerList[index])
    }

    override fun indexOf(element: QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        return itemAnswerList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemAnswerList.isEmpty()
    }

    override fun iterator(): Iterator<QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return listIterator()
    }

    override fun lastIndexOf(element: QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>): Int {
        return itemAnswerList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemAnswerListIteratorWrapper(
            itemAnswerList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemAnswerListIteratorWrapper(
            itemAnswerList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime>> {
        return Fhir3QuestionnaireResponseItemAnswerListWrapper(
            itemAnswerList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir3QuestionnaireResponseItemAnswer> {
        return itemAnswerList
    }
}

internal data class Fhir4QuestionnaireResponseItemAnswerListWrapper(
    private val itemAnswerList: List<Fhir4QuestionnaireResponseItemAnswer>
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswerList<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override val size: Int
        get() = itemAnswerList.size

    override fun contains(
        element: QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>
    ): Boolean {
        return itemAnswerList.contains(element.unwrap())
    }

    override fun containsAll(elements: Collection<QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>>): Boolean {
        return if (elements is Fhir4QuestionnaireResponseItemAnswerListWrapper) {
            itemAnswerList.containsAll(elements.unwrap())
        } else {
            throw RuntimeException("Unexpected Collection Type - Please use an Fhir4QuestionnaireResponseItemAnswerListWrapper.")
        }
    }

    override fun get(index: Int): QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemAnswerWrapper(itemAnswerList[index])
    }

    override fun indexOf(element: QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        return itemAnswerList.indexOf(element.unwrap())
    }

    override fun isEmpty(): Boolean {
        return itemAnswerList.isEmpty()
    }

    override fun iterator(): Iterator<QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return listIterator()
    }

    override fun lastIndexOf(element: QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Int {
        return itemAnswerList.lastIndexOf(element.unwrap())
    }

    override fun listIterator(): ListIterator<QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemAnswerListIteratorWrapper(
            itemAnswerList.listIterator()
        )
    }

    override fun listIterator(index: Int): ListIterator<QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemAnswerListIteratorWrapper(
            itemAnswerList.listIterator(index)
        )
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        return Fhir4QuestionnaireResponseItemAnswerListWrapper(
            itemAnswerList.subList(
                fromIndex = fromIndex,
                toIndex = toIndex
            )
        )
    }

    override fun unwrap(): List<Fhir4QuestionnaireResponseItemAnswer> {
        return itemAnswerList
    }
}
