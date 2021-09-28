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
import care.data4life.hl7.fhir.r4.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4PeriodTest {
    @Test
    fun `It fulfils Period`() {
        val period: Any = Fhir4PeriodWrapper(
            Fhir4Period()
        )

        assertTrue(period is CompatibilityWrapperContract.Period<*, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir4Period`() {
        // Given
        val givenPeriod = Fhir4Period()

        // When
        val actual = Fhir4PeriodWrapper(givenPeriod).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenPeriod
        )
    }

    @Test
    fun `It exposes the wrapped StartValue if it is null`() {
        // When
        val actual = Fhir4PeriodWrapper(
            Fhir4Period()
        ).start

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the wrapped StartValue`() {
        // Given
        val start = Fhir4DateTime(XsDateTime(XsDate(12)))

        // When
        val actual = Fhir4PeriodWrapper(
            Fhir4Period(
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
    fun `Given copy had been called with null as DateTime for Start, it returns a new Period and its wrapped Fhir4Period contains no Start Field`() {
        // Given
        val givenPeriod = Fhir4Period(
            start = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual = Fhir4PeriodWrapper(givenPeriod).copy(
            start = null,
            end = null
        )

        // Then
        assertNull(actual.start)
        assertNull(actual.unwrap().start)
    }

    @Test
    fun `Given copy had been called with a DateTime for Start, it returns a new Period and its wrapped Fhir4Period contains Start Field with the given Value`() {
        // Given
        val givenPeriod = Fhir4Period()

        val newValue = DateTime(XsDateTime(XsDate(42)))

        // When
        val actual = Fhir4PeriodWrapper(givenPeriod).copy(
            start = Fhir4DateTimeWrapper(newValue),
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
        val actual = Fhir4PeriodWrapper(
            Fhir4Period()
        ).end

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the wrapped EndValue`() {
        // Given
        val end = Fhir4DateTime(XsDateTime(XsDate(12)))

        // When
        val actual = Fhir4PeriodWrapper(
            Fhir4Period(
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
    fun `Given copy had been called with null as DateTime for End, it returns a new Period and its wrapped Fhir4Period contains no End Field`() {
        // Given
        val givenPeriod = Fhir4Period(
            start = DateTime(XsDateTime(XsDate(42)))
        )

        // When
        val actual = Fhir4PeriodWrapper(givenPeriod).copy(
            start = null,
            end = null
        )

        // Then
        assertNull(actual.end)
        assertNull(actual.unwrap().end)
    }

    @Test
    fun `Given copy had been called with a DateTime for End, it returns a new Period and its wrapped Fhir4Period contains End Field with the given Value`() {
        // Given
        val givenPeriod = Fhir4Period()

        val newValue = DateTime(XsDateTime(XsDate(42)))

        // When
        val actual = Fhir4PeriodWrapper(givenPeriod).copy(
            end = Fhir4DateTimeWrapper(newValue),
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
