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

import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.util.JsonConfiguratorContract
import care.data4life.sdk.lang.D4LRuntimeException
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.statement.HttpResponse

internal interface KtorPluginsContract {
    interface Logger : io.ktor.client.features.logging.Logger {
        override fun log(message: String)

        companion object {
            const val PREFIX = "DD-SDK-HTTP:"
        }
    }

    fun interface HttpSuccessfulResponseValidator {
        @Throws(D4LRuntimeException::class)
        fun validate(response: HttpResponse)
    }

    fun interface HttpErrorMapper {
        @Throws(D4LRuntimeException::class)
        fun mapAndThrow(error: Throwable)
    }

    data class HttpResponseValidationConfiguration(
        val successfulResponseValidator: HttpSuccessfulResponseValidator? = null,
        val errorMapper: HttpErrorMapper? = null
    )

    fun interface HttpSerializerConfigurator : Networking.HttpPluginConfigurator<JsonFeature.Config, JsonConfiguratorContract>
    fun interface HttpLoggingConfigurator : Networking.HttpPluginConfigurator<Logging.Config, care.data4life.sdk.log.Logger>
    fun interface HttpResponseValidatorConfigurator : Networking.HttpPluginConfigurator<HttpCallValidator.Config, HttpResponseValidationConfiguration>
}
