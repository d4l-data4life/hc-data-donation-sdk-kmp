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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.donation.consentsignature.model.ConsentSignature
import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.networking.AccessToken
import care.data4life.datadonation.networking.HttpRuntimeError

internal interface ConsentSignatureContract {
    interface ApiService {
        suspend fun enableSigning(
            accessToken: AccessToken,
            consentDocumentKey: String,
            message: String
        ): ConsentSignature

        suspend fun sign(
            accessToken: String,
            consentDocumentKey: String,
            message: String
        ): ConsentSignature

        suspend fun disableSigning(
            accessToken: String,
            consentDocumentKey: String,
            message: SignedDeletionMessage
        )

        interface ErrorHandler {
            fun handleEnableSigning(error: HttpRuntimeError): ConsentSignatureError
            fun handleSigning(error: HttpRuntimeError): ConsentSignatureError
            fun handleDisableSigning(error: HttpRuntimeError): ConsentSignatureError
        }

        companion object {
            val ROUTE = listOf("consent", "api", "v1", "userConsents")
            const val SIGNATURES = "signatures"
        }
    }

    interface Repository
}
