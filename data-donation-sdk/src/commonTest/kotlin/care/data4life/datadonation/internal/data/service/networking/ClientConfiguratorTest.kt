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

import care.data4life.sdk.util.test.runWithContextBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClientConfiguratorTest {
    @Test
    fun `It fulfils ClientConfigurator`() {
        val configurator: Any = ClientConfigurator

        assertTrue(configurator is Networking.ClientConfigurator)
    }

    @Test
    fun `Given configure is called with a HttpClientConfig, which supports Features, and a Configurator and its AuxiliaryConfigurator, it calls the Configurator with its appropriate parameter`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val serializer = object : Networking.SerializerConfigurator {
            var capturedPluginConfig = Channel<JsonFeature.Config>()
            var capturedUtil = Channel<Networking.JsonConfigurator>()

            override fun configure(
                pluginConfig: JsonFeature.Config,
                auxiliaryConfigurator: Networking.JsonConfigurator
            ) {
                launch {
                    capturedPluginConfig.send(pluginConfig)
                    capturedUtil.send(auxiliaryConfigurator)
                }
            }
        }

        // When
        HttpClient {
            ClientConfigurator.configure(
                this,
                mapOf(
                    JsonFeature to Pair(
                        serializer as Networking.Configurator<Any, Any>,
                        JsonConfigurator
                    )
                )
            )
        }

        // Then
        val pluginConfig: Any = serializer.capturedPluginConfig.receive()
        assertTrue(pluginConfig is JsonFeature.Config)
        assertSame(
            actual = serializer.capturedUtil.receive(),
            expected = JsonConfigurator
        )
    }

    @Test
    fun `Test`() {

    }
}
