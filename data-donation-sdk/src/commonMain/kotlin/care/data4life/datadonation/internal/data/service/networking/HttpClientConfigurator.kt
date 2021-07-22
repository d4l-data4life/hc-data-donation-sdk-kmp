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
import io.ktor.client.features.HttpResponseValidator

internal object HttpClientConfigurator : Networking.HttpClientConfigurator {
    private fun installFeatures(
        httpConfig: HttpClientConfig<*>,
        installers: List<Networking.HttpPluginInstaller<in Any, in Any?>>?
    ) {
        if (installers is List<*>) {
            installers.forEach { (plugin, configurator, subConfig) ->
                httpConfig.install(plugin) {
                    configurator.configure(
                        this,
                        subConfig
                    )
                }
            }
        }
    }

    private fun configureHttpResponseValidation(
        httpConfig: HttpClientConfig<*>,
        responseValidator: Networking.HttpResponseValidation?
    ) {
        if (responseValidator is Networking.HttpResponseValidation) {
            val (configurator, successfulResponseValidation, errorPropagation) = responseValidator

            httpConfig.HttpResponseValidator {
                configurator.configure(
                    this,
                    successfulResponseValidation,
                    errorPropagation
                )
            }
        }
    }

    override fun configure(
        httpConfig: HttpClientConfig<*>,
        installers: List<Networking.HttpPluginInstaller<in Any, in Any?>>?,
        responseValidator: Networking.HttpResponseValidation?
    ) {
        installFeatures(httpConfig, installers)
        configureHttpResponseValidation(httpConfig, responseValidator)
    }
}
