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

class Fhir4CodingListTest {
    @Test
    fun `It fulfils CodingList`() {
        val codingList: Any = Fhir4CodingListWrapper(
            emptyList()
        )

        assertTrue(codingList is CompatibilityWrapperContract.CodingList<*>)
    }

    @Test
    fun `It has the the size of the given List of Codings`() {
        // Given
        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems)

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
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems)[index]

        // Then
        assertSame(
            actual = actual.unwrap(),
            expected = givenItems[index]
        )
    }

    @Test
    fun `Given contains is called with a Coding it returns false if the wrapped list does not contain the Item`() {
        // Given
        val needle = Fhir4Coding(id = "23")

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).contains(
            Fhir4CodingWrapper(needle)
        )

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given contains is called with a Coding it returns true if the wrapped list contains the Item`() {
        // Given
        val needle = Fhir4Coding(id = "42")

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
            needle
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).contains(
            Fhir4CodingWrapper(needle)
        )

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given containsAll is called with a custom Collections of Codings it fails`() {
        // Given
        val needle = Fhir4CodingListWrapper(
            listOf(Fhir4Coding(id = "23"))
        )

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).containsAll(needle)

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given containsAll is called with a Collections of Codings it returns false if the wrapped list does match`() {
        // Given
        val needle = Fhir4ResponseItemCollection()

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            Fhir4CodingListWrapper(givenItems).containsAll(needle)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unexpected Collection Type - Please use an Fhir4CodingListWrapper."
        )
    }

    @Test
    fun `Given containsAll is called with a Coding it returns true if the wrapped list matches`() {
        // Given
        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        val needle = Fhir4CodingListWrapper(givenItems)

        // When
        val actual = Fhir4CodingListWrapper(givenItems).containsAll(needle)

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given indexOf is called with a Coding it returns the index of the element`() {
        // Given
        val needle = Fhir4Coding(id = "42")

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
            needle
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).indexOf(
            Fhir4CodingWrapper(needle)
        )

        // Then
        assertEquals(
            actual = actual,
            expected = givenItems.indexOf(needle)
        )
    }

    @Test
    fun `Given lastIndexOf is called with a Coding it returns the index of the element`() {
        // Given
        val needle = Fhir4Coding(id = "42")

        val givenItems = listOf(
            needle,
            Fhir4Coding(id = "1"),
            needle,
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
            needle
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).lastIndexOf(
            Fhir4CodingWrapper(needle)
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
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3")
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).isEmpty()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given isEmpty is called it returns true if the wrapped list is empty`() {
        // When
        val actual = Fhir4CodingListWrapper(emptyList()).isEmpty()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given subList is called with a startIndex and endIndex it returns a Fhir4CodingList for the Sublist`() {
        // Given
        val startIndex = 0
        val endIndex = 3

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
            Fhir4Coding(id = "4"),
            Fhir4Coding(id = "5"),
        )

        // When
        val actual: Any = Fhir4CodingListWrapper(givenItems).subList(
            fromIndex = startIndex,
            toIndex = endIndex
        )

        // Then
        assertTrue(actual is Fhir4CodingListWrapper)

        assertEquals(
            actual = actual.unwrap(),
            expected = givenItems.subList(
                fromIndex = startIndex,
                toIndex = endIndex
            )
        )
    }

    @Test
    fun `Given iterator is called it returns a CodingListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
        )

        // When
        val actual: Any = Fhir4CodingListWrapper(givenItems).iterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.CodingListIterator<*>)
    }

    @Test
    fun `Given listIterator is called it returns a CodingListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
        )

        // When
        val actual: Any = Fhir4CodingListWrapper(givenItems).listIterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.CodingListIterator<*>)
    }

    @Test
    fun `Given listIterator is called with a startIndex it returns a CodingListIterator`() {
        // Given
        val index = 1

        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
        )

        // When
        val actual: Any = Fhir4CodingListWrapper(givenItems).listIterator(index)

        // Then
        assertTrue(actual is CompatibilityWrapperContract.CodingListIterator<*>)
        assertEquals(
            actual = actual.nextIndex(),
            expected = givenItems.listIterator(index).nextIndex()
        )
    }

    @Test
    fun `Given unwrap is called it returns the given List`() {
        // Given
        val givenItems = listOf(
            Fhir4Coding(id = "1"),
            Fhir4Coding(id = "2"),
            Fhir4Coding(id = "3"),
        )

        // When
        val actual = Fhir4CodingListWrapper(givenItems).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenItems
        )
    }

    private class Fhir4ResponseItemCollection : Collection<CompatibilityWrapperContract.Coding<Fhir4Coding>> {
        override val size: Int
            get() = TODO("Not yet implemented")

        override fun contains(element: CompatibilityWrapperContract.Coding<Fhir4Coding>): Boolean {
            TODO("Not yet implemented")
        }

        override fun containsAll(elements: Collection<CompatibilityWrapperContract.Coding<Fhir4Coding>>): Boolean {
            TODO("Not yet implemented")
        }

        override fun isEmpty(): Boolean {
            TODO("Not yet implemented")
        }

        override fun iterator(): Iterator<CompatibilityWrapperContract.Coding<Fhir4Coding>> {
            TODO("Not yet implemented")
        }
    }
}
