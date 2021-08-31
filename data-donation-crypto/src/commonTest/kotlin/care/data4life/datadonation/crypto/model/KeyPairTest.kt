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

package care.data4life.datadonation.crypto.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KeyPairTest {
    @Test
    fun `It has a stable hash`() {
        // Given
        val keyPair = KeyPair(
            ByteArray(23),
            ByteArray(42)
        )

        // When
        val hash = keyPair.hashCode()

        // Then
        assertEquals(
            actual = 1769061314,
            expected = hash
        )
    }

    @Test
    fun `Given a KeyPair is compared to a Thing, which is not a KeyPair it return false`() {
        // Given
        val keyPair = KeyPair(
            ByteArray(23),
            ByteArray(42)
        )
        val thing = "potato"

        // When
        val result = keyPair.equals(thing)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given a KeyPair is compared another KeyPair, which has a different PrivateKey and PublicKey it return false`() {
        // Given
        val keyPair1 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(42)
        )
        val keyPair2 = KeyPair(
            privateKey = ByteArray(1),
            publicKey = ByteArray(2)
        )

        // When
        val result = keyPair1 == (keyPair2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given a KeyPair is compared another KeyPair, which has a different PublicKey it return false`() {
        // Given
        val keyPair1 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(42)
        )
        val keyPair2 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(41)
        )

        // When
        val result = keyPair1 == (keyPair2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given a KeyPair is compared another KeyPair, which has a different PrivateKey it return false`() {
        // Given
        val keyPair1 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(42)
        )
        val keyPair2 = KeyPair(
            privateKey = ByteArray(29),
            publicKey = ByteArray(42)
        )

        // When
        val result = keyPair1 == (keyPair2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given a KeyPair is compared another KeyPair, which has matching Keys it return true`() {
        // Given
        val keyPair1 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(42)
        )
        val keyPair2 = KeyPair(
            privateKey = ByteArray(23),
            publicKey = ByteArray(42)
        )

        // When
        val result = keyPair1 == (keyPair2)

        // Then
        assertTrue(result)
    }
}
