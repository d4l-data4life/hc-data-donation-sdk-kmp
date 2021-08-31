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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.donation.model.SignedConsentMessage
import care.data4life.datadonation.mock.stub.crypto.CryptoServiceStub
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureRepositoryStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.datadonation.networking.AccessToken
import care.data4life.sdk.util.Base64
import care.data4life.sdk.util.test.annotation.RobolectricTestRunner
import care.data4life.sdk.util.test.annotation.RunWithRobolectricTestRunner
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class ConsentSignatureControllerTest {
    @Test
    fun `It fulfils ConsentSignatureController`() {
        val controller: Any = ConsentSignatureController(
            ConsentSignatureRepositoryStub(),
            UserSessionTokenRepositoryStub(),
            CryptoServiceStub(),
            Json
        )

        assertTrue(controller is ConsentSignatureContract.Controller)
    }

    @Test
    fun `Given enableSigning is called with its parameter, it retrieves the AccessToken, builds the SignatureMessage encrypts it and delegates the call to its repository, whle wrapping the result as a SignedConsentMessage`() = runBlockingTest {
        // Given
        val token = "Tomato"
        val accessToken = "Potato"
        val consentDocumentKey = "Tofu"
        val donorPublicKey = "Broccoli"
        val donationServicePublicKey = "Carrot"

        val encryptedPayload = "Onion"

        val signature = "Soup"

        val session = UserSessionTokenRepositoryStub()
        session.whenSessionToken = { accessToken }

        val cryptor = CryptoServiceStub()

        var capturedPublicKey: String? = null
        var capturedPayload: ByteArray? = null

        cryptor.whenEncrypt = { delegatedPayload, delegatedPublicKey ->
            capturedPayload = delegatedPayload
            capturedPublicKey = delegatedPublicKey

            encryptedPayload.encodeToByteArray()
        }

        val repository = ConsentSignatureRepositoryStub()

        var capturedAccessToken: AccessToken? = null
        var capturedDocumentKey: String? = null
        var capturedRequest: SignatureRequest? = null
        repository.whenEnableSigning = { delegatedAccessToken, delegatedConsentDocumentKey, delegatedMessage ->
            capturedAccessToken = delegatedAccessToken
            capturedDocumentKey = delegatedConsentDocumentKey
            capturedRequest = delegatedMessage

            signature
        }

        // When
        val result = ConsentSignatureController(
            repository,
            session,
            cryptor,
            Json
        ).enableSigning(
            token,
            consentDocumentKey,
            donorPublicKey,
            donationServicePublicKey
        )

        // Then
        assertEquals(
            actual = result,
            expected = SignedConsentMessage(
                consentMessageJSON = "{\"consentDocumentKey\":\"$consentDocumentKey\",\"payload\":\"${Base64.encodeToString(encryptedPayload)}\",\"signatureType\":\"consentOnce\"}",
                signature = signature
            )
        )

        assertEquals(
            actual = capturedPublicKey,
            expected = donationServicePublicKey
        )
        assertTrue(
            capturedPayload.contentEquals(
                "{\"token\":\"$token\",\"donorID\":\"$donorPublicKey\"}".encodeToByteArray()
            ),

        )

        assertEquals(
            actual = capturedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = capturedDocumentKey,
            expected = consentDocumentKey
        )
        assertEquals(
            actual = capturedRequest,
            expected = "{\"consentDocumentKey\":\"$consentDocumentKey\",\"payload\":\"${Base64.encodeToString(encryptedPayload)}\",\"signatureType\":\"consentOnce\"}"
        )
    }
}
