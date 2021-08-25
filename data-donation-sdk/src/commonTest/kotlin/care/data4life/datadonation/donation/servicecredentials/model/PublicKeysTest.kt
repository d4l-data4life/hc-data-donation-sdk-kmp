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

package care.data4life.datadonation.donation.servicecredentials.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PublicKeysTest {
    @Test
    fun `Given equals is called with null it returns false`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))

        // When
        val result = key1.equals(null)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with an arbitrary object it returns false`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))

        // When
        val result = key1.equals(object {})

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with other PublicKeys object it returns false, if the DonationKey does not match`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))
        val key2 = PublicKeys(ByteArray(1), ByteArray(1))

        // When
        val result = key1.equals(key2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with other PublicKeys object it returns false, if the ALPKey does not match`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))
        val key2 = PublicKeys(ByteArray(0), ByteArray(1))

        // When
        val result = key1.equals(key2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with other PublicKeys object it returns true, if both keys match`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))
        val key2 = PublicKeys(ByteArray(0), ByteArray(0))

        // When
        val result = key1.equals(key2)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given hashcode is called it creates a hashcode`() {
        // Given
        val key1 = PublicKeys(ByteArray(0), ByteArray(0))

        // When
        val result = key1.hashCode()

        // Then
        assertEquals(
            actual = result,
            expected = 32
        )
    }
}
