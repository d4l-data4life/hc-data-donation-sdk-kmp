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

internal class ConsentSignatureTypeFullSerializerTest {
    @Test
    fun `It fulfils KSerializer`() {
        val serializer: Any = ConsentSignatureTypeFullSerializer

        assertTrue(serializer is KSerializer<*>)
    }

    @Test
    fun `It has a proper descriptor`() {
        assertEquals(
            actual = ConsentSignatureTypeFullSerializer.descriptor.kind,
            expected = PrimitiveKind.STRING
        )

        assertEquals(
            actual = ConsentSignatureTypeFullSerializer.descriptor.serialName,
            expected = "ConsentSignatureType"
        )
    }

    @Test
    fun `Given a Serializer is called with a ConsentSignatureType, it encodes it`() {
        // Given
        val serializer = Json

        for (field in DonationContract.ConsentSignatureType.values()) {
            // When
            val result = serializer.encodeToString(
                ConsentSignatureTypeFullSerializer,
                field
            )

            // Then
            assertEquals(
                actual = result,
                expected = "\"${field.value}\""
            )
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized ConsentSignatureType, it fails with a Internal Failure if the blur function is unknown`() {
        // Given
        val serializer = Json

        // Then
        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            // When
            serializer.decodeFromString(
                ConsentSignatureTypeFullSerializer,
                "\"notJS\""
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown ConsentSignatureType notJS."
        )
    }

    @Test
    fun `Given a Serializer is called with a serialized ConsentSignatureType, it decodes it`() {
        // Given
        val serializer = Json

        for (field in DonationContract.ConsentSignatureType.values()) {
            // When
            val result = serializer.decodeFromString(
                ConsentSignatureTypeFullSerializer,
                "\"${field.value}\""
            )

            // Then
            assertSame(
                actual = result,
                expected = field
            )
        }
    }
}
