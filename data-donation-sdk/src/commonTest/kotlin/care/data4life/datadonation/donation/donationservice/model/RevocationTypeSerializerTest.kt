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

package care.data4life.datadonation.donation.donationservice.model

import care.data4life.datadonation.donation.donationservice.DonationServiceContract
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class RevocationTypeSerializerTest {
    @Test
    fun `It fulfils KSerializer`() {
        val serializer: Any = RevocationTypeSerializer

        assertTrue(serializer is KSerializer<*>)
    }

    @Test
    fun `It has a proper descriptor`() {
        assertEquals(
            actual = RevocationTypeSerializer.descriptor.kind,
            expected = PrimitiveKind.STRING
        )

        assertEquals(
            actual = RevocationTypeSerializer.descriptor.serialName,
            expected = "RevocationType"
        )
    }

    @Test
    fun `Given a Serializer is called with a RevocationType, it encodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(RevocationTypeSerializer)
            }
        }

        for (field in DonationServiceContract.RevocationType.values()) {
            // When
            val result = serializer.encodeToString(field)

            // Then
            assertEquals(
                actual = result,
                expected = "\"${field.value}\""
            )
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized RevocationType, it fails with a Internal Failure if the blur function is unknown`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(RevocationTypeSerializer)
            }
        }

        // Then
        assertFailsWith<Throwable> {
            // When
            serializer.decodeFromString<DonationServiceContract.RevocationType>("\"notJS\"")
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized RevocationType, it decodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(RevocationTypeSerializer)
            }
        }

        for (field in DonationServiceContract.RevocationType.values()) {
            // When
            val result = serializer.decodeFromString<DonationServiceContract.RevocationType>("\"${field.value}\"")

            // Then
            assertSame(
                actual = result,
                expected = field
            )
        }
    }
}
