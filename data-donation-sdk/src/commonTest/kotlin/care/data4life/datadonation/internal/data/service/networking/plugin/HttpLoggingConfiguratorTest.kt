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

import care.data4life.datadonation.mock.stub.service.networking.plugin.LoggerStub
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class HttpLoggingConfiguratorTest {
    @Test
    fun `It fulfils HttpLoggingConfigurator`() {
        val configurator: Any = HttpLoggingConfigurator

        assertTrue(configurator is KtorPluginsContract.HttpLoggingConfigurator)
    }

    @Test
    fun `Given configure is called with a LoggingConfig, it sets it up and just runs`() {
        // Given
        val config = Logging.Config()
        val internalLogger = LoggerStub()
        var wasCalled = false

        internalLogger.whenInfo = { _ ->
            wasCalled = true
        }

        // When
        val result = HttpLoggingConfigurator.configure(config, internalLogger)
        config.logger.log("test")

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = config.level,
            expected = LogLevel.ALL
        )

        assertTrue(config.logger is KtorPluginsContract.Logger)
        assertTrue(wasCalled)
    }
}
