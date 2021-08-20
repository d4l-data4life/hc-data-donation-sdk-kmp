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
import care.data4life.hl7.fhir.common.datetime.XsTime
import care.data4life.hl7.fhir.common.datetime.XsTimeZone
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal object DateTimeSmearer : AnonymizationContract.DateTimeSmearer {
    // see: https://github.com/moment/luxon/blob/9ef24d66e7a7737d9cb0146548c6c68d1606048b/src/datetime.js#L1411
    private fun LocalDateTime.startOfDay(timeZone: TimeZone): LocalDateTime {
        return this.toInstant(timeZone)
            .minus(this.nanosecond, DateTimeUnit.NANOSECOND, timeZone)
            .minus(this.second, DateTimeUnit.SECOND, timeZone)
            .minus(this.minute, DateTimeUnit.MINUTE, timeZone)
            .minus(this.hour, DateTimeUnit.HOUR, timeZone)
            .toLocalDateTime(timeZone)
    }

    private fun LocalDateTime.startOfWeek(timeZone: TimeZone): LocalDateTime {
        return this.toInstant(timeZone)
            .minus(this.nanosecond, DateTimeUnit.NANOSECOND, timeZone)
            .minus(this.second, DateTimeUnit.SECOND, timeZone)
            .minus(this.minute, DateTimeUnit.MINUTE, timeZone)
            .minus(this.hour, DateTimeUnit.HOUR, timeZone)
            .minus(this.dayOfWeek.ordinal, DateTimeUnit.DAY, timeZone)
            .toLocalDateTime(timeZone)
    }

    private fun LocalDateTime.startOfMonth(timeZone: TimeZone): LocalDateTime {
        return this.toInstant(timeZone)
            .minus(this.nanosecond, DateTimeUnit.NANOSECOND, timeZone)
            .minus(this.second, DateTimeUnit.SECOND, timeZone)
            .minus(this.minute, DateTimeUnit.MINUTE, timeZone)
            .minus(this.hour, DateTimeUnit.HOUR, timeZone)
            .minus(this.dayOfMonth - 1, DateTimeUnit.DAY, timeZone)
            .toLocalDateTime(timeZone)
    }

    // see: https://github.com/moment/luxon/blob/9ef24d66e7a7737d9cb0146548c6c68d1606048b/src/datetime.js#L1463
    private fun LocalDateTime.endOfUnit(
        timeZone: TimeZone,
        transformer: (LocalDateTime) -> LocalDateTime,
        unit: DateTimeUnit
    ): LocalDateTime {
        return transformer(this)
            .toInstant(timeZone)
            .plus(1, unit, timeZone)
            .minus(1, DateTimeUnit.MILLISECOND, timeZone)
            .toLocalDateTime(timeZone)
    }

    private fun LocalDateTime.endOfDay(timeZone: TimeZone): LocalDateTime {
        return this.endOfUnit(
            timeZone,
            { dateTime: LocalDateTime -> dateTime.startOfDay(timeZone) },
            DateTimeUnit.DAY
        )
    }

    private fun LocalDateTime.endOfWeek(timeZone: TimeZone): LocalDateTime {
        return this.endOfUnit(
            timeZone,
            { dateTime: LocalDateTime -> dateTime.startOfWeek(timeZone) },
            DateTimeUnit.WEEK
        )
    }

    private fun LocalDateTime.endOfMonth(timeZone: TimeZone): LocalDateTime {
        return this.endOfUnit(
            timeZone,
            { dateTime: LocalDateTime -> dateTime.startOfMonth(timeZone) },
            DateTimeUnit.MONTH
        )
    }

    // TODO: Remove with Kotlin 1.5.x
    private fun normalizeToUTC(
        fhirTimeZone: XsTimeZone,
        dangledDateTime: Instant
    ): LocalDateTime {
        val utcNormalization = if (fhirTimeZone.positiveOffset) {
            { instant: Instant ->
                instant.minus(
                    fhirTimeZone.hourOffset,
                    DateTimeUnit.HOUR
                ).minus(
                    fhirTimeZone.minuteOffset,
                    DateTimeUnit.MINUTE
                )
            }
        } else {
            { instant: Instant ->
                instant.plus(
                    fhirTimeZone.hourOffset,
                    DateTimeUnit.HOUR
                ).plus(
                    fhirTimeZone.minuteOffset,
                    DateTimeUnit.MINUTE
                )
            }
        }

        return utcNormalization(dangledDateTime).toLocalDateTime(TimeZone.UTC)
    }

    // TODO: Remove with Kotlin 1.5.x
    private fun xsDateToLocalDateTime(fhirDate: XsDate): LocalDateTime {
        return LocalDateTime.parse("${fhirDate}T00:00")
    }

    // TODO: Remove with Kotlin 1.5.x
    private fun xsDateTimeToLocalDateTime(fhirDateTime: XsDateTime): LocalDateTime {
        val date = xsDateToLocalDateTime(fhirDateTime.date)
        val (hour, minutes, seconds, fraction) = fhirDateTime.time!!

        val dangledDateTime = LocalDateTime(
            date.year,
            date.month,
            date.dayOfMonth,
            hour,
            minutes,
            seconds ?: 0,
            ((fraction ?: 0.0) * 1000000000).toInt()
        ).toInstant(TimeZone.UTC)

        return normalizeToUTC(
            fhirDateTime.timeZone!!,
            dangledDateTime
        )
    }

    private fun convertFhirDate(fhirDateTime: XsDateTime): LocalDateTime {
        return if (fhirDateTime.time is XsTime) {
            xsDateTimeToLocalDateTime(fhirDateTime)
        } else {
            xsDateToLocalDateTime(fhirDateTime.date)
        }
    }

    private fun resolveDateTime(fhirDateTime: XsDateTime, location: String): Pair<LocalDateTime, TimeZone>? {
        return try {
            val targetZone = TimeZone.of(location)
            val dateTime = convertFhirDate(fhirDateTime)

            return dateTime to targetZone
        } catch (error: Throwable) {
            // TODO: Should we throw an error here instead?
            null
        }
    }

    private fun useStartOf(
        timeBundle: Pair<LocalDateTime, TimeZone>,
        rule: BlurFunction
    ): LocalDateTime {
        val (dateTime, timeZone) = timeBundle

        return when (rule) {
            BlurFunction.START_OF_WEEK -> dateTime.startOfWeek(timeZone)
            BlurFunction.START_OF_MONTH -> dateTime.startOfMonth(timeZone)
            else -> dateTime.startOfDay(timeZone)
        }
    }

    private fun useEndOf(
        timeBundle: Pair<LocalDateTime, TimeZone>,
        rule: BlurFunction
    ): LocalDateTime {
        val (dateTime, timeZone) = timeBundle

        return when (rule) {
            BlurFunction.END_OF_WEEK -> dateTime.endOfWeek(timeZone)
            BlurFunction.END_OF_MONTH -> dateTime.endOfMonth(timeZone)
            else -> dateTime.endOfDay(timeZone)
        }
    }

    private fun resolveRootBlurFunction(
        timeBundle: Pair<LocalDateTime, TimeZone>,
        rule: BlurFunction
    ): LocalDateTime {
        return if (rule.value.startsWith("start")) {
            useStartOf(timeBundle, rule)
        } else {
            useEndOf(timeBundle, rule)
        }
    }

    private fun blurToXsDateTime(
        timeBundle: Pair<LocalDateTime, TimeZone>,
        rule: BlurFunction
    ): XsDateTime {
        val dateTime = resolveRootBlurFunction(timeBundle, rule)

        return XsDateTime(
            date = XsDate(
                year = dateTime.year,
                month = dateTime.monthNumber,
                day = dateTime.dayOfMonth
            )
        )
    }

    override fun blur(
        fhirDateTime: XsDateTime,
        location: String,
        rule: BlurFunction
    ): XsDateTime {
        val timeBundle = resolveDateTime(fhirDateTime, location)

        return if (timeBundle is Pair<*, *>) {
            blurToXsDateTime(timeBundle, rule)
        } else {
            fhirDateTime
        }
    }
}
