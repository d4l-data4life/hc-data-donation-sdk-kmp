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

import care.data4life.datadonation.mock.stub.storage.CredentialsDataStorageStub
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CredentialRepositoryTest {
    @Test
    fun `It fulfils CredentialsRepository`() {
        val repo: Any = CredentialsRepository(
            CredentialsDataStorageStub()
        )

        assertTrue(repo is RepositoryContract.CredentialsRepository)
    }

    @Test
    fun `Given getDataDonationPublicKey is called it delegates the call to its storage and returns its result`() {
        // Given
        val storage = CredentialsDataStorageStub()
        val key = "KEY"

        storage.whenGetDataDonationPublicKey = { key }

        // When
        val repo = CredentialsRepository(storage)
        val result = repo.getDataDonationPublicKey()

        // Then
        assertSame(
            expected = key,
            actual = result
        )
    }

    @Test
    fun `Given getAnalyticsPlatformPublicKey is called it delegates the call to its storage and returns its result`() {
        // Given
        val storage = CredentialsDataStorageStub()
        val key = "KEY"

        storage.whenGetAnalyticsPlatformPublicKey = { key }

        // When
        val repo = CredentialsRepository(storage)
        val result = repo.getAnalyticsPlatformPublicKey()

        // Then
        assertSame(
            expected = key,
            actual = result
        )
    }
}
