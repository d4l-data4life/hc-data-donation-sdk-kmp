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

import care.data4life.datadonation.donation.donationservice.model.DeletionProof
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceApiServiceStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.utils.io.core.toByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonationServiceRepositoryTest {
    @Test
    fun `It fulfils DonationServiceRepositry`() {
        val repo: Any = DonationServiceRepository(
            DonationServiceApiServiceStub()
        )

        assertTrue(repo is DonationServiceContract.Repository)
    }

    @Test
    fun `Given fetchToken is called it delegates the Call to its ApiService and returns the result`() = runBlockingTest {
        // Given
        val token = "potato"
        val apiService = DonationServiceApiServiceStub()

        var wasCalled = false
        apiService.whenFetchToken = {
            wasCalled = true
            token
        }

        // When
        val result = DonationServiceRepository(apiService).fetchToken()

        // Then
        assertEquals(
            actual = result,
            expected = token
        )
        assertTrue(wasCalled)
    }

    @Test
    fun `Given register is called with a EncryptedJSON it delegates the Call to its ApiService and returns the result`() = runBlockingTest {
        // Given
        val message = "potato".toByteArray()
        val apiService = DonationServiceApiServiceStub()

        var capturedMessage: EncryptedJSON? = null
        apiService.whenRegister = { delegatedMessage ->
            capturedMessage = delegatedMessage
        }

        // When
        val result = DonationServiceRepository(apiService).register(message)

        // Then
        assertEquals(
            actual = result,
            expected = Unit
        )
        assertSame(
            actual = capturedMessage,
            expected = message
        )
    }

    @Test
    fun `Given donate is called with MultiPartFormDataContent it delegates the Call to its ApiService and returns the result`() = runBlockingTest {
        // Given
        val donations = MultiPartFormDataContent(emptyList())
        val apiService = DonationServiceApiServiceStub()

        var capturedDonations: MultiPartFormDataContent? = null
        apiService.whenDonate = { delegatedDonations ->
            capturedDonations = delegatedDonations
        }

        // When
        val result = DonationServiceRepository(apiService).donate(donations)

        // Then
        assertEquals(
            actual = result,
            expected = Unit
        )
        assertSame(
            actual = capturedDonations,
            expected = donations
        )
    }

    @Test
    fun `Given revoke is called with MultiPartFormDataContent it delegates the Call to its ApiService and returns the result`() = runBlockingTest {
        // Given
        val donations = MultiPartFormDataContent(emptyList())
        val response = DeletionProof("", "")
        val apiService = DonationServiceApiServiceStub()

        var capturedDonations: MultiPartFormDataContent? = null
        apiService.whenRevoke = { delegatedDonations ->
            capturedDonations = delegatedDonations

            response
        }

        // When
        val result = DonationServiceRepository(apiService).revoke(donations)

        // Then
        assertSame(
            actual = result,
            expected = response
        )
        assertSame(
            actual = capturedDonations,
            expected = donations
        )
    }
}
