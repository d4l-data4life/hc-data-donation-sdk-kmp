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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4QuestionnaireResponseItemListTest {
    @Test
    fun `It fulfils QuestionnaireResponseItemList`() {
        val questionnaireResponse: Any = Fhir4QuestionnaireResponseItemListWrapper(
            emptyList()
        )

        assertTrue(questionnaireResponse is CompatibilityWrapperContract.QuestionnaireResponseItemList<*, *, *>)
    }

    @Test
    fun `It has the the size of the given List of QuestionnaireResponseItems`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems)

        // Then
        assertEquals(
            actual = actual.size,
            expected = givenItems.size
        )
    }

    @Test
    fun `It relfects the item at a given Index wrappend`() {
        // Given
        val index = 0
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems)[index]

        // Then
        assertSame(
            actual = actual.unwrap(),
            expected = givenItems[index]
        )
    }

    @Test
    fun `Given contains is called with a QuestionnaireResponseItem it returns false if the wrapped list does not contain the Item`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItem(linkId = "23")

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).contains(
            Fhir4QuestionnaireResponseItemWrapper(needle)
        )

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given contains is called with a QuestionnaireResponseItem it returns true if the wrapped list contains the Item`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItem(linkId = "42")

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).contains(
            Fhir4QuestionnaireResponseItemWrapper(needle)
        )

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given containsAll is called with a custom Collections of QuestionnaireResponseItems it fails`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItemListWrapper(
            listOf(Fhir4QuestionnaireResponseItem(linkId = "23"))
        )

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).containsAll(needle)

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given containsAll is called with a Collections of QuestionnaireResponseItems it returns false if the wrapped list does match`() {
        // Given
        val needle = Fhir4ResponseItemCollection()

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            Fhir4QuestionnaireResponseItemListWrapper(givenItems).containsAll(needle)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unexpected Collection Type - Please use an Fhir4QuestionnaireResponseItemListWrapper."
        )
    }

    @Test
    fun `Given containsAll is called with a QuestionnaireResponseItem it returns true if the wrapped list matches`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        val needle = Fhir4QuestionnaireResponseItemListWrapper(givenItems)

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).containsAll(needle)

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given indexOf is called with a QuestionnaireResponseItem it returns the index of the element`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItem(linkId = "42")

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).indexOf(
            Fhir4QuestionnaireResponseItemWrapper(needle)
        )

        // Then
        assertEquals(
            actual = actual,
            expected = givenItems.indexOf(needle)
        )
    }

    @Test
    fun `Given lastIndexOf is called with a QuestionnaireResponseItem it returns the index of the element`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItem(linkId = "42")

        val givenItems = listOf(
            needle,
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            needle,
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).lastIndexOf(
            Fhir4QuestionnaireResponseItemWrapper(needle)
        )

        // Then
        assertEquals(
            actual = actual,
            expected = givenItems.lastIndexOf(needle)
        )
    }

    @Test
    fun `Given isEmpty is called it returns false if the wrapped list is empty`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).isEmpty()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given iEmpty is called it returns true if the wrapped list is empty`() {
        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(emptyList()).isEmpty()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given subList is called with a startIndex and endIndex it returns a Fhir4QuestionnaireResponseItemList for the Sublist`() {
        // Given
        val startIndex = 0
        val endIndex = 3

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
            Fhir4QuestionnaireResponseItem(linkId = "4"),
            Fhir4QuestionnaireResponseItem(linkId = "5"),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemListWrapper(givenItems).subList(
            fromIndex = startIndex,
            toIndex = endIndex
        )

        // Then
        assertTrue(actual is Fhir4QuestionnaireResponseItemListWrapper)

        assertEquals(
            actual = actual.unwrap(),
            expected = givenItems.subList(
                fromIndex = startIndex,
                toIndex = endIndex
            )
        )
    }

    @Test
    fun `Given iterator is called it returns a QuestionnaireResponseItemListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemListWrapper(givenItems).iterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemListIterator<*, *, *>)
    }

    @Test
    fun `Given listIterator is called it returns a QuestionnaireResponseItemListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemListWrapper(givenItems).listIterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemListIterator<*, *, *>)
    }

    @Test
    fun `Given listIterator is called with a startIndex it returns a QuestionnaireResponseItemListIterator`() {
        // Given
        val index = 1

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemListWrapper(givenItems).listIterator(index)

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemListIterator<*, *, *>)
        assertEquals(
            actual = actual.nextIndex(),
            expected = givenItems.listIterator(index).nextIndex()
        )
    }

    @Test
    fun `Given unwrap is called it returns the given List`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListWrapper(givenItems).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenItems
        )
    }

    private class Fhir4ResponseItemCollection : Collection<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        override val size: Int
            get() = TODO("Not yet implemented")

        override fun contains(element: CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Boolean {
            TODO("Not yet implemented")
        }

        override fun containsAll(elements: Collection<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>>): Boolean {
            TODO("Not yet implemented")
        }

        override fun isEmpty(): Boolean {
            TODO("Not yet implemented")
        }

        override fun iterator(): Iterator<CompatibilityWrapperContract.QuestionnaireResponseItem<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
            TODO("Not yet implemented")
        }
    }
}
