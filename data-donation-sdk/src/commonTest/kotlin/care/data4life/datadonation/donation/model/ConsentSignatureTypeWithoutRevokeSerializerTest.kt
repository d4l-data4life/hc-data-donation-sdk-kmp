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

package care.data4life.datadonation.donation.model

import care.data4life.datadonation.donation.DonationContract
import care.data4life.datadonation.error.CoreRuntimeError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class ConsentSignatureTypeWithoutRevokeSerializerTest {
    @Test
    fun `It fulfils KSerializer`() {
        val serializer: Any = ConsentSignatureTypeWithoutRevokeSerializer

        assertTrue(serializer is KSerializer<*>)
    }

    @Test
    fun `It has a proper descriptor`() {
        assertEquals(
            actual = ConsentSignatureTypeWithoutRevokeSerializer.descriptor.kind,
            expected = PrimitiveKind.STRING
        )

        assertEquals(
            actual = ConsentSignatureTypeWithoutRevokeSerializer.descriptor.serialName,
            expected = "ConsentSignatureType"
        )
    }

    @Test
    fun `Given a Serializer is called with CONSENT_ONCE or NORMAL_USE, it encodes it`() {
        // Given
        val serializer = Json

        // When
        val result1 = serializer.encodeToString(
            ConsentSignatureTypeWithoutRevokeSerializer,
            DonationContract.ConsentSignatureType.CONSENT_ONCE
        )
        val result2 = serializer.encodeToString(
            ConsentSignatureTypeWithoutRevokeSerializer,
            DonationContract.ConsentSignatureType.NORMAL_USE
        )

        // Then
        assertEquals(
            actual = result1,
            expected = "\"consentOnce\""
        )

        assertEquals(
            actual = result2,
            expected = "\"normalUse\""
        )
    }

    @Test
    fun `Given a Serializer is called with REVOKE_ONCE, it fails`() {
        // Given
        val serializer = Json

        // Then
        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            // When
            serializer.encodeToString(
                ConsentSignatureTypeWithoutRevokeSerializer,
                DonationContract.ConsentSignatureType.REVOKE_ONCE
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown ConsentSignatureType revokeOnce."
        )
    }

    @Test
    fun `Given a Serializer is called with a serialized ConsentSignatureType while encoding, it fails with a Internal Failure if the blur function is unknown`() {
        // Given
        val serializer = Json

        // Then
        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            // When
            serializer.decodeFromString(
                ConsentSignatureTypeWithoutRevokeSerializer,
                "\"notJS\""
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown ConsentSignatureType notJS."
        )
    }

    @Test
    fun `Given a Serializer is called with serialized CONSENT_ONCE or serialized NORMAL_USE, it decodes it`() {
        // Given
        val serializer = Json {
            isLenient = true
        }

        // When
        val result1 = serializer.decodeFromString(
            ConsentSignatureTypeWithoutRevokeSerializer,
            DonationContract.ConsentSignatureType.CONSENT_ONCE.value
        )
        val result2 = serializer.decodeFromString(
            ConsentSignatureTypeWithoutRevokeSerializer,
            DonationContract.ConsentSignatureType.NORMAL_USE.value
        )

        // Then
        assertSame(
            actual = result1,
            expected = DonationContract.ConsentSignatureType.CONSENT_ONCE
        )

        assertEquals(
            actual = result2,
            expected = DonationContract.ConsentSignatureType.NORMAL_USE
        )
    }

    @Test
    fun `Given a Serializer is called with serialized REVOKE_ONCE while decoding, it fails`() {
        // Given
        val serializer = Json

        // Then
        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            // When
            serializer.encodeToString(
                ConsentSignatureTypeWithoutRevokeSerializer,
                DonationContract.ConsentSignatureType.REVOKE_ONCE
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown ConsentSignatureType revokeOnce."
        )
    }
}
