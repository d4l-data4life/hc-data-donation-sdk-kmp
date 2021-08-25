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

import care.data4life.datadonation.donation.consentsignature.model.ConsentSignatureType
import care.data4life.datadonation.donation.consentsignature.model.ConsentSigningRequest
import care.data4life.datadonation.donation.consentsignature.model.DeletionMessage
import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.mock.fixture.ConsentSignatureFixture
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureApiServiceStub
import care.data4life.datadonation.networking.AccessToken
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentSignatureRepositoryTest {
    @Test
    fun `It fulfils ConsentSignature Repository`() {
        val repo: Any = ConsentSignatureRepository(
            ConsentSignatureApiServiceStub()
        )

        assertTrue(repo is ConsentSignatureContract.Repository)
    }

    @Test
    fun `Given enableSigning is called, it creates a ConsentSigningRequest and delegates the call to its ApiService and returns its response`() = runBlockingTest {
        // Given
        val apiService = ConsentSignatureApiServiceStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val message = "soup"

        val responds = ConsentSignatureFixture.sampleConsentSignature

        var capturedAccessToken: AccessToken? = null
        var capturedDocumentKey: String? = null
        var capturedRequest: ConsentSigningRequest? = null

        apiService.whenEnableSigning = { delegatedAccessToken, delegatedDocumentKey, delegatedSigningRequest ->
            capturedAccessToken = delegatedAccessToken
            capturedDocumentKey = delegatedDocumentKey
            capturedRequest = delegatedSigningRequest

            responds
        }

        // When
        val result = ConsentSignatureRepository(apiService).enableSigning(
            accessToken,
            consentDocumentKey,
            message
        )

        // Then
        assertSame(
            actual = result,
            expected = responds
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
            expected = ConsentSigningRequest(
                consentDocumentKey = consentDocumentKey,
                payload = message,
                signatureType = ConsentSignatureType.CONSENT_ONCE
            )
        )
    }

    @Test
    fun `Given sign is called, it creates a ConsentSigningRequest and delegates the call to its ApiService and returns its response`() = runBlockingTest {
        // Given
        val apiService = ConsentSignatureApiServiceStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val message = "soup"

        val responds = ConsentSignatureFixture.sampleConsentSignature

        var capturedAccessToken: AccessToken? = null
        var capturedDocumentKey: String? = null
        var capturedRequest: ConsentSigningRequest? = null

        apiService.whenSign = { delegatedAccessToken, delegatedDocumentKey, delegatedSigningRequest ->
            capturedAccessToken = delegatedAccessToken
            capturedDocumentKey = delegatedDocumentKey
            capturedRequest = delegatedSigningRequest

            responds
        }

        // When
        val result = ConsentSignatureRepository(apiService).sign(
            accessToken,
            consentDocumentKey,
            message
        )

        // Then
        assertSame(
            actual = result,
            expected = responds
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
            expected = ConsentSigningRequest(
                consentDocumentKey = consentDocumentKey,
                payload = message,
                signatureType = ConsentSignatureType.NORMAL_USE
            )
        )
    }

    @Test
    fun `Given disableSigning is called, it delegates the call to its ApiService and just runs`() = runBlockingTest {
        // Given
        val apiService = ConsentSignatureApiServiceStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val deletionMessage = SignedDeletionMessage(
            message = DeletionMessage(
                "soup",
                ConsentSignatureType.REVOKE_ONCE,
                "1981",
                "abc"
            ),
            signature = "super-secret"
        )

        var capturedAccessToken: AccessToken? = null
        var capturedDocumentKey: String? = null
        var capturedRequest: SignedDeletionMessage? = null

        apiService.whenDisableSigning = { delegatedAccessToken, delegatedDocumentKey, delegatedDeletionRequest ->
            capturedAccessToken = delegatedAccessToken
            capturedDocumentKey = delegatedDocumentKey
            capturedRequest = delegatedDeletionRequest
        }

        // When
        val result = ConsentSignatureRepository(apiService).disableSigning(
            accessToken,
            consentDocumentKey,
            deletionMessage
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
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
            expected = deletionMessage
        )
    }
}
