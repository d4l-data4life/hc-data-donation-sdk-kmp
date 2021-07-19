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

import care.data4life.sdk.log.Log
import care.data4life.sdk.log.Logger
import care.data4life.sdk.util.test.runWithContextBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
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
    fun `Given configure is called with a HttpClientConfig, which supports Features and a SerializerConfigurator, it calls it with the appropriate parameter`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val serializer = object : Networking.SerializerConfigurator {
            var capturedPluginConfig = Channel<JsonFeature.Config>()
            var capturedUtil = Channel<Networking.JsonConfigurator>()

            override fun configure(
                pluginConfig: JsonFeature.Config,
                util: Networking.JsonConfigurator
            ) {
                launch {
                    capturedPluginConfig.send(pluginConfig)
                    capturedUtil.send(util)
                }
            }
        }

        // When
        HttpClient {
            ClientConfigurator.configure(
                this,
                JsonConfigurator,
                serializer
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
    fun `Given configure is called with a HttpClientConfig, which supports Features and a LoggingConfigurator, it calls it with the appropriate parameter`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val logging = object : Networking.LoggingConfigurator {
            var capturedPluginConfig = Channel<Logging.Config>()
            var capturedUtil = Channel<Logger>()

            override fun configure(
                pluginConfig: Logging.Config,
                util: Logger
            ) {
                launch {
                    capturedPluginConfig.send(pluginConfig)
                    capturedUtil.send(util)
                }
            }
        }

        // When
        HttpClient {
            ClientConfigurator.configure(
                this,
                JsonConfigurator,
                logging
            )
        }

        // Then
        val pluginConfig: Any = logging.capturedPluginConfig.receive()
        assertTrue(pluginConfig is Logging.Config)
        assertSame(
            actual = logging.capturedUtil.receive(),
            expected = Log.logger
        )
    }
}
