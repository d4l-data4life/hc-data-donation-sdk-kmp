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
import care.data4life.datadonation.internal.data.storage.StorageContract
import care.data4life.datadonation.mock.MockException

class ConsentDataStoreStub : StorageContract.UserConsentRemoteStorage {
    var whenCreateUserConsent: ((accessToken: String, consentKey: String, version: Int) -> UserConsent)? = null
    var whenFetchUserConsents: ((accessToken: String, consentKey: String?) -> List<UserConsent>)? = null
    var whenSignUserConsent: ((accessToken: String, message: String) -> String)? = null
    var whenRevokeUserConsent: ((accessToken: String, consentKey: String) -> Unit)? = null

    override suspend fun createUserConsent(accessToken: String, consentKey: String, version: Int) {
        whenCreateUserConsent?.invoke(accessToken, consentKey, version)  ?: throw MockException()
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        consentKey: String?
    ): List<UserConsent> =
        whenFetchUserConsents?.invoke(accessToken, consentKey) ?: throw MockException()

    override suspend fun signUserConsentRegistration(accessToken: String, message: String): String {
        return whenSignUserConsent?.invoke(accessToken, message) ?: throw MockException()
    }

    override suspend fun signUserConsentDonation(accessToken: String, message: String): String {
        return whenSignUserConsent?.invoke(accessToken, message) ?: throw MockException()
    }

    override suspend fun revokeUserConsent(accessToken: String, consentKey: String) {
        whenRevokeUserConsent?.invoke(accessToken, consentKey) ?: throw MockException()
    }
}
