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

package care.data4life.datadonation.mock.stub.donation.consentsignature

import care.data4life.datadonation.donation.consentsignature.ConsentSignatureContract
import care.data4life.datadonation.donation.consentsignature.Signature
import care.data4life.datadonation.donation.consentsignature.SignatureRequest
import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import care.data4life.datadonation.networking.AccessToken

internal class ConsentSignatureRepositoryStub : ConsentSignatureContract.Repository, MockContract.Stub {
    var whenEnableSigning: ((AccessToken, String, SignatureRequest) -> Signature)? = null
    var whenSign: ((AccessToken, String, SignatureRequest) -> Signature)? = null
    var whenDisableSigning: ((AccessToken, String, SignedDeletionMessage) -> Unit)? = null

    override suspend fun enableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        request: SignatureRequest
    ): Signature {
        return whenEnableSigning?.invoke(accessToken, consentDocumentKey, request) ?: throw MockException()
    }

    override suspend fun sign(
        accessToken: AccessToken,
        consentDocumentKey: String,
        request: SignatureRequest
    ): Signature {
        return whenSign?.invoke(accessToken, consentDocumentKey, request) ?: throw MockException()
    }

    override suspend fun disableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        message: SignedDeletionMessage
    ) {
        return whenDisableSigning?.invoke(accessToken, consentDocumentKey, message) ?: throw MockException()
    }

    override fun clear() {
        whenEnableSigning = null
        whenSign = null
        whenDisableSigning = null
    }
}
