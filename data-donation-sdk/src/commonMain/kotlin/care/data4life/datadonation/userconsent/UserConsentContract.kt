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

package care.data4life.datadonation.userconsent

import care.data4life.datadonation.ConsentDataContract
import care.data4life.datadonation.networking.AccessToken
import care.data4life.datadonation.networking.HttpRuntimeError

internal interface UserConsentContract {
    interface ApiService {
        suspend fun fetchUserConsents(
            accessToken: AccessToken,
            latestConsent: Boolean?,
            consentDocumentKey: String? = null
        ): List<ConsentDataContract.UserConsent>

        suspend fun createUserConsent(
            accessToken: AccessToken,
            consentDocumentKey: String,
            version: String
        )

        suspend fun revokeUserConsent(
            accessToken: AccessToken,
            consentDocumentKey: String
        )

        companion object {
            val PATH = listOf("consent", "api", "v1", "userConsents")

            object PARAMETER {
                const val USER_CONSENT_KEY = "consentDocumentKey"
                const val LANGUAGE = "language"
                const val VERSION = "version"
                const val LATEST_CONSENT = "latest"
            }
        }

        interface ErrorHandler {
            fun handleFetchUserConsents(error: HttpRuntimeError): UserConsentError
            fun handleCreateUserConsent(error: HttpRuntimeError): UserConsentError
            fun handleRevokeUserConsent(error: HttpRuntimeError): UserConsentError
        }
    }

    interface Repository {
        suspend fun createUserConsent(
            accessToken: AccessToken,
            consentDocumentKey: String,
            consentDocumentVersion: String
        )

        suspend fun fetchUserConsents(
            accessToken: AccessToken,
            consentDocumentKey: String
        ): List<ConsentDataContract.UserConsent>

        suspend fun fetchLatestUserConsents(
            accessToken: AccessToken
        ): List<ConsentDataContract.UserConsent>

        suspend fun fetchAllUserConsents(
            accessToken: AccessToken
        ): List<ConsentDataContract.UserConsent>

        suspend fun revokeUserConsent(
            accessToken: AccessToken,
            consentDocumentKey: String
        )
    }

    interface Interactor {
        suspend fun createUserConsent(
            consentDocumentKey: String,
            consentDocumentVersion: String
        ): ConsentDataContract.UserConsent

        suspend fun fetchUserConsents(
            consentDocumentKey: String
        ): List<ConsentDataContract.UserConsent>

        suspend fun fetchAllUserConsents(): List<ConsentDataContract.UserConsent>

        suspend fun revokeUserConsent(consentDocumentKey: String): ConsentDataContract.UserConsent
    }
}
