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
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir3DateTimeTest {
    @Test
    fun `It fulfils DateTime`() {
        val dateTime: Any = Fhir3DateTimeWrapper(
            Fhir3DateTime(
                XsDateTime(XsDate(12))
            )
        )

        assertTrue(dateTime is CompatibilityWrapperContract.DateTime<*>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir3DateTime`() {
        // Given
        val givenDateTime = Fhir3DateTime(
            XsDateTime(XsDate(12))
        )

        // When
        val actual = Fhir3DateTimeWrapper(givenDateTime).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenDateTime
        )
    }

    @Test
    fun `It exposes the given XsDateTime of the wrapped DateTime`() {
        // Given
        val givenDateTime = Fhir3DateTime(
            XsDateTime(XsDate(12))
        )

        // When
        val actual = Fhir3DateTimeWrapper(givenDateTime).value

        // Then
        assertSame(
            actual = actual,
            expected = givenDateTime.value
        )
    }

    @Test
    fun `Given copy had been called with XsDateTime, it returns a new DateTime and its wrapped Fhir3DateTime contains the new value`() {
        // Given
        val givenDateTime = Fhir3DateTime(
            XsDateTime(XsDate(12))
        )
        val newDateTime = XsDateTime(XsDate(42))

        // When
        val actual = Fhir3DateTimeWrapper(givenDateTime).copy(newDateTime)

        // Then
        assertSame(
            actual = actual.unwrap().value,
            expected = newDateTime
        )
        assertSame(
            actual = actual.value,
            expected = newDateTime
        )
    }
}
