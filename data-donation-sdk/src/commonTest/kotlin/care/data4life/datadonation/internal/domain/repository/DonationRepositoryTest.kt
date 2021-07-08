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

import care.data4life.datadonation.internal.data.model.DonationPayload
import care.data4life.datadonation.mock.stub.DonationDataStorageStub
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonationRepositoryTest {
    @Test
    fun `It fulfils DonationRepository`() {
        val repo: Any = DonationRepository(
            DonationDataStorageStub()
        )

        assertTrue(repo is RepositoryContract.DonationRepository)
    }

    @Test
    fun `Given donateResources is called with DonationPayload it delegates the call to its storage and just runs`() = runBlockingTest {
        // Given
        val storage = DonationDataStorageStub()
        val payload = DonationPayload(ByteArray(23), emptyList())

        var capturedPayload: DonationPayload? = null
        storage.whenDonateResources = { delegatedPayload ->
            capturedPayload = delegatedPayload
        }

        // When
        val repo = DonationRepository((storage))
        repo.donateResources(payload)

        // Then
        assertSame(
            expected = payload,
            actual = capturedPayload
        )
    }
}
