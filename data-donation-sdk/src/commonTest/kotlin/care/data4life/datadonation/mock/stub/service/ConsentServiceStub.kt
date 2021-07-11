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

package care.data4life.datadonation.mock.stub.service

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class ConsentServiceStub : ServiceContract.ConsentService, MockContract.Stub {
    var whenCreateUserConsent: ((String, String, Int) -> Unit)? = null
    var whenFetchConsentDocuments: ((String, Int?, String?, String) -> List<ConsentDocument>)? = null
    var whenFetchUserConsents:((String, Boolean?, String?) -> List<UserConsent>)? = null
    var whenRequestSignatureConsentRegistration: ((String, String) -> ConsentSignature)? = null
    var whenRequestSignatureDonation: ((String, String) -> ConsentSignature)? = null
    var whenRevokeUserConsent: ((String, String) -> Unit)? = null

    override suspend fun createUserConsent(
        accessToken: String,
        consentKey: String,
        version: Int
    ) {
        return whenCreateUserConsent?.invoke(
            accessToken,
            consentKey,
            version
        ) ?: throw MockException()
    }

    override suspend fun fetchConsentDocuments(
        accessToken: String,
        version: Int?,
        language: String?,
        consentKey: String
    ): List<ConsentDocument> {
        return whenFetchConsentDocuments?.invoke(
            accessToken,
            version,
            language,
            consentKey
        ) ?: throw MockException()
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        latestConsent: Boolean?,
        consentKey: String?
    ): List<UserConsent> {
        return whenFetchUserConsents?.invoke(
            accessToken,
            latestConsent,
            consentKey
        ) ?: throw MockException()
    }

    override suspend fun requestSignatureConsentRegistration(
        accessToken: String,
        message: String
    ): ConsentSignature {
        return whenRequestSignatureConsentRegistration?.invoke(accessToken, message) ?: throw MockException()
    }

    override suspend fun requestSignatureDonation(
        accessToken: String,
        message: String
    ): ConsentSignature {
        return whenRequestSignatureDonation?.invoke(accessToken, message) ?: throw MockException()
    }

    override suspend fun revokeUserConsent(accessToken: String, consentKey: String) {
        return whenRevokeUserConsent?.invoke(accessToken, consentKey) ?: throw MockException()
    }

    override fun clear() {
        whenCreateUserConsent = null
        whenFetchConsentDocuments = null
        whenFetchUserConsents = null
        whenRequestSignatureConsentRegistration = null
        whenRequestSignatureDonation = null
        whenRevokeUserConsent = null
    }
}
