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

import care.data4life.hl7.fhir.common.datetime.XsDateTime

internal class Fhir3DateTimeWrapper(
    private var dateTime: Fhir3DateTime
) : CompatibilityWrapperContract.DateTime<Fhir3DateTime> {
    override val value: XsDateTime
        get() = dateTime.value

    override fun copy(
        value: XsDateTime
    ): CompatibilityWrapperContract.DateTime<Fhir3DateTime> {
        return Fhir3DateTimeWrapper(
            dateTime.copy(value = value)
        )
    }

    override fun unwrap(): Fhir3DateTime {
        return dateTime
    }
}

internal class Fhir4DateTimeWrapper(
    private var dateTime: Fhir4DateTime
) : CompatibilityWrapperContract.DateTime<Fhir4DateTime> {
    override val value: XsDateTime
        get() = dateTime.value

    override fun copy(
        value: XsDateTime
    ): CompatibilityWrapperContract.DateTime<Fhir4DateTime> {
        return Fhir4DateTimeWrapper(
            dateTime.copy(value = value)
        )
    }

    override fun unwrap(): Fhir4DateTime {
        return dateTime
    }
}
