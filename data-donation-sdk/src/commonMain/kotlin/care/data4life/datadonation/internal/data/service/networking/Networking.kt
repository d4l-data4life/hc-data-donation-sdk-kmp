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

import care.data4life.sdk.lang.D4LRuntimeException
import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import kotlinx.serialization.json.JsonBuilder

typealias Header = Map<String, String>
typealias Parameter = Map<String, Any?>
typealias AccessToken = String
typealias Path = List<String>

internal interface Networking {
    interface Logger : io.ktor.client.features.logging.Logger {
        override fun log(message: String)

        companion object {
            const val PREFIX = "DD-SDK-HTTP:"
        }
    }

    fun interface Configurator<Config : Any, AuxiliaryConfigurator : Any> {
        fun configure(pluginConfig: Config, auxiliaryConfigurator: AuxiliaryConfigurator)
    }

    fun interface JsonConfigurator {
        fun configure(jsonBuild: JsonBuilder): JsonBuilder
    }

    fun interface ResponseValidator {
        @Throws(D4LRuntimeException::class)
        fun validate(response: HttpResponse)
    }

    fun interface ErrorPropagator {
        @Throws(D4LRuntimeException::class)
        fun propagate(error: Throwable)
    }

    fun interface SerializerConfigurator : Configurator<JsonFeature.Config, JsonConfigurator>
    fun interface LoggingConfigurator : Configurator<Logging.Config, care.data4life.sdk.log.Logger>
    fun interface ResponseValidatorConfigurator : Configurator<HttpCallValidator.Config, Pair<ResponseValidator?, ErrorPropagator?>>

    fun interface ClientConfigurator {
        fun configure(
            config: HttpClientConfig<*>,
            installers: Map<HttpClientFeature<*, *>, Pair<Configurator<Any, Any>, Any>>,
            responseValidator: Pair<ResponseValidatorConfigurator, Pair<ResponseValidator?, ErrorPropagator?>>
        )
    }

    enum class Method(name: String) {
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

    // TODO Add a new package with potential HTTP Interceptor
    interface RequestBuilder {
        fun setHeaders(header: Header): RequestBuilder
        fun setParameter(parameter: Parameter): RequestBuilder
        fun setAccessToken(token: AccessToken): RequestBuilder
        fun useJsonContentType(): RequestBuilder
        fun setBody(body: Any): RequestBuilder

        fun prepare(
            method: Method = Method.GET,
            path: Path = listOf("")
        ): HttpStatement

        companion object {
            const val ACCESS_TOKEN_FIELD = "Authorization"
            const val ACCESS_TOKEN_VALUE_PREFIX = "Bearer"
        }
    }

    interface RequestBuilderTemplate {
        fun create(): RequestBuilder
    }
}
