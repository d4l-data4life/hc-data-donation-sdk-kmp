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

import care.data4life.hl7.fhir.stu3.model.FhirResource

internal interface AnonymizationContract {
    interface Redaction {
        suspend fun redact(resources: List<FhirResource>): List<FhirResource>

        companion object {
            const val REDACTED = "REDACTED"
            // see: https://github.com/gesundheitscloud/delphi/blob/8c2b45aabf79e5f73c8e4c0c9439b58f7c998335/src/global/constants.ts#L6
            val REDACTABLE_FIELDS = listOf(
                "FN"
            )
        }
    }
}
