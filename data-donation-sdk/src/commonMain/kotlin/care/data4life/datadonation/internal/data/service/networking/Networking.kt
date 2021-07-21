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

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.statement.HttpStatement
import kotlinx.serialization.json.JsonBuilder

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias AccessToken = String
internal typealias Path = List<String>

internal interface Networking {
    interface Logger : io.ktor.client.features.logging.Logger {
        override fun log(message: String)

        companion object {
            const val PREFIX = "DD-SDK-HTTP:"
        }
    }

    fun interface HttpFeatureConfigurator<FeatureConfiguration : Any, SubConfiguration> {
        fun configure(pluginConfig: FeatureConfiguration, subConfiguration: SubConfiguration)
    }

    fun interface JsonConfigurator {
        fun configure(jsonBuild: JsonBuilder): JsonBuilder
    }

    fun interface HttpSerializerConfigurator : HttpFeatureConfigurator<JsonFeature.Config, JsonConfigurator>
    fun interface HttpLoggingConfigurator : HttpFeatureConfigurator<Logging.Config, care.data4life.sdk.log.Logger>

    data class HttpFeatureInstaller<FeatureConfiguration : Any, SubConfiguration>(
        val feature: HttpClientFeature<*, *>,
        val featureConfigurator: HttpFeatureConfigurator<FeatureConfiguration, SubConfiguration>,
        val subConfiguration: SubConfiguration
    )

    fun interface HttpClientConfigurator {
        fun configure(
            httpConfig: HttpClientConfig<*>,
            installers: List<HttpFeatureInstaller<in Any, in Any?>>
        )
    }

    enum class Method(name: String) {
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put")
    }

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
