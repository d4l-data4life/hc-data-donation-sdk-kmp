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

package care.data4life.datadonation.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ModelContract {
    enum class Environment(val url: String) {
        DEV("api-phdp-dev.hpsgc.de"),
        SANDBOX("api-phdp-sandbox.hpsgc.de"),
        STAGING("api-staging.data4life.care"),
        PRODUCTION("api.data4life.care")
    }

    @Serializable
    enum class ConsentEvent {
        @SerialName("consent")
        Consent,
        @SerialName("revoke")
        Revoke
    }

    interface ConsentDocument {
        val key: String
        val version: Int
        val processor: String
        val description: String
        val recipient: String
        val language: String
        val text: String
        val requiresToken: Boolean
        val studyId: String
        val programName: String
    }

    interface KeyPair {
        val public: ByteArray
        val private: ByteArray
    }

    interface UserConsent {
        val consentDocumentKey: String
        val consentDocumentVersion: String
        val accountId: String
        val event: ConsentEvent
        val createdAt: String
    }
}
