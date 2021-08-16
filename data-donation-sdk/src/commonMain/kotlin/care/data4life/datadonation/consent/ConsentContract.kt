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

package care.data4life.datadonation.consent

import care.data4life.datadonation.ConsentDataContract
import care.data4life.datadonation.networking.HttpRuntimeError

interface ConsentContract {
    interface ConsentApiService {
        suspend fun fetchConsentDocuments(
            accessToken: String,
            version: String?,
            language: String?,
            consentDocumentKey: String
        ): List<ConsentDataContract.ConsentDocument>

        suspend fun fetchUserConsents(
            accessToken: String,
            latestConsent: Boolean?,
            consentDocumentKey: String? = null
        ): List<ConsentDataContract.UserConsent>

        suspend fun createUserConsent(
            accessToken: String,
            consentDocumentKey: String,
            version: String
        )

        suspend fun revokeUserConsent(accessToken: String, consentDocumentKey: String)

        companion object {
            val ROOT = listOf("consent", "api", "v1")

            object PARAMETER {
                const val USER_CONSENT_KEY = "consentDocumentKey"
                const val LANGUAGE = "language"
                const val VERSION = "version"
                const val LATEST_CONSENT = "latest"
            }

            object PATH {
                const val USER_CONSENTS = "userConsents"
                const val CONSENTS_DOCUMENTS = "consentDocuments"
                const val SIGNATURES = "signatures"
            }
        }

        interface ConsentErrorHandler {
            fun handleFetchConsentDocuments(error: HttpRuntimeError): ConsentServiceError
            fun handleFetchUserConsents(error: HttpRuntimeError): ConsentServiceError
            fun handleCreateUserConsent(error: HttpRuntimeError): ConsentServiceError
            fun handleRequestSignatureConsentRegistration(error: HttpRuntimeError): ConsentServiceError
            fun handleRequestSignatureDonation(error: HttpRuntimeError): ConsentServiceError
            fun handleRevokeUserConsent(error: HttpRuntimeError): ConsentServiceError
        }
    }

    interface ConsentDocumentRepository {
        suspend fun fetchConsentDocuments(
            language: String?,
            version: String?,
            consentDocumentKey: String
        ): List<ConsentDataContract.ConsentDocument>
    }

    interface UserConsentRepository {
        suspend fun createUserConsent(consentDocumentKey: String, version: String)
        suspend fun fetchUserConsents(consentDocumentKey: String? = null): List<ConsentDataContract.UserConsent>
        suspend fun revokeUserConsent(consentDocumentKey: String)
    }
}
