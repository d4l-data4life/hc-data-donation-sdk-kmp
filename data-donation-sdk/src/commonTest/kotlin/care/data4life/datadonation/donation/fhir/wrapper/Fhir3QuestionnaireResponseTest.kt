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
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir3QuestionnaireResponseTest {
    @Test
    fun `It fulfils QuestionnaireResponse`() {
        val questionnaireResponse: Any = Fhir3QuestionnaireResponseWrapper(
            Fhir3QuestionnaireResponse(
                status = QuestionnaireResponseStatus.COMPLETED
            )
        )

        assertTrue(questionnaireResponse is CompatibilityWrapperContract.QuestionnaireResponse<*, *, *, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir3QuestionnaireResponse`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenQuestionnaireResponse
        )
    }

    @Test
    fun `It has no QuestionnaireReference if the given Fhir3QuestionnaireResponse contains no Reference for questionnaire`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).questionnaireReference

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has no QuestionnaireReference if the given Fhir3QuestionnaireResponse contains a Reference but no ReferenceValue`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            questionnaire = Reference()
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).questionnaireReference

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has a QuestionnaireReference if the given Fhir3QuestionnaireResponse contains a Reference and a ReferenceValue`() {
        // Given
        val expected = "something"

        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            questionnaire = Reference(
                reference = expected
            )
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).questionnaireReference

        // Then
        assertEquals(
            actual = actual,
            expected = expected
        )
    }

    @Test
    fun `It has no Items if the given Fhir3QuestionnaireResponse contains no QuestionnaireResponseItems`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).item

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has Items if the given Fhir3QuestionnaireResponse contains a List of QuestionnaireResponseItem`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = emptyList()
        )

        // When
        val actual: Any? = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).item

        // Then
        assertTrue(actual is CompatibilityWrapperContract.QuestionnaireResponseItemList<*, *, *>)
    }

    @Test
    fun `Given copy had been called with null as Items, it returns a new QuestionnaireResponse and its wrapped Fhir3QuestionnaireResponse contains no Items Field`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = emptyList()
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).copy(
            item = null,
            authored = null
        )

        // Then
        assertNull(actual.item)
        assertNull(actual.unwrap().item)
    }

    @Test
    fun `Given copy had been called with Items, it returns a new QuestionnaireResponse and its wrapped Fhir3QuestionnaireResponse contains the modified Items`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = emptyList()
        )

        val newValue = listOf(QuestionnaireResponseItem(linkId = "123"))

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).copy(
            item = Fhir3QuestionnaireResponseItemListWrapper(newValue),
            authored = null
        )

        // Then
        assertSame(
            actual = actual.item?.unwrap(),
            expected = newValue
        )
        assertSame(
            actual = actual.unwrap().item,
            expected = newValue
        )
    }

    @Test
    fun `It has no Authored if the given Fhir3QuestionnaireResponse contains no Authored Field`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).authored

        // Then
        assertNull(actual)
    }

    @Test
    fun `It has Authored if the given Fhir3QuestionnaireResponse contains an Authored Field`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            authored = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual: Any? = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).authored

        // Then
        assertTrue(actual is CompatibilityWrapperContract.DateTime<*>)
    }

    @Test
    fun `Given copy had been called with null as DateTime, it returns a new QuestionnaireResponse and its wrapped Fhir3QuestionnaireResponse contains no Authored Field`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            authored = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).copy(
            item = null,
            authored = null
        )

        // Then
        assertNull(actual.authored)
        assertNull(actual.unwrap().authored)
    }

    @Test
    fun `Given copy had been called with a DateTime, it returns a new QuestionnaireResponse and its wrapped Fhir3QuestionnaireResponse contains Authored Field with the given Value`() {
        // Given
        val givenQuestionnaireResponse = Fhir3QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            authored = DateTime(XsDateTime(XsDate(23)))
        )

        val newValue = DateTime(XsDateTime(XsDate(42)))

        // When
        val actual = Fhir3QuestionnaireResponseWrapper(givenQuestionnaireResponse).copy(
            authored = Fhir3DateTimeWrapper(newValue),
            item = null
        )

        // Then
        assertSame(
            actual = actual.authored?.unwrap(),
            expected = newValue
        )
        assertSame(
            actual = actual.unwrap().authored,
            expected = newValue
        )
    }
}
