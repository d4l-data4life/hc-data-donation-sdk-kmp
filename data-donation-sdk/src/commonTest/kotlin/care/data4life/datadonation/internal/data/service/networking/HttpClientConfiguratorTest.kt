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

import care.data4life.datadonation.mock.stub.service.networking.HttpPluginConfiguratorStub
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import care.data4life.sdk.util.test.ktor.createHelloWorldOkResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.features.HttpClientFeature
import io.ktor.util.AttributeKey
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpClientConfiguratorTest {
    @BeforeTest
    fun setUp() {
        Counter.amount = 0
    }

    @Test
    fun `It fulfils HttpClientConfigurator`() {
        val configurator: Any = HttpClientConfigurator

        assertTrue(configurator is Networking.HttpClientConfigurator)
    }

    @Test
    fun `Given configure is called with a HttpClientConfig and a List of HttpFeatureInstaller it installs a given Plugin`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val capturedPluginConfig = Channel<Any>()
        val capturedSubConfig = Channel<Any>()

        val subConfig = "something"
        val stubPluginConfigurator = HttpPluginConfiguratorStub<Any, Any?>()

        stubPluginConfigurator.whenConfigure = { pluginConfig, subConfiguration ->
            launch {
                capturedPluginConfig.send(pluginConfig)
                capturedSubConfig.send(subConfiguration!!)
            }
        }

        val features = listOf(
            Networking.HttpPluginInstaller(
                PluginStub,
                stubPluginConfigurator,
                subConfig,
            )
        )

        // When
        HttpClient(MockEngine) {
            HttpClientConfigurator.configure(
                this,
                features
            )

            engine {
                addHandler {
                    createHelloWorldOkResponse(this)
                }
            }
        }

        // Then
        assertTrue(capturedPluginConfig.receive() is PluginStub.Config)
        assertEquals(
            actual = capturedSubConfig.receive(),
            expected = subConfig
        )
    }

    @Test
    fun `Given configure is called with a HttpClientConfig and a List of HttpFeatureInstaller it installs a arbitrary number of Plugins`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val subConfig = object {}
        val stubPluginConfigurator = HttpPluginConfiguratorStub<Any, Any?>()

        stubPluginConfigurator.whenConfigure = { _, _ ->
            Counter.amount++
        }

        val features = listOf(
            Networking.HttpPluginInstaller(
                PluginStub,
                stubPluginConfigurator,
                subConfig,
            ),
            Networking.HttpPluginInstaller(
                PluginStub,
                stubPluginConfigurator,
                subConfig,
            ),
            Networking.HttpPluginInstaller(
                PluginStub,
                stubPluginConfigurator,
                subConfig,
            )
        )

        // When
        HttpClient(MockEngine) {
            HttpClientConfigurator.configure(
                this,
                features
            )

            engine {
                addHandler {
                    createHelloWorldOkResponse(this)
                }
            }
        }

        // Then
        assertEquals(
            actual = Counter.amount,
            expected = features.size
        )
    }

    @ThreadLocal
    private object Counter {
        var amount = 0
    }

    private class PluginStub {
        class Config

        companion object Feature : HttpClientFeature<Config, PluginStub> {
            override val key: AttributeKey<PluginStub> = AttributeKey("PluginStub")

            override fun install(feature: PluginStub, scope: HttpClient) = Unit

            override fun prepare(block: Config.() -> Unit): PluginStub {
                Config().apply(block)
                return PluginStub()
            }
        }
    }
}
