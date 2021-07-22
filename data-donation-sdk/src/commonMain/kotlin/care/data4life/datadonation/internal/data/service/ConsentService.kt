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
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentCreationPayload
import care.data4life.datadonation.internal.data.model.ConsentRevocationPayload
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.model.ConsentSignatureType
import care.data4life.datadonation.internal.data.model.ConsentSigningRequest
import care.data4life.datadonation.internal.data.service.ServiceContract.Companion.DEFAULT_DONATION_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LANGUAGE
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.VERSION
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.CONSENTS_DOCUMENTS
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.SIGNATURES
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.USER_CONSENTS
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.ROOT
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.receive
import care.data4life.datadonation.lang.HttpRuntimeError
import kotlinx.datetime.Clock

internal class ConsentService constructor(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: ServiceContract.ConsentService.ConsentErrorHandler,
    private val clock: Clock
) : ServiceContract.ConsentService {
    private fun buildPath(
        endpoint: String,
        vararg tail: String
    ): List<String> {
        val path = ROOT.toMutableList().also { it.add(endpoint) }
        if (tail.isNotEmpty()) {
            tail.forEach { directory -> path.add(directory) }
        }

        return path
    }

    override suspend fun fetchConsentDocuments(
        accessToken: String,
        version: Int?,
        language: String?,
        consentKey: String
    ): List<ConsentDocument> {
        val path = buildPath(CONSENTS_DOCUMENTS)
        val parameter = mapOf(
            USER_CONSENT_KEY to consentKey,
            VERSION to version,
            LANGUAGE to language
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .setParameter(parameter)
            .prepare(
                Networking.Method.GET,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchConsentDocuments(error)
        }
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        latestConsent: Boolean?,
        consentKey: String?
    ): List<UserConsent> {
        val path = buildPath(USER_CONSENTS)
        val parameter = mapOf(
            LATEST_CONSENT to latestConsent,
            USER_CONSENT_KEY to consentKey,
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .setParameter(parameter)
            .prepare(
                Networking.Method.GET,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchUserConsents(error)
        }
    }

    override suspend fun createUserConsent(
        accessToken: String,
        consentKey: String,
        version: Int
    ) {
        val path = buildPath(USER_CONSENTS)
        val payload = ConsentCreationPayload(
            consentKey,
            version,
            clock.now().toString()
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(
                Networking.Method.POST,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleCreateUserConsent(error)
        }
    }

    // see: https://github.com/gesundheitscloud/consent-management/blob/master/swagger/api.yml#L356
    override suspend fun requestSignatureConsentRegistration(
        accessToken: String,
        message: String
    ): ConsentSignature {
        val consentDocumentKey = DEFAULT_DONATION_CONSENT_KEY
        val path = buildPath(
            USER_CONSENTS,
            consentDocumentKey,
            SIGNATURES
        )
        val payload = ConsentSigningRequest(
            consentDocumentKey,
            message,
            ConsentSignatureType.CONSENT_ONCE.apiValue
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(
                Networking.Method.POST,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    override suspend fun requestSignatureDonation(
        accessToken: String,
        message: String
    ): ConsentSignature {
        val consentDocumentKey = DEFAULT_DONATION_CONSENT_KEY
        val path = buildPath(
            USER_CONSENTS,
            consentDocumentKey,
            SIGNATURES
        )
        val payload = ConsentSigningRequest(
            consentDocumentKey,
            message,
            ConsentSignatureType.NORMAL_USE.apiValue
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(
                Networking.Method.PUT,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleRequestSignatureDonation(error)
        }
    }

    override suspend fun revokeUserConsent(accessToken: String, consentKey: String) {
        val path = buildPath(USER_CONSENTS)
        val payload = ConsentRevocationPayload(consentKey)

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(
                Networking.Method.DELETE,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleRevokeUserConsent(error)
        }
    }
}
