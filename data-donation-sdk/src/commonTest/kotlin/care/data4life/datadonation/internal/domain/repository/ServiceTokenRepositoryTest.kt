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

package care.data4life.datadonation.internal.domain.repository

import care.data4life.datadonation.mock.stub.service.DonationServiceStub
import care.data4life.sdk.util.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServiceTokenRepositoryTest {
    @Test
    fun `It fulfils ServiceTokenRepository`() {
        val repo: Any = ServiceTokenRepository(
            DonationServiceStub()
        )

        assertTrue(repo is RepositoryContract.ServiceTokenRepository)
    }

    @Test
    fun `Given requestDonationToken is called it delegates the call to its DonationService and returns its result`() = runBlockingTest {
        // Given
        val service = DonationServiceStub()
        val token = "token"

        service.whenRequestToken = { token }

        // When
        val repo = ServiceTokenRepository(service)
        val result = repo.requestDonationToken()

        // Then
        assertEquals(
            expected = token,
            actual = result
        )
    }
}