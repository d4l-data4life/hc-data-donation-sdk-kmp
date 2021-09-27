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

import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4QuestionnaireResponseItemAnswerTest {
    @Test
    fun `It fulfils Fhir4QuestionnaireResponseItemAnswer`() {
        val questionnaireResponse: Any = Fhir4QuestionnaireResponseItemAnswerWrapper(
            Fhir4QuestionnaireResponseItemAnswer()
        )

        assertTrue(questionnaireResponse is CompatibilityWrapperContract.QuestionnaireResponseItemAnswer<*, *, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir4QuestionnaireResponseItemAnswer`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer()

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenQuestionnaireResponseItemAnswer
        )
    }

    @Test
    fun `It exposes the ValueString of the wrappend QuestionnaireResponseItemAnswer`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            valueString = "asd"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).valueString

        // Then
        assertEquals(
            actual = actual,
            expected = givenQuestionnaireResponseItemAnswer.valueString
        )
    }

    @Test
    fun `It has no Items if the given Fhir4QuestionnaireResponseItem contains no QuestionnaireResponseItems`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).item

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has Items if the given Fhir4QuestionnaireResponseItem contains a List of QuestionnaireResponseItem`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            item = emptyList()
        )

        // When
        val actual: Any? = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).item

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemList<*, *, *>)
    }

    @Test
    fun `It has no ValueDateTime if the given Fhir4QuestionnaireResponseItemAnwser contains no ValueDate`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).valueDateTime

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has ValueDateTime if the given Fhir4QuestionnaireResponseItemAnwser contains a ValueDate`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            valueDateTime = Fhir4DateTime(XsDateTime(XsDate(12)))
        )

        // When
        val actual: Any? = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).valueDateTime

        // Then
        assertTrue(actual is CompatibilityWrapperContract.DateTime<*>)
    }

    @Test
    fun `Given copy had been called with null as Items, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains no Items`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            item = emptyList()
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = null,
            valueDateTime = null,
            valueString = null
        )

        // Then
        assertNull(actual.item)
        assertNull(actual.unwrap().item)
    }

    @Test
    fun `Given copy had been called with Items, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains the new Items`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
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
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = newValue,
            valueDateTime = null,
            valueString = null
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
    fun `Given copy had been called with null as ValueString, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains no ValueString`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            valueString = "something"
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = null,
            valueDateTime = null,
            valueString = null
        )

        // Then
        assertNull(actual.valueString)
        assertNull(actual.unwrap().valueString)
    }

    @Test
    fun `Given copy had been called with a String, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains the new ValueString`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            item = emptyList()
        )

        val newValue = "something"

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = null,
            valueDateTime = null,
            valueString = newValue
        )

        // Then
        assertEquals(
            actual = actual.valueString,
            expected = newValue
        )

        assertEquals(
            actual = actual.unwrap().valueString,
            expected = newValue
        )
    }

    @Test
    fun `Given copy had been called with null as ValueDateTime, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains no ValueDateTime`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            valueDateTime = Fhir4DateTime(XsDateTime(XsDate(23)))
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = null,
            valueDateTime = null,
            valueString = null
        )

        // Then
        assertNull(actual.valueDateTime)
        assertNull(actual.unwrap().valueDateTime)
    }

    @Test
    fun `Given copy had been called with a wrapped DateTime, it returns a new QuestionnaireResponseItemAnswer and its wrapped Fhir4QuestionnaireResponseItemAnswer contains the new ValueDateTime`() {
        // Given
        val givenQuestionnaireResponseItemAnswer = Fhir4QuestionnaireResponseItemAnswer(
            id = "23",
            item = emptyList()
        )

        val newValue = Fhir4DateTimeWrapper(
            Fhir4DateTime(XsDateTime(XsDate(23)))
        )

        // When
        val actual = Fhir4QuestionnaireResponseItemAnswerWrapper(givenQuestionnaireResponseItemAnswer).copy(
            item = null,
            valueDateTime = newValue,
            valueString = null
        )

        // Then
        assertEquals(
            actual = actual.valueDateTime?.unwrap(),
            expected = newValue.unwrap()
        )

        assertEquals(
            actual = actual.unwrap().valueDateTime,
            expected = newValue.unwrap()
        )
    }
}
