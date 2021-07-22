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

import care.data4life.datadonation.mock.fake.defaultResponse
import care.data4life.datadonation.mock.stub.service.networking.plugin.HttpErrorMapperStub
import care.data4life.datadonation.mock.stub.service.networking.plugin.HttpSuccessfulResponseValidatorStub
import care.data4life.sdk.lang.D4LException
import care.data4life.sdk.util.test.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class HttpResponseValidatorConfiguratorTest {
    @Test
    fun `It fulfils HttpResponseValidatorConfigurator`() {
        val configurator: Any = HttpResponseValidatorConfigurator

        assertTrue(configurator is KtorPluginsContract.HttpResponseValidatorConfigurator)
    }

    @Test
    fun `Given configure is called with a Pair of Validators, it ignores ResponseValidation if it is null`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            install(HttpCallValidator) {
                HttpResponseValidatorConfigurator.configure(
                    this,
                    KtorPluginsContract.HttpResponseValidationConfiguration(
                        null,
                        null
                    )
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
    fun `Given configure is called  with a Pair of Validators, it configures a HttpSuccessfulResponseValidator if it is not null`() = runBlockingTest {
        // Given
        val successfulResponseValidator = HttpSuccessfulResponseValidatorStub()

        successfulResponseValidator.whenValidate = {
            throw RuntimeException()
        }

        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    defaultResponse(this)
                }
            }
            install(HttpCallValidator) {
                HttpResponseValidatorConfigurator.configure(
                    this,
                    KtorPluginsContract.HttpResponseValidationConfiguration(
                        successfulResponseValidator,
                        null
                    )
                )
            }
        }

        // When
        assertFailsWith<RuntimeException> {
            client.request<HttpResponse>("/not/important")
        }
    }

    @Test
    fun `Given configure is called with a Pair of Validators, it ignores HttpErrorPropagator if it is null`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            install(HttpCallValidator) {
                HttpResponseValidatorConfigurator.configure(
                    this,
                    KtorPluginsContract.HttpResponseValidationConfiguration(
                        null,
                        null
                    )
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
    fun `Given configure is called  with a Pair of Validators, it configures a HttpErrorPropagator if it is not null`() = runBlockingTest {
        // Given
        val propagator = HttpErrorMapperStub()

        propagator.whenPropagate = {
            throw D4LException()
        }

        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                        "Bad Request",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }

            install(HttpCallValidator) {
                HttpResponseValidatorConfigurator.configure(
                    this,
                    KtorPluginsContract.HttpResponseValidationConfiguration(
                        null,
                        propagator
                    )
                )
            }
        }

        // When
        assertFailsWith<D4LException> {
            client.request<HttpResponse>("/not/important")
        }
    }
}
