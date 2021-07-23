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

import care.data4life.datadonation.Contract
import care.data4life.datadonation.mock.fake.createDefaultMockClient
import care.data4life.datadonation.mock.stub.service.networking.HttpClientConfiguratorStub
import care.data4life.datadonation.mock.stub.service.networking.HttpPluginConfiguratorStub
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.util.AttributeKey
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class NetworkingKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a RequestBuilderTemplate`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module(override = true) {
                    single { createDefaultMockClient() }
                    single { Contract.Environment.DEV }
                }
            )
        }
        // Then
        val builder: Networking.RequestBuilderFactory = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains the HttpClientConfigurator`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module(override = true) {
                    single { createDefaultMockClient() }
                    single { Contract.Environment.DEV }
                }
            )
        }

        // Then
        val configuration = koin.koin.get<Networking.HttpClientConfigurator>()
        assertNotNull(configuration)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, it assembles the HttpClient`() {
        // Given
        val clientConfigurator = HttpClientConfiguratorStub()
        val features = listOf(
            Networking.HttpPluginInstaller(
                PluginStub,
                HttpPluginConfiguratorStub<Any, Any>(),
                "something"
            )
        )
        var capturedHttpConfig: HttpClientConfig<*>? = null
        var capturedPlugins: List<Networking.HttpPluginInstaller<in Any, in Any>>? = null

        clientConfigurator.whenConfigure = { delegatedHttpConfig, delegatedPlugins ->
            capturedHttpConfig = delegatedHttpConfig
            capturedPlugins = delegatedPlugins
        }

        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module(override = true) {
                    single<Networking.HttpClientConfigurator> {
                        clientConfigurator
                    }
                    single<List<Networking.HttpPluginInstaller<out Any, out Any?>>> {
                        features
                    }
                    single { Contract.Environment.DEV }
                }
            )
        }
        // Then
        val client: HttpClient = koin.koin.get()
        assertNotNull(client)

        assertTrue(capturedHttpConfig is HttpClientConfig<*>)
        assertSame(
            actual = capturedPlugins,
            expected = features
        )
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
