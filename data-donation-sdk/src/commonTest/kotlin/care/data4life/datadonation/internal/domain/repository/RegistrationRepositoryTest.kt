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

import care.data4life.datadonation.mock.stub.RegistrationDataStorageStub
import care.data4life.sdk.util.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RegistrationRepositoryTest {
    @Test
    fun `It fulfils RegistrationRepository`() {
        val repo: Any = RegistrationRepository(
            RegistrationDataStorageStub()
        )

        assertTrue(repo is RepositoryContract.RegistrationRepository)
    }

    @Test
    fun `Given registerNewDonor is called with Data delegates it delegates the call to its storage and just runs`() = runBlockingTest {
        // Given
        val storage = RegistrationDataStorageStub()
        val data = ByteArray(23)

        var capturedData: ByteArray? = null
        storage.whenRegisterNewDonor = { delegatedData ->
            capturedData = delegatedData
        }

        // When
        val repo = RegistrationRepository(storage)
        val result = repo.registerNewDonor(data)

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertSame(
            expected = data,
            actual = capturedData
        )
    }
}
