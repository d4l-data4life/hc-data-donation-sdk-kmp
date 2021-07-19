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

package care.data4life.datadonation.mock.stub.service.networking

import care.data4life.datadonation.internal.data.service.networking.AccessToken
import care.data4life.datadonation.internal.data.service.networking.Header
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.Parameter
import care.data4life.datadonation.internal.data.service.networking.Path
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import io.ktor.client.statement.HttpStatement

internal class RequestBuilderSpy private constructor(
    private val onPrepare: ((Networking.Method, Path) -> HttpStatement)?
) : Networking.RequestBuilder {
    private var headers: Header? = null
    val delegatedHeaders: Header?
        get() = headers

    private var parameter: Parameter? = null
    val delegatedParameter: Parameter?
        get() = parameter

    private var token: AccessToken? = null
    val delegatedAccessToken: AccessToken?
        get() = token

    private var json: Boolean = false
    val delegatedJsonFlag: Boolean
        get() = json

    private var body: Any? = null
    val delegatedBody: Any?
        get() = body

    override fun setHeaders(header: Header): Networking.RequestBuilder {
        return this.also { this.headers = header }
    }

    override fun setParameter(parameter: Parameter): Networking.RequestBuilder {
        return this.also { this.parameter = parameter }
    }

    override fun setAccessToken(token: AccessToken): Networking.RequestBuilder {
        return this.also { this.token = token }
    }

    override fun useJsonContentType(): Networking.RequestBuilder {
        return this.also { json = true }
    }

    override fun setBody(body: Any): Networking.RequestBuilder {
        return this.also { this.body = body }
    }

    private fun clear() {
        headers = null
        parameter = null
        token = null
        json = false
        body = null
    }

    override fun prepare(
        method: Networking.Method,
        path: Path
    ): HttpStatement {
        return onPrepare?.invoke(
            method,
            path
        ) ?: throw MockException()
    }

    class Template : Networking.RequestBuilderTemplate, MockContract.Spy {
        var onPrepare: ((Networking.Method, Path) -> HttpStatement)? = null

        private var instance: RequestBuilderSpy? = null
        val lastInstance: RequestBuilderSpy?
            get() = instance

        private var instanceCounter = 0
        val createdInstances: Int
            get() = instanceCounter

        override fun create(): Networking.RequestBuilder {
            return RequestBuilderSpy(onPrepare).also {
                this.instance = it
                this.instanceCounter++
            }
        }

        override fun clear() {
            onPrepare = null
            instance = null
            instanceCounter = 0
        }
    }
}
