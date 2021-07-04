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

package care.data4life.datadonation.mock.stub

import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class UserConsentRemoteStub : RepositoryContract.UserConsentRemoteStorage, MockContract.Stub {
    var whenCreateUserConsent: ((String?, Int, String?) -> Unit)? = null
    var whenFetchUserConsents: ((String?, String?) -> List<UserConsent>)? = null
    var whenSignUserConsentRegistration: ((String, String) -> String)? = null
    var whenSignUserConsentDonation: ((String, String) -> String)? = null
    var whenRevokeUserConsent: ((String, String?) -> Unit)? = null

    override suspend fun createUserConsent(
        accessToken: String,
        version: Int,
        language: String?
    ) = whenCreateUserConsent?.invoke(accessToken, version, language) ?: throw MockException()

    override suspend fun fetchUserConsents(
        accessToken: String,
        consentKey: String?
    ): List<UserConsent> = whenFetchUserConsents?.invoke(accessToken, consentKey) ?: throw MockException()

    override suspend fun signUserConsentRegistration(
        accessToken: String,
        message: String
    ): String = whenSignUserConsentRegistration?.invoke(accessToken, message) ?: throw MockException()

    override suspend fun signUserConsentDonation(
        accessToken: String,
        message: String
    ): String = whenSignUserConsentDonation?.invoke(accessToken, message) ?: throw MockException()

    override suspend fun revokeUserConsent(
        accessToken: String,
        language: String?
    ) = whenRevokeUserConsent?.invoke(accessToken, language) ?: throw MockException()

    override fun clear() {
        whenCreateUserConsent = null
        whenFetchUserConsents = null
        whenSignUserConsentRegistration = null
        whenSignUserConsentDonation = null
        whenRevokeUserConsent = null
    }
}
