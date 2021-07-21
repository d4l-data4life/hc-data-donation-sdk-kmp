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
import io.ktor.client.statement.HttpStatement
import kotlinx.datetime.Clock

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias AccessToken = String
internal typealias Path = List<String>

internal typealias SessionToken = String
internal typealias DataDonationKey = String
internal typealias AnalyticsKey = String

internal interface ServiceContract {
    enum class Method(name: String) {
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    // TODO Add a new package with potential HTTP Interceptor
    interface RequestBuilder {
        fun setHeaders(header: Header): RequestBuilder
        fun setParameter(parameter: Parameter): RequestBuilder
        fun setAccessToken(token: AccessToken): RequestBuilder
        fun useJsonContentType(): RequestBuilder
        fun setBody(body: Any): RequestBuilder

        fun create(): RequestBuilder

        fun prepare(
            method: Method = Method.GET,
            path: Path = listOf("")
        ): HttpStatement

        companion object {
            const val ACCESS_TOKEN_FIELD = "Authorization"
            const val ACCESS_TOKEN_VALUE_PREFIX = "Bearer"
        }
    }

    interface RequestBuilderTemplate {
        fun create(): RequestBuilder
    }

    interface RequestBuilderTemplateFactory {
        fun getInstance(
            environment: Environment,
            client: HttpClient,
            port: Int? = null
        ): RequestBuilderTemplate
    }

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
            builderTemplateFactory: RequestBuilderTemplateFactory,
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