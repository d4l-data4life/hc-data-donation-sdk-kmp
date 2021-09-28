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

internal class Fhir3CodingWrapper(
    private val coding: Fhir3Coding
) : CompatibilityWrapperContract.Coding<Fhir3Coding> {
    override val code: String?
        get() = coding.code

    override val system: String?
        get() = coding.system

    override fun unwrap(): Fhir3Coding {
        return coding
    }
}

internal class Fhir4CodingWrapper(
    private val coding: Fhir4Coding
) : CompatibilityWrapperContract.Coding<Fhir4Coding> {
    override val code: String?
        get() = coding.code

    override val system: String?
        get() = coding.system

    override fun unwrap(): Fhir4Coding {
        return coding
    }
}
