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

package care.data4life.datadonation.donation.program.model

import kotlinx.serialization.Serializable

internal interface ProgramModelContract {
    @Serializable(with = BlurFieldSerializer::class)
    enum class BlurField(val value: String) {
        START_OF_DAY("startOfDay"),
        END_OF_DAY("endOfDay"),
        START_OF_WEEK("startOfWeek"),
        END_OF_WEEK("endOfWeek"),
        START_OF_MONTH("startOfMonth"),
        END_OF_MONTH("endOfMonth")
    }

    interface ProgramItemBlur {
        val linkId: String
        val field: BlurField
    }

    interface ProgramResourceBlur {
        val location: String?
        val authored: BlurField?
        val items: List<ProgramItemBlur>
    }

    interface ProgramResource {
        val url: String
        val versions: List<String>?
        val blur: ProgramResourceBlur?
    }

    interface ProgramAnonymizationBlurValue {
        val location: String
        val authored: BlurField?
        val researchSubject: BlurField?
    }

    interface ProgramAnonymization {
        val blur: ProgramAnonymizationBlurValue?
    }

    interface ProgramConfiguration {
        val consentKey: String
        val resources: List<ProgramResource>
        val anonymization: ProgramAnonymization?
        val triggerList: List<String>?
        val delay: Double
        val studyID: String
    }

    interface Program {
        val name: String
        val slug: String
        val tenantID: String
        val donation: ProgramConfiguration?
    }
}
