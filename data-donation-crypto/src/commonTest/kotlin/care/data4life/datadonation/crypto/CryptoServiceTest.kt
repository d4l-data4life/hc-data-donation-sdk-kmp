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
import care.data4life.datadonation.crypto.util.AndroidOnly
import care.data4life.datadonation.crypto.util.CryptoVerification
import care.data4life.sdk.util.test.annotation.RobolectricTestRunner
import care.data4life.sdk.util.test.annotation.RunWithRobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@AndroidOnly
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class CryptoServiceTest {

    @Test
    fun `It fulfils the Crypto Service`() {
        val service: Any = CryptoService()

        assertTrue(service is CryptoServiceContract)
    }

    @Test
    fun `Given encrypt is called with a Payload and a PublicKey it encrypts the given Data`() {
        // Given
        val expected = "{\"a\":\"Hello World!\"}".encodeToByteArray()
        val publicKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePublicKey.txt")
        val privateKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // When
        val encrypted = CryptoService().encrypt(
            expected,
            publicKey
        )
        val decrypted = CryptoVerification.decrypt(
            encrypted,
            privateKey
        )

        // Then
        assertFalse(
            encrypted.contentEquals(expected)
        )
        assertTrue(
            decrypted.contentEquals(expected)
        )
    }

    @Test
    fun `Given sign is called with a Payload, a PrivateKey and arbitrary Salt, it fails`() {
        // Given
        val data = "{\"a\":\"Hello World!\"}".encodeToByteArray()
        val publicKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePublicKey.txt")
        val privateKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // Then
        val error = assertFailsWith<CryptoError.UnknownSalt> {
            CryptoService().sign(
                data,
                privateKey,
                23
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown salt length 23."
        )
    }

    @Test
    fun `Given sign is called with a Payload, a PrivateKey and 0 as Salt, it signs the given Data`() {
        // Given
        val data = "{\"a\":\"Hello World!\"}".encodeToByteArray()
        val publicKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePublicKey.txt")
        val privateKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // When
        val signature = CryptoService().sign(
            data,
            privateKey,
            0
        )
        val isValid = CryptoVerification.verify(
            data,
            signature,
            publicKey,
            0
        )

        // Then
        assertTrue(isValid)
    }

    @Test
    fun `Given sign is called with a Payload, a PrivateKey and 32 as Salt, it signs the given Data`() {
        // Given
        val data = "{\"a\":\"Hello World!\"}".encodeToByteArray()
        val publicKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePublicKey.txt")
        val privateKey = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // When
        val signature = CryptoService().sign(
            data,
            privateKey,
            32
        )
        val isValid = CryptoVerification.verify(
            data,
            signature,
            publicKey,
            32
        )

        // Then
        assertTrue(isValid)
    }
}
