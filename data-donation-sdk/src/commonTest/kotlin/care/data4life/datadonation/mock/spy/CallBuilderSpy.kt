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

package care.data4life.datadonation.mock.spy

import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.data.service.AccessToken
import care.data4life.datadonation.internal.data.service.Header
import care.data4life.datadonation.internal.data.service.Parameter
import care.data4life.datadonation.internal.data.service.Path
import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import io.ktor.client.HttpClient

internal class CallBuilderSpy private constructor() : ServiceContract.CallBuilder, MockContract.Spy {
    var whenExecute: ((ServiceContract.Method, Path) -> Any)? = null

    private var headers: Header? = null
    val delegatedHeaders: Header?
        get() = headers

    private var parameter: Parameter? = null
    val delegatedParameter: Header?
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

    override fun setHeaders(header: Header): ServiceContract.CallBuilder {
        return this.also { this.headers = header }
    }

    override fun setParameter(parameter: Parameter): ServiceContract.CallBuilder {
        return this.also { this.parameter = parameter }
    }

    override fun setAccessToken(token: AccessToken): ServiceContract.CallBuilder {
        return this.also { this.token = token }
    }

    override fun useJsonContentType(): ServiceContract.CallBuilder {
        return this.also { json = true }
    }

    override fun setBody(body: Any): ServiceContract.CallBuilder {
        return this.also { this.body = body }
    }

    override suspend fun execute(
        method: ServiceContract.Method,
        path: Path
    ): Any {
        return whenExecute?.invoke(
            method,
            path
        ) ?: throw MockException()
    }

    override fun clear() {
        whenExecute = null
    }

    companion object : ServiceContract.CallBuilderFactory, MockContract.Spy {
        private var environment: Environment? = null
        val delegatedEnvironment: Environment?
            get() = environment

        private var client: HttpClient? = null
        val delegatedClient: HttpClient?
            get() = client

        private var port: Int? = null
        val delegatedPort: Int?
            get() = port

        override fun getInstance(
            environment: Environment,
            client: HttpClient,
            port: Int?
        ): ServiceContract.CallBuilder {
            return CallBuilderSpy().also {
                this.environment = environment
                this.client = client
                this.port = port
            }
        }

        override fun clear() {
            environment = null
            client = null
            port = null
        }
    }
}
