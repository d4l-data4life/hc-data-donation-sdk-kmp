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

internal class UserConsentApiServiceStub : UserConsentContract.ApiService, MockContract.Stub {
    var whenCreateUserConsent: ((String, String, String) -> Unit)? = null
    var whenFetchUserConsents: ((String, Boolean?, String?) -> List<UserConsent>)? = null
    var whenRevokeUserConsent: ((String, String) -> Unit)? = null

    override suspend fun createUserConsent(
        accessToken: String,
        consentDocumentKey: String,
        consentDocumentVersion: String
    ) {
        return whenCreateUserConsent?.invoke(
            accessToken,
            consentDocumentKey,
            consentDocumentVersion
        ) ?: throw MockException()
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        latestConsent: Boolean?,
        consentDocumentKey: String?
    ): List<UserConsent> {
        return whenFetchUserConsents?.invoke(
            accessToken,
            latestConsent,
            consentDocumentKey
        ) ?: throw MockException()
    }

    override suspend fun revokeUserConsent(accessToken: String, consentDocumentKey: String) {
        return whenRevokeUserConsent?.invoke(accessToken, consentDocumentKey) ?: throw MockException()
    }

    override fun clear() {
        whenCreateUserConsent = null
        whenFetchUserConsents = null
        whenRevokeUserConsent = null
    }
}
