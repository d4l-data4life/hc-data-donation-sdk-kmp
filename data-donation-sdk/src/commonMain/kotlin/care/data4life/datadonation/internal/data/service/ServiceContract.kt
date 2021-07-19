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

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.model.DonationPayload
import care.data4life.datadonation.internal.data.service.networking.Networking
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock

typealias SessionToken = String
typealias DataDonationKey = String
typealias AnalyticsKey = String

internal interface ServiceContract {
    interface CredentialService {
        fun getDataDonationPublicKey(): DataDonationKey
        fun getAnalyticsPlatformPublicKey(): AnalyticsKey
    }

    interface UserSessionTokenService {
        suspend fun getUserSessionToken(): SessionToken
    }

    interface ConsentService {
        suspend fun fetchConsentDocuments(
            accessToken: String,
            version: Int?,
            language: String?,
            consentKey: String
        ): List<ConsentDocument>

        suspend fun fetchUserConsents(
            accessToken: String,
            latestConsent: Boolean?,
            consentKey: String? = null
        ): List<UserConsent>

        suspend fun createUserConsent(
            accessToken: String,
            consentKey: String,
            version: Int
        )

        suspend fun requestSignatureConsentRegistration(
            accessToken: String,
            message: String
        ): ConsentSignature

        suspend fun requestSignatureDonation(
            accessToken: String,
            message: String
        ): ConsentSignature

        suspend fun revokeUserConsent(accessToken: String, consentKey: String)

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
    }

    interface ConsentServiceFactory {
        fun getInstance(
            environment: Environment,
            client: HttpClient,
            builderTemplateFactory: Networking.RequestBuilderTemplateFactory,
            clock: Clock
        ): ConsentService
    }

    interface DonationService {
        suspend fun requestToken(): String
        suspend fun registerNewDonor(payload: ByteArray)
        suspend fun donateResources(payload: DonationPayload)

        companion object {
            object Endpoints {
                const val token = "token"
                const val register = "register"
                const val donate = "donate"
            }

            object FormDataEntries {
                const val request = "request"
                const val signature = "signature_"
                const val donation = "donation_"
            }

            object FormDataHeaders {
                const val fileName = "filename="
            }
        }
    }

    companion object {
        const val DEFAULT_DONATION_CONSENT_KEY = "d4l.data-donation.broad"
        const val LOCAL_PORT = 8080 // TODO -> Do we need a local env?
    }
}
