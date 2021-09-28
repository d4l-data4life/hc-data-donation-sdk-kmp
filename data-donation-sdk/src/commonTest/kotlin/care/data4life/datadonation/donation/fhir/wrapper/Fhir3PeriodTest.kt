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
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir3PeriodTest {
    @Test
    fun `It fulfils Period`() {
        val period: Any = Fhir3PeriodWrapper(
            Fhir3Period()
        )

        assertTrue(period is CompatibilityWrapperContract.Period<*, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir3Period`() {
        // Given
        val givenPeriod = Fhir3Period()

        // When
        val actual = Fhir3PeriodWrapper(givenPeriod).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenPeriod
        )
    }

    @Test
    fun `It exposes the wrapped StartValue if it is null`() {
        // When
        val actual = Fhir3PeriodWrapper(
            Fhir3Period()
        ).start

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the wrapped StartValue`() {
        // Given
        val start = Fhir3DateTime(XsDateTime(XsDate(12)))

        // When
        val actual = Fhir3PeriodWrapper(
            Fhir3Period(
                start = start
            )
        ).start

        // Then
        assertSame(
            actual = actual?.unwrap(),
            expected = start
        )
    }

    @Test
    fun `Given copy had been called with null as DateTime for Start, it returns a new Period and its wrapped Fhir3Period contains no Start Field`() {
        // Given
        val givenPeriod = Fhir3Period(
            start = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual = Fhir3PeriodWrapper(givenPeriod).copy(
            start = null,
            end = null
        )

        // Then
        assertNull(actual.start)
        assertNull(actual.unwrap().start)
    }

    @Test
    fun `Given copy had been called with a DateTime for Start, it returns a new Period and its wrapped Fhir3Period contains Start Field with the given Value`() {
        // Given
        val givenPeriod = Fhir3Period()

        val newValue = DateTime(XsDateTime(XsDate(42)))

        // When
        val actual = Fhir3PeriodWrapper(givenPeriod).copy(
            start = Fhir3DateTimeWrapper(newValue),
            end = null
        )

        // Then
        assertSame(
            actual = actual.start?.unwrap(),
            expected = newValue
        )
        assertSame(
            actual = actual.unwrap().start,
            expected = newValue
        )
    }

    @Test
    fun `It exposes the wrapped EndValue if it is null`() {
        // When
        val actual = Fhir3PeriodWrapper(
            Fhir3Period()
        ).end

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the wrapped EndValue`() {
        // Given
        val end = Fhir3DateTime(XsDateTime(XsDate(12)))

        // When
        val actual = Fhir3PeriodWrapper(
            Fhir3Period(
                end = end
            )
        ).end

        // Then
        assertSame(
            actual = actual?.unwrap(),
            expected = end
        )
    }

    @Test
    fun `Given copy had been called with null as DateTime for End, it returns a new Period and its wrapped Fhir3Period contains no End Field`() {
        // Given
        val givenPeriod = Fhir3Period(
            start = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual = Fhir3PeriodWrapper(givenPeriod).copy(
            start = null,
            end = null
        )

        // Then
        assertNull(actual.end)
        assertNull(actual.unwrap().end)
    }

    @Test
    fun `Given copy had been called with a DateTime for End, it returns a new Period and its wrapped Fhir3Period contains End Field with the given Value`() {
        // Given
        val givenPeriod = Fhir3Period()

        val newValue = DateTime(XsDateTime(XsDate(42)))

        // When
        val actual = Fhir3PeriodWrapper(givenPeriod).copy(
            end = Fhir3DateTimeWrapper(newValue),
            start = null
        )

        // Then
        assertSame(
            actual = actual.end?.unwrap(),
            expected = newValue
        )
        assertSame(
            actual = actual.unwrap().end,
            expected = newValue
        )
    }
}
