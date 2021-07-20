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

import care.data4life.datadonation.core.model.ModelContract
import care.data4life.sdk.log.Log
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
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
    fun `Given resolveServiceModule is called it creates a Module, which contains a RequestBuilderTemplate`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
                module {
                    single { ModelContract.Environment.DEV }
                }
            )
        }
        // Then
        val builder: Networking.RequestBuilderTemplate = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a HTTPClient`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
            )
        }
        // Then
        val client: HttpClient = koin.koin.get()
        assertNotNull(client)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains the HTTPClient plugin configuration`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
            )
        }
        // Then
        val config: Map<HttpClientFeature<*, *>, Pair<Networking.Configurator<Any, Any>, Any>> = koin.koin.get()
        assertNotNull(config)

        assertEquals(
            actual = config.size,
            expected = 2
        )

        assertTrue(config.containsKey(JsonFeature))
        assertTrue(config.containsKey(Logging))

        val (serializerConfig, jsonConfigurator) = config[JsonFeature]!!
        assertSame<Any>(
            actual = serializerConfig,
            expected = SerializerConfigurator
        )
        assertSame(
            actual = jsonConfigurator,
            expected = JsonConfigurator
        )

        val (loggingConfig, logger) = config[Logging]!!
        assertSame<Any>(
            actual = loggingConfig,
            expected = LoggerConfigurator
        )
        assertSame(
            actual = logger,
            expected = Log.logger
        )
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains the HTTPClient validator configuration`() {
        // When
        val koin = koinApplication {
            modules(
                resolveNetworking(),
            )
        }
        // Then
        val config: Pair<Networking.ResponseValidatorConfigurator, Pair<Networking.ResponseValidator?, Networking.ErrorPropagator?>> = koin.koin.get()
        assertNotNull(config)

        val (responseValidator, auxiliary) = config

        assertSame(
            actual = responseValidator,
            expected = ResponseValidatorConfigurator
        )
        assertEquals(
            actual = auxiliary,
            expected = Pair(ResponseValidator, ErrorPropagator)
        )
    }
}
