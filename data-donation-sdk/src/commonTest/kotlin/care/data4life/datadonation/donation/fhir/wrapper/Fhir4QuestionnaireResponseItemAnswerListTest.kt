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

class Fhir4QuestionnaireResponseItemAnswerListTest {
    @Test
    fun `It fulfils QuestionnaireResponseItemAnswerList`() {
        val questionnaireResponse: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(
            emptyList()
        )

        assertTrue(questionnaireResponse is CompatibilityWrapperContract.QuestionnaireResponseItemAnswerList<*, *, *>)
    }

    @Test
    fun `It has the the size of the given List of QuestionnaireResponseItems`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems)

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
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems)[index]

        // Then
        assertSame(
            actual = actual.unwrap(),
            expected = givenItems[index]
        )
    }

    @Test
    fun `Given contains is called with a QuestionnaireResponseItem it returns false if the wrapped list does not contain the Item`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItemAnswer()

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(valueString = "1"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "2"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).contains(
            Fhir4QuestionnaireResponseItemAnswerWrapper(needle)
        )

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given contains is called with a QuestionnaireResponseItem it returns true if the wrapped list contains the Item`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItemAnswer(valueString = "42")

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(valueString = "1"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "2"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "3"),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).contains(
            Fhir4QuestionnaireResponseItemAnswerWrapper(needle)
        )

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given containsAll is called with a custom Collections of QuestionnaireResponseItems it fails`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItemAnswerListWrapper(
            listOf(Fhir4QuestionnaireResponseItemAnswer())
        )

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(valueString = "1"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "2"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "3")
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).containsAll(needle)

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given containsAll is called with a Collections of QuestionnaireResponseItems it returns false if the wrapped list does match`() {
        // Given
        val needle = Fhir4ResponseItemAnswerCollection()

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(valueString = "1"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "2"),
            Fhir4QuestionnaireResponseItemAnswer(valueString = "3")
        )

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).containsAll(needle)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unexpected Collection Type - Please use an Fhir4QuestionnaireResponseItemAnswerListWrapper."
        )
    }

    @Test
    fun `Given containsAll is called with a QuestionnaireResponseItem it returns true if the wrapped list matches`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer()
        )

        val needle = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems)

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).containsAll(needle)

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given indexOf is called with a QuestionnaireResponseItem it returns the index of the element`() {
        // Given
        val needle = Fhir4QuestionnaireResponseItemAnswer()

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).indexOf(
            Fhir4QuestionnaireResponseItemAnswerWrapper(needle)
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
        val needle = Fhir4QuestionnaireResponseItemAnswer()

        val givenItems = listOf(
            needle,
            Fhir4QuestionnaireResponseItemAnswer(),
            needle,
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            needle
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).lastIndexOf(
            Fhir4QuestionnaireResponseItemAnswerWrapper(needle)
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
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).isEmpty()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given is Empty is called it returns true if the wrapped list is empty`() {
        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(emptyList()).isEmpty()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given subList is called with a startIndex and endIndex it returns a Fhir4QuestionnaireResponseItemAnswerList for the Sublist`() {
        // Given
        val startIndex = 0
        val endIndex = 3

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).subList(
            fromIndex = startIndex,
            toIndex = endIndex
        )

        // Then
        assertTrue(actual is Fhir4QuestionnaireResponseItemAnswerListWrapper)

        assertEquals(
            actual = actual.unwrap(),
            expected = givenItems.subList(
                fromIndex = startIndex,
                toIndex = endIndex
            )
        )
    }

    @Test
    fun `Given iterator is called it returns a QuestionnaireResponseItemAnswerListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).iterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemAnswerListIterator<*, *, *>)
    }

    @Test
    fun `Given listIterator is called it returns a QuestionnaireResponseItemAnswerListIterator`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).listIterator()

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemAnswerListIterator<*, *, *>)
    }

    @Test
    fun `Given listIterator is called with a startIndex it returns a QuestionnaireResponseItemAnswerListIterator`() {
        // Given
        val index = 1

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).listIterator(index)

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemAnswerListIterator<*, *, *>)
        assertEquals(
            actual = actual.nextIndex(),
            expected = givenItems.listIterator(index).nextIndex()
        )
    }

    @Test
    fun `Given unwrap is called it returns the given List`() {
        // Given
        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
            Fhir4QuestionnaireResponseItemAnswer(),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenItems
        )
    }

    @Test
    fun `Given map is called with an action it applies the action to each item and returns the modified List`() {
        // Given
        val action: (CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>) -> CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> =
            { item ->
                item.copy(
                    item = Fhir4QuestionnaireResponseItemListWrapper(emptyList()),
                    valueDateTime = null,
                    valueString = null
                )
            }

        val givenItems = listOf(
            Fhir4QuestionnaireResponseItemAnswer(id = "1"),
            Fhir4QuestionnaireResponseItemAnswer(id = "2"),
            Fhir4QuestionnaireResponseItemAnswer(id = "3"),
        )

        // When
        val actual: Any = Fhir4QuestionnaireResponseItemAnswerListWrapper(givenItems).map(action)

        // Then
        assertTrue(actual is Fhir4QuestionnaireResponseItemAnswerListWrapper)
        actual.unwrap().forEach { item ->
            assertEquals(
                actual = item.item,
                expected = emptyList()
            )
        }
    }

    private class Fhir4ResponseItemAnswerCollection : Collection<CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
        override val size: Int
            get() = TODO("Not yet implemented")

        override fun contains(element: CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>): Boolean {
            TODO("Not yet implemented")
        }

        override fun containsAll(elements: Collection<CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>>): Boolean {
            TODO("Not yet implemented")
        }

        override fun isEmpty(): Boolean {
            TODO("Not yet implemented")
        }

        override fun iterator(): Iterator<CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime>> {
            TODO("Not yet implemented")
        }
    }
}
