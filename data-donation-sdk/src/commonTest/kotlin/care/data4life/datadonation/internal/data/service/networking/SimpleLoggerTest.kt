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

import care.data4life.datadonation.mock.stub.service.networking.LoggerStub
import care.data4life.sdk.log.Log
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimpleLoggerTest {
    @Test
    fun `It fulfils logger`() {
        val logger: Any = SimpleLogger(Log.logger)

        assertTrue(logger is Networking.Logger)
    }

    @Test
    fun `Given log is called, it delegates the entry with a prefix to the SDK Logger`() {
        // Given
        val internalLogger = LoggerStub()
        var capturedMessage: String? = null
        val message = "Test123"

        internalLogger.whenInfo = { delegatedMessage ->
            capturedMessage = delegatedMessage
        }

        // When
        val logger = SimpleLogger(internalLogger)
        logger.log(message)

        // Then
        assertTrue(
            capturedMessage!!.startsWith("DD-SDK-HTTP:")
        )

        assertEquals(
            actual = capturedMessage!!.substring(startIndex = "DD-SDK-HTTP:".length + 1),
            expected = message
        )
    }
}
