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

import care.data4life.datadonation.lang.HttpRuntimeError
import care.data4life.datadonation.mock.fake.defaultResponse
import care.data4life.sdk.util.test.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.request.delete
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HttpSuccessfulResponseValidatorTest {
    @Test
    fun `It fulfils HttpSuccessfulResponseValidator`() {
        val validator: Any = HttpSuccessfulResponseValidator

        assertTrue(validator is KtorPluginsContract.HttpSuccessfulResponseValidator)
    }

    @Test
    fun `Given, validate is called with a response, the requests uses not DELETE it ignores 200 status`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                validateResponse { response ->
                    HttpSuccessfulResponseValidator.validate(response)
                }
            }

            engine {
                addHandler {
                    defaultResponse(this)
                }
            }
        }

        // When
        val response = client.request<HttpResponse>("/not/important")

        // Then
        assertEquals(
            actual = response.status,
            expected = HttpStatusCode.OK
        )
    }

    @Test
    fun `Given, validate is called with a response, the requests uses not DELETE it fails for non 200 status`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                validateResponse { response ->
                    HttpSuccessfulResponseValidator.validate(response)
                }
            }

            engine {
                addHandler {
                    respond(
                        content = "",
                        status = HttpStatusCode.NoContent
                    )
                }
            }
        }

        // Then
        val error = assertFailsWith<HttpRuntimeError> {
            // When
            client.request<HttpResponse>("/not/important")
        }

        assertEquals(
            actual = error.statusCode,
            expected = HttpStatusCode.NoContent
        )
    }

    @Test
    fun `Given, validate is called with a response, the requests uses DELETE it ignores 204 status`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                validateResponse { response ->
                    HttpSuccessfulResponseValidator.validate(response)
                }
            }

            engine {
                addHandler {
                    respond(
                        content = "",
                        status = HttpStatusCode.NoContent
                    )
                }
            }
        }

        // When
        val response = client.delete<HttpResponse>("/not/important")

        // Then
        assertEquals(
            actual = response.status,
            expected = HttpStatusCode.NoContent
        )
    }

    @Test
    fun `Given, validate is called with a response, the requests uses DELETE it fails for non 204 status`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                validateResponse { response ->
                    HttpSuccessfulResponseValidator.validate(response)
                }
            }

            engine {
                addHandler {
                    defaultResponse(this)
                }
            }
        }

        // Then
        val error = assertFailsWith<HttpRuntimeError> {
            // When
            client.delete<HttpResponse>("/not/important")
        }

        assertEquals(
            actual = error.statusCode,
            expected = HttpStatusCode.OK
        )
    }
}
