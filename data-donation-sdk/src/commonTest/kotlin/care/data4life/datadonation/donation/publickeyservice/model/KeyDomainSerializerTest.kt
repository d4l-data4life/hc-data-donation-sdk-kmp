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

package care.data4life.datadonation.donation.publickeyservice.model

import care.data4life.datadonation.donation.publickeyservice.PublicKeyServiceContract
import care.data4life.datadonation.error.CoreRuntimeError
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

internal class KeyDomainSerializerTest {
    @Test
    fun `It fulfils KSerializer`() {
        val serializer: Any = KeyDomainSerializer

        assertTrue(serializer is KSerializer<*>)
    }

    @Test
    fun `It has a proper descriptor`() {
        assertEquals(
            actual = KeyDomainSerializer.descriptor.kind,
            expected = PrimitiveKind.STRING
        )

        assertEquals(
            actual = KeyDomainSerializer.descriptor.serialName,
            expected = "KeyDomain"
        )
    }

    @Test
    fun `Given a Serializer is called with a KeyDomain, it encodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(KeyDomainSerializer)
            }
        }

        for (field in PublicKeyServiceContract.KeyDomain.values()) {
            // When
            val result = serializer.encodeToString(field)

            // Then
            assertEquals(
                actual = result,
                expected = "\"${field.domain}\""
            )
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized KeyDomain, it fails with a Internal Failure if the blur function is unknown`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(KeyDomainSerializer)
            }
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            // When
            serializer.decodeFromString<PublicKeyServiceContract.KeyDomain>("\"notJS\"")
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown KeyDomain notJS."
        )
    }

    @Test
    fun `Given a Serializer is called with a serialized KeyDomain, it decodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(KeyDomainSerializer)
            }
        }

        for (field in PublicKeyServiceContract.KeyDomain.values()) {
            // When
            val result = serializer.decodeFromString<PublicKeyServiceContract.KeyDomain>(
                "\"${field.domain}\""
            )

            // Then
            assertSame(
                actual = result,
                expected = field
            )
        }
    }
}
