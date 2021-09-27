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

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline.Phases.Before
import io.ktor.client.request.HttpSendPipeline.Phases.State
import io.ktor.http.HeadersBuilder
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext

internal class HttpCustomContentType(
    private val config: InternalConfig
) {
    class Config(
        var replacementHeader: String? = null,
        var amendmentHeader: String? = null,
    ) {
        fun toInternalConfig(): InternalConfig {
            return InternalConfig(replacementHeader, amendmentHeader)
        }
    }

    data class InternalConfig(
        val replacementHeader: String? = null,
        val amendmentHeader: String? = null,
    )

    companion object Feature : HttpClientFeature<Config, HttpCustomContentType> {
        override val key: AttributeKey<HttpCustomContentType> = AttributeKey("HttpCustomContentType")

        override fun prepare(block: Config.() -> Unit): HttpCustomContentType {
            val config = Config().apply(block).toInternalConfig()

            return HttpCustomContentType(config)
        }

        private fun headerFieldsToList(headerFields: String): List<String> {
            return headerFields.trim('[')
                .trim(']')
                .split(", ")
                .sorted()
        }

        private fun resolveHeaderFields(
            headers: HeadersBuilder,
            HeaderFieldName: String
        ): List<String>? {
            val headerFields = headers.get(HeaderFieldName)
            headers.remove(HeaderFieldName)

            return if (headerFields != null) {
                headerFieldsToList(headerFields)
            } else {
                null
            }
        }

        private fun addAcceptFields(
            headers: HeadersBuilder,
            headerFields: List<String>
        ) {
            headerFields.forEach { header ->
                headers.append("Accept", header)
            }
        }

        private fun applyReplacementHeader(
            pipeline: PipelineContext<Any, HttpRequestBuilder>,
            scope: HttpClient,
            headerField: String
        ) {
            val headerFields = resolveHeaderFields(
                pipeline.context.headers,
                headerField
            )

            if (headerFields != null) {
                setAccept(
                    scope,
                    headerFields
                )
            }
        }

        private fun setAccept(scope: HttpClient, headerFields: List<String>) {
            scope.sendPipeline.intercept(State) {
                this.context.headers.remove("Accept")
                addAcceptFields(this.context.headers, headerFields)
            }
        }

        private fun applyAmendmentHeader(
            pipeline: PipelineContext<Any, HttpRequestBuilder>,
            scope: HttpClient,
            headerField: String
        ) {
            val headerFields = resolveHeaderFields(
                pipeline.context.headers,
                headerField
            )

            if (headerFields != null) {
                addAccept(
                    scope,
                    headerFields
                )
            }
        }

        private fun addAccept(scope: HttpClient, headerFields: List<String>) {
            scope.sendPipeline.intercept(State) {
                addAcceptFields(this.context.headers, headerFields)
            }
        }

        override fun install(feature: HttpCustomContentType, scope: HttpClient) {
            scope.requestPipeline.intercept(Before) {
                if (feature.config.replacementHeader is String) {
                    applyReplacementHeader(
                        this,
                        scope,
                        feature.config.replacementHeader
                    )
                }

                if (feature.config.amendmentHeader is String) {
                    applyAmendmentHeader(
                        this,
                        scope,
                        feature.config.amendmentHeader
                    )
                }
            }
        }
    }
}
