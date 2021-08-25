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

import care.data4life.datadonation.DataDonationSDK
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal class EnvironmentSerializerTest {
    @Test
    fun `It fulfils KSerializer`() {
        val serializer: Any = EnvironmentSerializer

        assertTrue(serializer is KSerializer<*>)
    }

    @Test
    fun `It has a proper descriptor`() {
        assertEquals(
            actual = EnvironmentSerializer.descriptor.kind,
            expected = PrimitiveKind.STRING
        )

        assertEquals(
            actual = EnvironmentSerializer.descriptor.serialName,
            expected = "Environment"
        )
    }

    @Test
    fun `Given a Serializer is called with a Environment, it encodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(EnvironmentSerializer)
            }
        }

        for (field in DataDonationSDK.Environment.values()) {
            // When
            val result = serializer.encodeToString(field)
            println(result)
            // Then
            assertEquals(
                actual = result,
                expected = "\"${field.name.toLowerCase()}\""
            )
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized Environment, it decodes it`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                contextual(EnvironmentSerializer)
            }
        }

        for (field in DataDonationSDK.Environment.values()) {
            // When
            val result = serializer.decodeFromString<DataDonationSDK.Environment>("\"${field.name.toLowerCase()}\"")

            // Then
            assertSame(
                actual = result,
                expected = field
            )
        }
    }
}
