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

package care.data4life.datadonation.consent.userconsent

import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.PATH
import care.data4life.datadonation.consent.userconsent.model.ConsentCreationPayload
import care.data4life.datadonation.consent.userconsent.model.ConsentRevocationPayload
import care.data4life.datadonation.consent.userconsent.model.UserConsent
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.receive
import kotlinx.datetime.Clock
import care.data4life.datadonation.ConsentDataContract.UserConsent as UserConsentContract

internal class UserConsentApiService constructor(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.ErrorHandler,
    private val clock: Clock
) : care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService {

    override suspend fun createUserConsent(
        accessToken: String,
        consentDocumentKey: String,
        version: String
    ) {
        val payload = ConsentCreationPayload(
            consentDocumentKey,
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
                PATH
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleCreateUserConsent(error)
        }
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        latestConsent: Boolean?,
        consentDocumentKey: String?
    ): List<UserConsentContract> {
        val parameter = mapOf(
            LATEST_CONSENT to latestConsent,
            USER_CONSENT_KEY to consentDocumentKey,
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .setParameter(parameter)
            .prepare(
                Networking.Method.GET,
                PATH
            )

        return try {
            receive<List<UserConsent>>(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchUserConsents(error)
        }
    }

    override suspend fun revokeUserConsent(accessToken: String, consentDocumentKey: String) {
        val payload = ConsentRevocationPayload(consentDocumentKey)

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(
                Networking.Method.DELETE,
                PATH
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleRevokeUserConsent(error)
        }
    }
}
