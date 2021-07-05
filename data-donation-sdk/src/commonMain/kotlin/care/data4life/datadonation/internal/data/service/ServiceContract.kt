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
import io.ktor.client.HttpClient

typealias Header = Map<String, String>
typealias Parameter = Map<String, String>
typealias AccessToken = String
typealias Path = List<String>

internal interface ServiceContract {
    enum class Method(name: String) {
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    interface Service

    interface ServiceFactory {
        fun getInstance(
            environment: Environment,
            client: HttpClient
        ) : Service
    }

    // TODO Add a new package with potential HTTP Interceptor
    interface CallBuilder : Service {
        fun setHeaders(header: Header): CallBuilder
        fun setParameter(parameter: Parameter): CallBuilder
        fun setAccessToken(token: AccessToken): CallBuilder
        fun useJsonContentType(): CallBuilder
        fun setBody(body: Any): CallBuilder

        suspend fun execute(
            method: Method = Method.GET,
            path: Path = listOf(""),
            port: Int? = null
        ): Any

        companion object {
            const val ACCESS_TOKEN_FIELD = "Authorization"
            const val ACCESS_TOKEN_VALUE_PREFIX = "Bearer"
        }
    }

    interface CallBuilderFactory : ServiceFactory

    interface ConsentService : Service {
        suspend fun fetchConsentDocuments(
            accessToken: String,
            version: Int?,
            language: String?,
            consentKey: String
        ): List<ConsentDocument>

        suspend fun fetchUserConsents(
            accessToken: String,
            latest: Boolean?,
            consentKey: String? = null
        ): List<UserConsent>

        suspend fun createUserConsent(
            accessToken: String,
            version: Int,
            language: String?
        )

        suspend fun requestSignatureRegistration(
            accessToken: String,
            message: String
        ): ConsentSignature

        suspend fun requestSignatureDonation(
            accessToken: String,
            message: String
        ): ConsentSignature

        suspend fun revokeUserConsent(accessToken: String, language: String?)

        companion object {
            const val XSRF_VALIDITY = 23 * 60 * 60 * 1000

            object Endpoints {
                const val userConsents = "userConsents"
                const val consentDocuments = "consentDocuments"
                const val token = "xsrf"
            }

            object Parameters {
                const val consentDocumentKey = "key"
                const val userConsentDocumentKey = "consentDocumentKey"
                const val latest = "latest"
                const val language = "language"
                const val version = "version"
            }

            object Headers {
                const val XSRFToken = "X-Csrf-Token"
            }
        }
    }

    interface ConsentServiceFactory : ServiceFactory

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
    }
}
