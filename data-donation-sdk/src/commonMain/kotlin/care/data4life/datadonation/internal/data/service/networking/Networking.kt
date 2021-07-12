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

import care.data4life.datadonation.core.model.Environment
import io.ktor.client.HttpClient

typealias Header = Map<String, String>
typealias Parameter = Map<String, Any?>
typealias AccessToken = String
typealias Path = List<String>

internal interface Networking {
    enum class Method(name: String) {
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    // TODO Add a new package with potential HTTP Interceptor
    interface CallBuilder {
        fun setHeaders(header: Header): CallBuilder
        fun setParameter(parameter: Parameter): CallBuilder
        fun setAccessToken(token: AccessToken): CallBuilder
        fun useJsonContentType(): CallBuilder
        fun setBody(body: Any): CallBuilder

        suspend fun execute(
            method: Method = Method.GET,
            path: Path = listOf("")
        ): Any

        companion object {
            const val ACCESS_TOKEN_FIELD = "Authorization"
            const val ACCESS_TOKEN_VALUE_PREFIX = "Bearer"
        }
    }

    interface CallBuilderFactory {
        fun getInstance(
            environment: Environment,
            client: HttpClient,
            port: Int? = null
        ): CallBuilder
    }
}
