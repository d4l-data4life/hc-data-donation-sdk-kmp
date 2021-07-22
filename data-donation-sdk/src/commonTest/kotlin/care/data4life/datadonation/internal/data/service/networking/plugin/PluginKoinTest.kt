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

package care.data4life.datadonation.internal.data.service.networking.plugin

import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.sdk.log.Log
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class PluginKoinTest {
    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a List of HttpFeatureInstaller`() {
        // When
        val koin = koinApplication {
            modules(
                resolveKtorPlugins()
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
    fun `Given resolveServiceModule is called it creates a Module, which contains the HTTPClient validator configuration`() {
        // When
        val koin = koinApplication {
            modules(
                resolveKtorPlugins(),
            )
        }
        // Then
        val config: Networking.HttpResponseValidation = koin.koin.get()
        assertNotNull(config)

        val (responseValidator, successfulResponses, errorPropagator) = config

        assertSame(
            actual = responseValidator,
            expected = HttpResponseValidatorConfigurator
        )
        assertSame(
            actual = successfulResponses,
            expected = HttpSuccessfulResponseValidator
        )
        assertSame(
            actual = errorPropagator,
            expected = HttpErrorPropagator
        )
    }
}
