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

package care.data4life.datadonation.core.model

import care.data4life.datadonation.internal.data.model.DocumentWithSignature
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KeyPairTest {
    @Test
    fun `It fulfils KeyPair`() {
        val pair: Any = KeyPair(ByteArray(23), ByteArray(42))

        assertTrue(pair is ModelContract.KeyPair)
    }

    @Test
    fun `Given equals is called with a non KeyPair, it returns false`() {
        // Given
        val payload = KeyPair(
            ByteArray(23),
            ByteArray(42),
        )

        // When
        val result = payload.equals("23")

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a KeyPair, it returns false, if the document do not match`() {
        // Given
        val pair1 = KeyPair(
            ByteArray(23),
            ByteArray(23),
        )
        val pair2 = KeyPair(
            ByteArray(42),
            ByteArray(23),
        )

        // When
        val result = pair1.equals(pair2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a DonationPayload, it returns false, if the signature do not match`() {
        // Given
        val pair1 = DocumentWithSignature(
            ByteArray(23),
            ByteArray(23),
        )
        val pair2 = DocumentWithSignature(
            ByteArray(23),
            ByteArray(42),
        )

        // When
        val result = pair1.equals(pair2)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a DonationPayload, it returns true, if the documents and signature do match`() {
        // Given
        val pair1 = DocumentWithSignature(
            ByteArray(23),
            ByteArray(42),
        )
        val pair2 = DocumentWithSignature(
            ByteArray(23),
            ByteArray(42),
        )

        // When
        val result = pair1.equals(pair2)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given hashCode is called it returns a stable hash code`() {
        // Given
        val public = ByteArray(23)
        val private = ByteArray(42)

        val signedDocument = KeyPair(
            public,
            private
        )

        // When
        val result = signedDocument.hashCode()

        // Then
        assertEquals(
            actual = result,
            expected = 31 * public.contentHashCode() + private.contentHashCode()
        )
    }
}
