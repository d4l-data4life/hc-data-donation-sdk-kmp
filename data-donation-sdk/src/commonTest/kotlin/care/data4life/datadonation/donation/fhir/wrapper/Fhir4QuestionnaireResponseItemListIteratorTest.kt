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
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4QuestionnaireResponseItemListIteratorTest {
    @Test
    fun `It fulfils QuestionnaireResponseItemListIterator`() {
        val questionnaireResponseItemListIterator: Any = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            emptyList<Fhir4QuestionnaireResponseItem>().listIterator()
        )

        assertTrue(questionnaireResponseItemListIterator is CompatibilityWrapperContract.QuestionnaireResponseItemListIterator<*, *, *>)
    }

    @Test
    fun `Given unwrap is called it returns the wrapped iterator`() {
        // Given
        val givenIterator = emptyList<Fhir4QuestionnaireResponseItem>().listIterator()

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(givenIterator).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenIterator
        )
    }

    @Test
    fun `Given hasNext is called it returns true if Items are left`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).hasNext()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given hasNext is called it returns false if no Items are left`() {
        // Given
        val given = emptyList<Fhir4QuestionnaireResponseItem>()

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).hasNext()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given next is called it returns the a QuestionnaireResponseItem`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).next()

        // Then
        assertSame(
            actual = actual.unwrap(),
            expected = given.listIterator().next()
        )
    }

    @Test
    fun `Given hasPrevious is called it returns false if there is no previous Item`() {
        // Given
        val given = emptyList<Fhir4QuestionnaireResponseItem>()

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).hasPrevious()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given hasPrevious is called it returns true if there is a previous Item`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val iterator = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        )
        iterator.next()
        val actual = iterator.hasPrevious()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given nextIndex is called it returns the next index`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        ).listIterator()

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given
        ).nextIndex()

        // Then
        assertEquals(
            actual = actual,
            expected = given.nextIndex()
        )
    }

    @Test
    fun `Given previous is called it returns the a QuestionnaireResponseItem`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).also { it.next() }.previous()

        // Then
        assertSame(
            actual = actual.unwrap(),
            expected = given.listIterator().also { it.next() }.previous()
        )
    }

    @Test
    fun `Given previousIndex is called it returns the previous index`() {
        // Given
        val given = listOf(
            Fhir4QuestionnaireResponseItem(linkId = "1"),
            Fhir4QuestionnaireResponseItem(linkId = "2"),
            Fhir4QuestionnaireResponseItem(linkId = "3"),
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemListIteratorWrapper(
            given.listIterator()
        ).also { it.next() }.previousIndex()

        // Then
        assertSame(
            actual = actual,
            expected = given.listIterator().also { it.next() }.previousIndex()
        )
    }
}
