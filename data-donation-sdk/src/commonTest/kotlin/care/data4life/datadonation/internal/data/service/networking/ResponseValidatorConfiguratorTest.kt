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

import care.data4life.datadonation.mock.fake.defaultResponse
import care.data4life.sdk.lang.D4LException
import care.data4life.sdk.util.test.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ResponseValidatorConfiguratorTest {
    @Test
    fun `It fulfils ResponseValidatorConfigurator`() {
        val configurator: Any = ResponseValidatorConfigurator

        assertTrue(configurator is Networking.ResponseConfigurator)
    }

    @Test
    fun `Given configure is called with a Pair of Validators, it ignores ResponseValidation if it is null`() = runBlockingTest {
        // Given
        val validators = Pair(null, null)
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                ResponseValidatorConfigurator.configure(
                    this,
                    validators
                )
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
    fun `Given configure is called  with a Pair of Validators, it sets a ResponseValidation if it is not null`() = runBlockingTest {
        // Given
        val validators = Pair(ResponseValidatorStub, null)
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    defaultResponse(this)
                }
            }

            HttpResponseValidator {
                ResponseValidatorConfigurator.configure(
                    this,
                    validators
                )
            }
        }

        // When
        assertFailsWith<RuntimeException> {
            client.request<HttpResponse>("/not/important")
        }
    }

    @Test
    fun `Given configure is called with a Pair of Validators, it ignores ErrorPropagator if it is null`() = runBlockingTest {
        // Given
        val validators = Pair(null, null)
        val client = HttpClient(MockEngine) {
            HttpResponseValidator {
                ResponseValidatorConfigurator.configure(
                    this,
                    validators
                )
            }

            engine {
                addHandler {
                    respond(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }
        }

        // When
        assertFailsWith<ClientRequestException> {
            client.request<HttpResponse>("/not/important")
        }
    }

    @Test
    fun `Given configure is called  with a Pair of Validators, it sets a ErrorPropagator if it is not null`() = runBlockingTest {
        // Given
        val validators = Pair(null, ErrorPropagatorStub)
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }

            HttpResponseValidator {
                ResponseValidatorConfigurator.configure(
                    this,
                    validators
                )
            }
        }

        // When
        assertFailsWith<D4LException> {
            client.request<HttpResponse>("/not/important")
        }
    }

    private object ResponseValidatorStub : Networking.ResponseValidator {
        override fun validate(response: HttpResponse) {
            throw RuntimeException()
        }
    }

    private object ErrorPropagatorStub : Networking.ErrorPropagator {
        override fun propagate(error: Throwable) {
            throw D4LException()
        }
    }
}
