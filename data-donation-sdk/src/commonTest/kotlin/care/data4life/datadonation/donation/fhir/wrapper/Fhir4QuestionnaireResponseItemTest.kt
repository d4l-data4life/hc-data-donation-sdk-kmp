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
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4QuestionnaireResponseItemTest {
    @Test
    fun `It fulfils QuestionnaireResponseItem`() {
        val questionnaireResponse: Any = Fhir4QuestionnaireResponseItemWrapper(
            Fhir4QuestionnaireResponseItem(
                linkId = "abc"
            )
        )

        assertTrue(questionnaireResponse is CompatibilityWrapperContract.QuestionnaireResponseItem<*, *, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir4QuestionnaireResponseItem`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "abc"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenQuestionnaireResponseItem
        )
    }

    @Test
    fun `It has the linkId of the wrapped QuestionnaireResponseItem`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).linkId

        // Then
        assertEquals(
            actual = actual,
            expected = givenQuestionnaireResponseItem.linkId
        )
    }

    @Test
    fun `It has no Items if the given Fhir4QuestionnaireResponseItem contains no QuestionnaireResponseItems`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).item

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has Items if the given Fhir4QuestionnaireResponseItem contains a List of QuestionnaireResponseItem`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            item = emptyList()
        )

        // When
        val actual: Any? = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).item

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemList<*, *, *>)
    }

    @Test
    fun `It has no ItemsAnswers if the given Fhir4QuestionnaireResponseItem contains no QuestionnaireResponseItemsAnswer`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).answer

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has ItemsAnswers if the given Fhir4QuestionnaireResponseItem contains a List of QuestionnaireResponseItemsAnswer`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            answer = emptyList()
        )

        // When
        val actual: Any? = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).answer

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemAnswerList<*, *, *>)
    }

    @Test
    fun `Given copy had been called with null as Items, it returns a new QuestionnaireResponseItem and its wrapped Fhir4QuestionnaireResponseItem contains no Items`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            item = emptyList()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).copy(
            item = null,
            answer = null
        )

        // Then
        assertNull(actual.item)
        assertNull(actual.unwrap().item)
    }

    @Test
    fun `Given copy had been called with Items, it returns a new QuestionnaireResponseItem and its wrapped Fhir4QuestionnaireResponseItem contains the new Items`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            item = emptyList()
        )

        val newValue = Fhir4QuestionnaireResponseItemListWrapper(
            listOf(
                Fhir4QuestionnaireResponseItem(
                    linkId = "23"
                )
            )
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).copy(
            item = newValue,
            answer = null
        )

        // Then
        assertEquals(
            actual = actual.item?.unwrap(),
            expected = newValue.unwrap()
        )

        assertEquals(
            actual = actual.unwrap().item,
            expected = newValue.unwrap()
        )
    }

    @Test
    fun `Given copy had been called with ItemsAnswers, it returns a new QuestionnaireResponseItem and its wrapped Fhir4QuestionnaireResponseItem contains no ItemsAnswers`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            answer = emptyList()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).copy(
            item = null,
            answer = null
        )

        // Then
        assertNull(actual.answer)
        assertNull(actual.unwrap().answer)
    }

    @Test
    fun `Given copy had been called with ItemsAnswers, it returns a new QuestionnaireResponseItem and its wrapped Fhir4QuestionnaireResponseItem contains the new ItemsAnswers`() {
        // Given
        val givenQuestionnaireResponseItem = Fhir4QuestionnaireResponseItem(
            linkId = "id",
            item = emptyList()
        )

        val newValue = Fhir4QuestionnaireResponseItemAnswerListWrapper(
            listOf(
                Fhir4QuestionnaireResponseItemAnswer(
                    id = "23"
                )
            )
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemWrapper(givenQuestionnaireResponseItem).copy(
            item = null,
            answer = newValue
        )

        // Then
        assertEquals(
            actual = actual.unwrap().answer,
            expected = newValue.unwrap()
        )

        assertEquals(
            actual = actual.unwrap().answer,
            expected = newValue.unwrap()
        )
    }
}
