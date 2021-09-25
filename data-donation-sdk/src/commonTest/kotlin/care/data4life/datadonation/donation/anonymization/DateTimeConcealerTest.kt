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

package care.data4life.datadonation.donation.anonymization

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.common.datetime.XsTimeZone
import care.data4life.hl7.fhir.common.datetime.parser.XsDateTimeParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeConcealerTest {
    @Test
    fun `It fulfils DateTimeConcealer`() {
        val concealer: Any = DateTimeConcealer

        assertTrue(concealer is AnonymizationContract.DateTimeConcealer)
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and BlurFunction, it returns null if the given Location was not valid`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T11:13:56.382Z")
        val location = "somewhere"
        val function = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and START_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_DAY

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and START_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_DAY

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 9
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and START_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_WEEK

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 3
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and START_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_WEEK

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and START_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-01T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_MONTH

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    month = 4,
                    day = 1
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and START_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.START_OF_MONTH

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 1
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and END_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_DAY

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 9
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and END_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_DAY

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and END_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_WEEK

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 9
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and END_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_WEEK

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 16
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, Location and END_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-01T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    month = 4,
                    day = 30
                )
            )
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a Location and END_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val location = "Europe/Berlin"
        val function = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeConcealer.blur(fhirDateTime, location, function)

        // Then
        assertEquals(
            actual = result,
            expected = XsDateTime(
                date = fhirDateTime.date.copy(
                    day = 31
                )
            )
        )
    }
}
