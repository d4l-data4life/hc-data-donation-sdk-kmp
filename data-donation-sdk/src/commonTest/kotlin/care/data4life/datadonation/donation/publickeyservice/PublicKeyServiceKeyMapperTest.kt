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

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.fixture.PublicKeyServiceFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PublicKeyServiceKeyMapperTest {
    @Test
    fun `It fulfils KeyMapper`() {
        val mapper: Any = PublicKeyServiceKeyMapper(DataDonationSDK.Environment.DEVELOPMENT)

        assertTrue(mapper is PublicKeyServiceContract.Repository.KeyMapper)
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it fails if no matching enviroment was found for 2 Keys`() {
        // Given
        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it.removeAt(0)
            }
        )

        val error = assertFailsWith<CoreRuntimeError.MissingCredentials> {
            // When
            PublicKeyServiceKeyMapper(DataDonationSDK.Environment.DEVELOPMENT).mapKeys(
                rawKeys
            )
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Malformed credential source."
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it fails if no matching DonatonServiceKey was found`() {
        // Given
        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[0] = it[0].copy(domain = PublicKeyServiceContract.KeyDomain.ALP)
            }
        )

        val error = assertFailsWith<CoreRuntimeError.MissingCredentials> {
            // When
            PublicKeyServiceKeyMapper(DataDonationSDK.Environment.DEVELOPMENT).mapKeys(
                rawKeys
            )
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Malformed credential source - No DonationServiceKey found."
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it fails if no matching ALPKey was found`() {
        // Given
        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[4] = it[4].copy(domain = PublicKeyServiceContract.KeyDomain.DonationService)
            }
        )

        val error = assertFailsWith<CoreRuntimeError.MissingCredentials> {
            // When
            PublicKeyServiceKeyMapper(DataDonationSDK.Environment.DEVELOPMENT).mapKeys(
                rawKeys
            )
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Malformed credential source - No ALPKey found."
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it maps the Keys for DEVELOPMENT`() {
        // Given
        val donationKey = "donation"
        val alpKey = "alp"

        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[0] = it[0].copy(key = donationKey)
                it[4] = it[4].copy(key = alpKey)
            }
        )

        // When
        val result = PublicKeyServiceKeyMapper(DataDonationSDK.Environment.DEVELOPMENT).mapKeys(
            rawKeys
        )

        // Then
        assertEquals(
            actual = result.donationService,
            expected = donationKey
        )
        assertEquals(
            actual = result.alp,
            expected = alpKey
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it maps the Keys for SANDBOX`() {
        // Given
        val donationKey = "donation"
        val alpKey = "alp"

        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[1] = it[1].copy(key = donationKey)
                it[5] = it[5].copy(key = alpKey)
            }
        )

        // When
        val result = PublicKeyServiceKeyMapper(DataDonationSDK.Environment.SANDBOX).mapKeys(
            rawKeys
        )

        // Then
        assertEquals(
            actual = result.donationService,
            expected = donationKey
        )
        assertEquals(
            actual = result.alp,
            expected = alpKey
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it maps the Keys for STAGING`() {
        // Given
        val donationKey = "donation"
        val alpKey = "alp"

        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[2] = it[2].copy(key = donationKey)
                it[6] = it[6].copy(key = alpKey)
            }
        )

        // When
        val result = PublicKeyServiceKeyMapper(DataDonationSDK.Environment.STAGING).mapKeys(
            rawKeys
        )

        // Then
        assertEquals(
            actual = result.donationService,
            expected = donationKey
        )
        assertEquals(
            actual = result.alp,
            expected = alpKey
        )
    }

    @Test
    fun `Given mapKeys is called with RawKeys, it maps the Keys for PRODUCTION`() {
        // Given
        val donationKey = "donation"
        val alpKey = "alp"

        val rawKeys = PublicKeyServiceFixture.sampleRawKeys.copy(
            credentials = PublicKeyServiceFixture.sampleRawKeys.credentials.toMutableList().also {
                it[3] = it[3].copy(key = donationKey)
                it[7] = it[7].copy(key = alpKey)
            }
        )

        // When
        val result = PublicKeyServiceKeyMapper(DataDonationSDK.Environment.PRODUCTION).mapKeys(
            rawKeys
        )

        // Then
        assertEquals(
            actual = result.donationService,
            expected = donationKey
        )
        assertEquals(
            actual = result.alp,
            expected = alpKey
        )
    }
}
