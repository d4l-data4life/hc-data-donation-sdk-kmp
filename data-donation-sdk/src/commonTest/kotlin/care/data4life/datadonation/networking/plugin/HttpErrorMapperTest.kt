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

package care.data4life.datadonation.networking.plugin

import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HttpErrorMapperTest {
    @Test
    fun `It fulfils HttpErrorMapper`() {
        val propagator: Any = HttpErrorMapper

        assertTrue(propagator is KtorPluginsContract.HttpErrorMapper)
    }

    @Test
    fun `Given mapAndThrow is called with a Throwable, it rethrows non ResponseException unwrapped and just runs`() {
        // Given
        val throwable = RuntimeException("abc")

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            HttpErrorMapper.mapAndThrow(throwable)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = throwable.message
        )
    }

    @Test
    fun `Given mapAndThrow is called with a Throwable, it rethrows it as HttpRuntimeError, which contains a HttpStatusCode`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                handleResponseException { response ->
                    HttpErrorMapper.mapAndThrow(response)
                }
            }

            engine {
                addHandler {
                    respondError(
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }

        // Then
        val error = assertFailsWith<HttpRuntimeError> {
            client.get("/somewhre")
        }

        assertEquals(
            actual = error.statusCode,
            expected = HttpStatusCode.InternalServerError
        )
    }
}
