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

package care.data4life.datadonation.internal.data.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DonationPayloadTest {
    @Test
    fun `Given equals is called with a non DonationPayload, it returns false`() {
        // Given
        val payload = DonationPayload(
            ByteArray(23),
            emptyList()
        )

        // When
        val result = payload.equals("23")

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a DonationPayload, it returns false, if the requests do not match`() {
        // Given
        val payload = DonationPayload(
            ByteArray(23),
            emptyList()
        )
        val payload2 = DonationPayload(
            ByteArray(36),
            emptyList()
        )

        // When
        val result = payload == payload2

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a DonationPayload, it returns false, if the documents do not match`() {
        // Given
        val payload = DonationPayload(
            ByteArray(23),
            emptyList()
        )
        val payload2 = DonationPayload(
            ByteArray(23),
            listOf(
                DocumentWithSignature(
                    ByteArray(23),
                    ByteArray(23),
                )
            )
        )

        // When
        val result = payload == payload2

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given equals is called with a DonationPayload, it returns true, if the documents and request do match`() {
        // Given
        val payload = DonationPayload(
            ByteArray(23),
            listOf(
                DocumentWithSignature(
                    ByteArray(23),
                    ByteArray(23),
                )
            )
        )
        val payload2 = DonationPayload(
            ByteArray(23),
            listOf(
                DocumentWithSignature(
                    ByteArray(23),
                    ByteArray(23),
                )
            )
        )

        // When
        val result = payload == payload2

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given hashCode is called it returns a stable hash code`() {
        // Given
        val request = ByteArray(23)
        val documents = listOf(
            DocumentWithSignature(
                ByteArray(23),
                ByteArray(23),
            )
        )

        val payload = DonationPayload(
            request,
            documents
        )

        // When
        val result = payload.hashCode()

        // Then
        assertEquals(
            actual = result,
            expected = 31 * request.contentHashCode() + documents.hashCode()
        )
    }
}
