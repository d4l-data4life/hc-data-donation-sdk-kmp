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

package care.data4life.datadonation.internal.data.service.networking

import care.data4life.datadonation.mock.stub.service.networking.JsonConfiguratorStub
import io.ktor.client.features.json.JsonFeature
import kotlinx.serialization.json.JsonBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpSerializerConfiguratorTest {
    @Test
    fun `It fulfils HttpSerializerConfigurator`() {
        val configurator: Any = HttpSerializerConfigurator

        assertTrue(configurator is Networking.HttpFeatureConfigurator<*, *>)
    }

    @Test
    fun `Given configure is called with a JsonFeatureConfig it just runs while configuring the serializer`() {
        // Given
        val pluginConfig = JsonFeature.Config()
        val jsonConfigurator = JsonConfiguratorStub()

        var capturedBuilder: JsonBuilder? = null
        jsonConfigurator.whenConfigure = { delegatedBuilder ->
            capturedBuilder = delegatedBuilder
            delegatedBuilder
        }

        // When
        val result = HttpSerializerConfigurator.configure(pluginConfig, jsonConfigurator)

        // Then
        assertEquals(
            actual = result,
            expected = Unit
        )
        assertTrue(capturedBuilder is JsonBuilder)
    }
}
