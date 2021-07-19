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

package care.data4life.datadonation.mock.fake

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpResponseData
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import io.ktor.util.AttributeKey

fun getDefaultMockClient(): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler {
                defaultResponse(this)
            }
        }
    }
}

fun createMockClientWithResponse(
    responseObjects: List<Any>? = null,
    response: (scope: MockRequestHandleScope) -> HttpResponseData,
): HttpClient {
    return HttpClient(MockEngine) {
        if (responseObjects != null) {
            install(HttpMockObjectResponse) {
                addResponses(responseObjects)
            }
        }

        engine {
            addHandler {
                response(this)
            }
        }
    }
}

class HttpMockObjectResponse(
    internal val responses: MutableList<Any>
) {
    class Config {
        internal val responses: MutableList<Any> = mutableListOf()

        fun addResponse(response: Any) {
            responses.add(response)
        }

        fun addResponses(responses: List<Any>) {
            this.responses.addAll(responses)
        }
    }

    companion object Feature : HttpClientFeature<Config, HttpMockObjectResponse> {
        override val key: AttributeKey<HttpMockObjectResponse> = AttributeKey("HttpMockObjectResponse")

        override fun prepare(block: Config.() -> Unit): HttpMockObjectResponse {
            val config = Config().apply(block)

            with(config) {
                return HttpMockObjectResponse(responses)
            }
        }

        override fun install(feature: HttpMockObjectResponse, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.After) { (info, _) ->
                val value = if (feature.responses.size == 1) {
                    feature.responses[0]
                } else {
                    feature.responses.removeAt(0)
                }

                proceedWith(
                    HttpResponseContainer(
                        info,
                        value
                    )
                )
            }
        }
    }
}

fun defaultResponse(scope: MockRequestHandleScope): HttpResponseData {
    return scope.respond(
        "Hello, world",
        headers = headersOf(
            "Content-Type" to listOf(
                ContentType.Text.Plain.toString()
            )
        )
    )
}
