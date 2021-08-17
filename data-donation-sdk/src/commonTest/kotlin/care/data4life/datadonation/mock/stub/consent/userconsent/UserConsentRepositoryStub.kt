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

package care.data4life.datadonation.mock.stub.consent.userconsent

import care.data4life.datadonation.consent.userconsent.UserConsentContract
import care.data4life.datadonation.consent.userconsent.model.UserConsent
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import care.data4life.datadonation.networking.AccessToken

internal class UserConsentRepositoryStub : UserConsentContract.Repository, MockContract.Stub {
    var whenCreateUserConsent: ((AccessToken, String, String) -> Unit)? = null
    var whenFetchUserConsents: ((AccessToken, String?) -> List<UserConsent>)? = null
    var whenFetchLatestUserConsents: ((AccessToken) -> List<UserConsent>)? = null
    var whenFetchAllUserConsents: ((AccessToken) -> List<UserConsent>)? = null
    var whenRevokeUserConsent: ((AccessToken, String) -> Unit)? = null

    override suspend fun createUserConsent(
        accessToken: AccessToken,
        consentDocumentKey: String,
        consentDocumentVersion: String
    ) {
        whenCreateUserConsent?.invoke(
            accessToken,
            consentDocumentKey,
            consentDocumentVersion
        ) ?: throw MockException()
    }

    override suspend fun fetchUserConsents(
        accessToken: AccessToken,
        consentDocumentKey: String
    ): List<UserConsent> {
        return whenFetchUserConsents?.invoke(
            accessToken,
            consentDocumentKey
        ) ?: throw MockException()
    }

    override suspend fun fetchLatestUserConsents(
        accessToken: AccessToken,
    ): List<UserConsent> {
        return whenFetchLatestUserConsents?.invoke(
            accessToken,
        ) ?: throw MockException()
    }

    override suspend fun fetchAllUserConsents(
        accessToken: AccessToken,
    ): List<UserConsent> {
        return whenFetchAllUserConsents?.invoke(
            accessToken,
        ) ?: throw MockException()
    }

    override suspend fun revokeUserConsent(
        accessToken: AccessToken,
        consentDocumentKey: String
    ) {
        whenRevokeUserConsent?.invoke(
            accessToken,
            consentDocumentKey
        ) ?: throw MockException()
    }

    override fun clear() {
        whenCreateUserConsent = null
        whenFetchUserConsents = null
        whenFetchAllUserConsents = null
        whenRevokeUserConsent = null
    }
}
