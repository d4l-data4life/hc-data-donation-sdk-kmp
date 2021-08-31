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

package care.data4life.datadonation.donation.donationservice

import care.data4life.datadonation.donation.model.SignedConsentMessage
import care.data4life.datadonation.mock.stub.crypto.CryptoServiceStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonationControllerTest {
    @Test
    fun `It fufuls DonationServiceController`() {
        val controller: Any = DonationServiceController(
            DonationServiceRepositoryStub(),
            CryptoServiceStub(),
            Json
        )

        assertTrue(controller is DonationServiceContract.Controller)
    }

    @Test
    fun `Given fetchToken is called it delegates the call to its repository`() = runBlockingTest {
        // Given
        val token = "TOKEN"

        val repository = DonationServiceRepositoryStub()

        repository.whenFetchToken = { token }

        // When
        val result = DonationServiceController(
            repository,
            CryptoServiceStub(),
            Json
        ).fetchToken()

        // Then
        assertEquals(
            actual = result,
            expected = token
        )
    }

    @Test
    fun `Given register is called with a SignedConsent and a PublicKey of the DonationService it encrypts consent and delegates it to the repository`() = runBlockingTest {
        // Given
        val consentMessage = "potato"
        val signature = "CrazyStuff"
        val donationServiceKey = "DonationServiceKey"
        val signedConsent = SignedConsentMessage(
            consentMessageJSON = consentMessage,
            signature = signature
        )
        val encryptedPayload = "encrypted"

        val repository = DonationServiceRepositoryStub()

        var capturedEncryptedPayload: ByteArray? = null
        repository.whenRegister = { delegatedPayload ->
            capturedEncryptedPayload = delegatedPayload
        }

        val cryptor = CryptoServiceStub()

        var capturedPublicKey: String? = null
        var capturedPayload: ByteArray? = null

        cryptor.whenEncrypt = { delegatedPayload, delegatedPublicKey ->
            capturedPayload = delegatedPayload
            capturedPublicKey = delegatedPublicKey

            encryptedPayload.encodeToByteArray()
        }


        // When
        val result = DonationServiceController(
            repository,
            cryptor,
            Json
        ).register(
            signedConsent = signedConsent,
            donationServicePublicKey = donationServiceKey
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )

        assertTrue(
            capturedPayload.contentEquals(
                "{\"consentMessageJSON\":\"potato\",\"signature\":\"CrazyStuff\"}".encodeToByteArray()
            )
        )
        assertEquals(
            actual = capturedPublicKey,
            expected = donationServiceKey
        )

        assertTrue(
            capturedEncryptedPayload.contentEquals(
                encryptedPayload.encodeToByteArray()
            )
        )
    }
}
