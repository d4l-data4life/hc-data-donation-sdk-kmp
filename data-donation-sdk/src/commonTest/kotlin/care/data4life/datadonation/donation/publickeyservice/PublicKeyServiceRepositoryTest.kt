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

package care.data4life.datadonation.donation.publickeyservice

import care.data4life.datadonation.donation.publickeyservice.model.PublicKeys
import care.data4life.datadonation.donation.publickeyservice.model.RawKeys
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceApiServiceStub
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceKeyMapperStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class PublicKeyServiceRepositoryTest {
    @Test
    fun `It fulfils PublicKeyServiceRepository`() {
        val repo: Any = PublicKeyServiceRepository(
            PublicKeyServiceApiServiceStub(),
            PublicKeyServiceKeyMapperStub()
        )

        assertTrue(repo is PublicKeyServiceContract.Repository)
    }

    @Test
    fun `Given fetchPublicKeys is called it retrieves the Keys from the API and maps the response with the KeyMapper and returns the result`() = runBlockingTest {
        // Given
        val rawKeys = RawKeys(credentials = emptyList())
        val publicKeys = PublicKeys("donation", "alp")

        val apiService = PublicKeyServiceApiServiceStub()
        val keyMapper = PublicKeyServiceKeyMapperStub()

        apiService.whenFetchPublicKeys = { rawKeys }

        var capturedRawKeys: RawKeys? = null
        keyMapper.whenMapKeys = { delegatedRawKeys ->
            capturedRawKeys = delegatedRawKeys

            publicKeys
        }

        // When
        val result = PublicKeyServiceRepository(apiService, keyMapper)
            .fetchPublicKeys()

        // Then
        assertSame(
            actual = result,
            expected = publicKeys
        )

        assertSame(
            actual = capturedRawKeys,
            expected = rawKeys
        )
    }
}
