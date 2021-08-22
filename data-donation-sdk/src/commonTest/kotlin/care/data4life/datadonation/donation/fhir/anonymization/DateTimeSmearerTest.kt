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

package care.data4life.datadonation.donation.fhir.anonymization

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.common.datetime.XsTimeZone
import care.data4life.hl7.fhir.common.datetime.parser.XsDateTimeParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeSmearerTest {
    @Test
    fun `It fulfils DateTimeSmearer`() {
        val smearer: Any = DateTimeSmearer

        assertTrue(smearer is AnonymizationContract.DateTimeSmearer)
    }

    @Test
    fun `Given blur is called with a XsDateTime, TargetTimeZone and BlurFunction, it reflects the given XsDateTime if the given TargetTimeZone was not valid`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T11:13:56.382Z")
        val targetTimeZone = "somewhere"
        val rule = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and START_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_DAY

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, TargetTimeZone and START_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_DAY

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, TargetTimeZone and START_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_WEEK

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and START_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_WEEK

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, TargetTimeZone and START_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-01T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_MONTH

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and START_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.START_OF_MONTH

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, TargetTimeZone and END_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_DAY

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and END_OF_DAY, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_DAY

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

        // Then
        assertEquals(
            actual = result,
            expected = fhirDateTime
        )
    }

    @Test
    fun `Given blur is called with a XsDateTime, TargetTimeZone and END_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-10T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_WEEK

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and END_OF_WEEK, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_WEEK

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, TargetTimeZone and END_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTimeParser.parse("2021-05-01T01:13:56.382Z").copy(
            timeZone = XsTimeZone(3)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
    fun `Given blur is called with a XsDateTime, which only contains a XsDate, a TargetTimeZone and END_OF_MONTH, it returns only the Start of the day`() {
        // Given
        val fhirDateTime = XsDateTime(
            date = XsDate(2021, 5, 10)
        )
        val targetTimeZone = "Europe/Berlin"
        val rule = BlurFunction.END_OF_MONTH

        // When
        val result = DateTimeSmearer.blur(fhirDateTime, targetTimeZone, rule)

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
