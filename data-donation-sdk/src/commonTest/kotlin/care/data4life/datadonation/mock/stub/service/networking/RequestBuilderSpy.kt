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

import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.data.service.networking.AccessToken
import care.data4life.datadonation.internal.data.service.networking.Header
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.Parameter
import care.data4life.datadonation.internal.data.service.networking.Path
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpStatement
import kotlin.native.concurrent.ThreadLocal

internal class RequestBuilderSpy private constructor(
    private val onPrepare: ((Networking.Method, Path) -> HttpStatement)?
) : Networking.RequestBuilder, Networking.RequestBuilderTemplate {
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

    private var instanceCounter = 0
    val createdInstances: Int
        get() = instanceCounter

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

    override fun create(): Networking.RequestBuilder {
        return this.also {
            clear()
            instanceCounter++
        }
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

    @ThreadLocal
    companion object TemplateFactory : Networking.RequestBuilderTemplateFactory, MockContract.Spy {
        var onPrepare: ((Networking.Method, Path) -> HttpStatement)? = null

        private var environment: Environment? = null
        val delegatedEnvironment: Environment?
            get() = environment

        private var client: HttpClient? = null
        val delegatedClient: HttpClient?
            get() = client

        private var port: Int? = null
        val delegatedPort: Int?
            get() = port

        private var instance: RequestBuilderSpy? = null
        val lastInstance: RequestBuilderSpy?
            get() = instance

        override fun getInstance(
            environment: Environment,
            client: HttpClient,
            port: Int?
        ): Networking.RequestBuilderTemplate {
            return RequestBuilderSpy(onPrepare).also {
                this.environment = environment
                this.client = client
                this.port = port
                this.instance = it
            }
        }

        override fun clear() {
            onPrepare = null
            environment = null
            client = null
            port = null
            instance = null
        }
    }
}
