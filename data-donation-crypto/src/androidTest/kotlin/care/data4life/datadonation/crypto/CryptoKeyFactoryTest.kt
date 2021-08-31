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

package care.data4life.datadonation.crypto

import care.data4life.datadonation.crypto.mock.ResourceLoader
import care.data4life.sdk.crypto.ExchangeKey
import care.data4life.sdk.crypto.KeyType
import care.data4life.sdk.crypto.KeyVersion
import care.data4life.sdk.util.Base64
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class CryptoKeyFactoryTest {
    @Test
    fun `It fulfils the CryptoKeyFactoryContract`() {
        val factory: Any = CryptoKeyFactory

        assertTrue(factory is CryptoKeyFactoryContract)
    }

    @Test
    fun `Given createPublicKey is called with a ExchangeKey, it creates a Asymmetric KeyPair, which contains the given PublicKey`() {
        // Given
        val key = ResourceLoader.loader.load("/fixture/crypto/DonationServicePublicKey.txt")
        val exchangeKey = ExchangeKey(
            type = KeyType.APP_PUBLIC_KEY,
            privateKey = null,
            publicKey = key,
            symmetricKey = null,
            version = KeyVersion.VERSION_1
        )

        // When
        val keyPair = CryptoKeyFactory.createPublicKey(exchangeKey)

        // Then
        assertEquals(
            actual = keyPair.algorithm.transformation,
            expected = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
        )
        assertEquals(
            actual = keyPair.keyVersion,
            expected = KeyVersion.VERSION_1.value
        )
        assertTrue(
            keyPair.publicKey!!.value.encoded.contentEquals(
                Base64.decode(key)
            )
        )
    }

    @Test
    fun `Given createPrivateKey is called with a ExchangeKey, it creates a Asymmetric KeyPair, which contains the given PublicKey`() {
        // Given
        val key = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")
        val exchangeKey = ExchangeKey(
            type = KeyType.APP_PRIVATE_KEY,
            privateKey = key,
            publicKey = null,
            symmetricKey = null,
            version = KeyVersion.VERSION_1
        )

        // When
        val keyPair = CryptoKeyFactory.createPrivateKey(exchangeKey)

        // Then
        assertEquals(
            actual = keyPair.algorithm.transformation,
            expected = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
        )
        assertEquals(
            actual = keyPair.keyVersion,
            expected = KeyVersion.VERSION_1.value
        )
        assertTrue(
            keyPair.privateKey!!.value.encoded.contentEquals(
                Base64.decode(key)
            )
        )
    }

    @Test
    fun `Given generateSymmetricKey is called, it creates a GCKey`() {
        // When
        val key = CryptoKeyFactory.generateSymmetricKey()

        // Then
        assertEquals(
            actual = key.algorithm.transformation,
            expected = "AES/GCM/NoPadding"
        )
        assertEquals(
            actual = key.keyVersion,
            expected = KeyVersion.VERSION_1.symmetricKeySize
        )
    }
}
