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

package care.data4life.datadonation.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.statement.HttpStatement

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias AccessToken = String
internal typealias Path = List<String>

// TODO Move into util rep
internal interface Networking {
    fun interface HttpPluginConfigurator<PluginConfiguration : Any, SubConfiguration> {
        fun configure(pluginConfiguration: PluginConfiguration, subConfiguration: SubConfiguration)
    }

    data class HttpPluginInstaller<PluginConfiguration : Any, SubConfiguration>(
        val feature: HttpClientFeature<*, *>,
        val pluginConfigurator: HttpPluginConfigurator<PluginConfiguration, SubConfiguration>,
        val subConfiguration: SubConfiguration
    )

    interface HttpClientConfigurator {
        fun configure(
            httpConfig: HttpClientConfig<*>,
            installers: List<HttpPluginInstaller<in Any, in Any?>>? = null
        )
    }

    enum class Method(name: String) {
        HEAD("head"),
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
            val BODYLESS_METHODS = listOf(Method.HEAD, Method.GET)
        }
    }

    interface RequestBuilderFactory {
        fun create(): RequestBuilder
        fun withHost(host: String): RequestBuilderFactory
    }
}
