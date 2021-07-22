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
import care.data4life.datadonation.mock.stub.service.networking.HttpFeatureConfiguratorStub
import care.data4life.datadonation.mock.stub.service.networking.HttpResponseValidatorConfiguratorStub
import care.data4life.datadonation.mock.stub.service.networking.plugin.HttpErrorPropagatorStub
import care.data4life.datadonation.mock.stub.service.networking.plugin.HttpSuccessfulResponseValidatorStub
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
        val builder: Networking.RequestBuilderTemplate = koin.koin.get()
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
            Networking.HttpFeatureInstaller(
                FeatureStub,
                HttpFeatureConfiguratorStub<Any, Any>(),
                "something"
            )
        )
        val validation = Networking.HttpResponseValidation(
            HttpResponseValidatorConfiguratorStub(),
            HttpSuccessfulResponseValidatorStub(),
            HttpErrorPropagatorStub()
        )

        var capturedHttpConfig: HttpClientConfig<*>? = null
        var capturedFeatures: List<Networking.HttpFeatureInstaller<in Any, in Any>>? = null
        var capturedResponseValidation: Networking.HttpResponseValidation? = null

        clientConfigurator.whenConfigure = { delegatedHttpConfig, delegatedFeatures, delegatedResponseValidation ->
            capturedHttpConfig = delegatedHttpConfig
            capturedFeatures = delegatedFeatures
            capturedResponseValidation = delegatedResponseValidation
        }

        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module(override = true) {
                    single<Networking.HttpClientConfigurator> {
                        clientConfigurator
                    }
                    single<List<Networking.HttpFeatureInstaller<out Any, out Any?>>> {
                        features
                    }
                    single { validation }
                    single { Contract.Environment.DEV }
                }
            )
        }
        // Then
        val client: HttpClient = koin.koin.get()
        assertNotNull(client)

        assertTrue(capturedHttpConfig is HttpClientConfig<*>)
        assertSame(
            actual = capturedFeatures,
            expected = features
        )
        assertSame(
            actual = capturedResponseValidation,
            expected = validation
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
