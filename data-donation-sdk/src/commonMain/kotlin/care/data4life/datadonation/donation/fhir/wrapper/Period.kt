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

internal class Fhir3PeriodWrapper(
    private var period: Fhir3Period
) : CompatibilityWrapperContract.Period<Fhir3Period, Fhir3DateTime> {
    private fun getDateTimeField(field: Fhir3DateTime?): CompatibilityWrapperContract.DateTime<Fhir3DateTime>? {
        return if (field is Fhir3DateTime) {
            Fhir3DateTimeWrapper(field)
        } else {
            null
        }
    }

    override val start: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?
        get() = getDateTimeField(period.start)

    override val end: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?
        get() = getDateTimeField(period.end)

    override fun unwrap(): Fhir3Period {
        return period
    }

    override fun copy(
        start: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?,
        end: CompatibilityWrapperContract.DateTime<Fhir3DateTime>?
    ): CompatibilityWrapperContract.Period<Fhir3Period, Fhir3DateTime> {
        return Fhir3PeriodWrapper(
            period.copy(
                start = start?.unwrap(),
                end = end?.unwrap()
            )
        )
    }
}

internal class Fhir4PeriodWrapper(
    private var period: Fhir4Period
) : CompatibilityWrapperContract.Period<Fhir4Period, Fhir4DateTime> {
    private fun getDateTimeField(field: Fhir4DateTime?): CompatibilityWrapperContract.DateTime<Fhir4DateTime>? {
        return if (field is Fhir4DateTime) {
            Fhir4DateTimeWrapper(field)
        } else {
            null
        }
    }

    override val start: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?
        get() = getDateTimeField(period.start)

    override val end: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?
        get() = getDateTimeField(period.end)

    override fun unwrap(): Fhir4Period {
        return period
    }

    override fun copy(
        start: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?,
        end: CompatibilityWrapperContract.DateTime<Fhir4DateTime>?
    ): CompatibilityWrapperContract.Period<Fhir4Period, Fhir4DateTime> {
        return Fhir4PeriodWrapper(
            period.copy(
                start = start?.unwrap(),
                end = end?.unwrap()
            )
        )
    }
}
