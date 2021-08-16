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

package care.data4life.datadonation.mock.stub.consent

import care.data4life.datadonation.consent.ConsentContract
import care.data4life.datadonation.consent.model.UserConsent
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

internal class UserConsentRepositoryStub : ConsentContract.Repository, MockContract.Stub {
    var whenCreateUserConsent: ((consentDocumentKey: String, version: String) -> Unit)? = null
    var whenFetchUserConsents: ((consentDocumentKey: String?) -> List<UserConsent>)? = null
    var whenRevokeUserConsent: ((consentDocumentKey: String) -> Unit)? = null

    override suspend fun createUserConsent(consentDocumentKey: String, consentDocumentVersion: String) {
        whenCreateUserConsent?.invoke(consentDocumentKey, consentDocumentVersion) ?: throw MockException()
    }

    override suspend fun fetchUserConsents(consentDocumentKey: String?): List<UserConsent> {
        return whenFetchUserConsents?.invoke(consentDocumentKey) ?: throw MockException()
    }

    override suspend fun revokeUserConsent(consentDocumentKey: String) {
        whenRevokeUserConsent?.invoke(consentDocumentKey) ?: throw MockException()
    }

    override fun clear() {
        whenCreateUserConsent = null
        whenFetchUserConsents = null
        whenRevokeUserConsent = null
    }
}
