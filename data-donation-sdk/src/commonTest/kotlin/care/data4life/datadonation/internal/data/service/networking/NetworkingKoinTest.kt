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

import care.data4life.datadonation.mock.stub.service.networking.HttpClientConfiguratorStub
import care.data4life.datadonation.mock.stub.service.networking.HttpFeatureConfiguratorStub
import care.data4life.sdk.log.Log
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.util.AttributeKey
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class NetworkingKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a CallBuilderFactory`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking()
            )
        }
        // Then
        val builder: Networking.RequestBuilderTemplateFactory = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a HttpClient`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking()
            )
        }
        // Then
        val client: HttpClient = koin.koin.get()
        assertNotNull(client)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a List of HttpFeatureInstaller`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking()
            )
        }

        val configuration = koin.koin.get<List<Networking.HttpFeatureInstaller<in Any, in Any?>>>()

        // Then
        assertEquals(
            actual = configuration.size,
            expected = 2
        )
        assertEquals(
            actual = configuration[0],
            expected = Networking.HttpFeatureInstaller(
                JsonFeature,
                HttpSerializerConfigurator,
                JsonConfigurator
            )
        )
        assertEquals(
            actual = configuration[1],
            expected = Networking.HttpFeatureInstaller(
                Logging,
                HttpLoggingConfigurator,
                Log.logger
            )
        )
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains the HttpClientConfigurator`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking()
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
            Networking.HttpFeatureInstaller(
                FeatureStub,
                HttpFeatureConfiguratorStub<Any, Any>(),
                "something"
            )
        )

        var capturedHttpConfig: HttpClientConfig<*>? = null
        var capturedFeatures: List<Networking.HttpFeatureInstaller<in Any, in Any>>? = null

        clientConfigurator.whenConfigure = { delegatedHttpConfig, delegatedFeatures ->
            capturedHttpConfig = delegatedHttpConfig
            capturedFeatures = delegatedFeatures
        }

        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module(override = true) {
                    factory<Networking.HttpClientConfigurator> {
                        clientConfigurator
                    }
                    factory<List<Networking.HttpFeatureInstaller<out Any, out Any?>>> {
                        features
                    }
                }
            )
        }
        // Then
        koin.koin.get<HttpClient>()
        assertTrue(capturedHttpConfig is HttpClientConfig<*>)
        assertSame(
            actual = capturedFeatures,
            expected = features
        )
    }

    private class FeatureStub {
        class Config

        companion object Feature : HttpClientFeature<Config, FeatureStub> {
            override val key: AttributeKey<FeatureStub> = AttributeKey("FeatureStub")

            override fun install(feature: FeatureStub, scope: HttpClient) = Unit

            override fun prepare(block: Config.() -> Unit): FeatureStub {
                Config().apply(block)
                return FeatureStub()
            }
        }
    }
}
