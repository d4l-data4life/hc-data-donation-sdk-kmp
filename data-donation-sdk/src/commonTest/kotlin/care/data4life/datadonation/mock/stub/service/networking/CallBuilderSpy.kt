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

import care.data4life.datadonation.core.model.ModelContract
import care.data4life.datadonation.internal.data.service.networking.AccessToken
import care.data4life.datadonation.internal.data.service.networking.Header
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.Parameter
import care.data4life.datadonation.internal.data.service.networking.Path
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import io.ktor.client.HttpClient
import kotlin.native.concurrent.ThreadLocal

internal class CallBuilderSpy private constructor(
    private val onExecute: ((Networking.Method, Path) -> Any)?
) : Networking.CallBuilder {
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

    override fun setHeaders(header: Header): Networking.CallBuilder {
        return this.also { this.headers = header }
    }

    override fun setParameter(parameter: Parameter): Networking.CallBuilder {
        return this.also { this.parameter = parameter }
    }

    override fun setAccessToken(token: AccessToken): Networking.CallBuilder {
        return this.also { this.token = token }
    }

    override fun useJsonContentType(): Networking.CallBuilder {
        return this.also { json = true }
    }

    override fun setBody(body: Any): Networking.CallBuilder {
        return this.also { this.body = body }
    }

    override suspend fun execute(
        method: Networking.Method,
        path: Path
    ): Any {
        return onExecute?.invoke(
            method,
            path
        ) ?: throw MockException()
    }

    @ThreadLocal
    companion object Factory : Networking.CallBuilderFactory, MockContract.Stub {
        var onExecute: ((Networking.Method, Path) -> Any)? = null

        private var environment: ModelContract.Environment? = null
        val delegatedEnvironment: ModelContract.Environment?
            get() = environment

        private var client: HttpClient? = null
        val delegatedClient: HttpClient?
            get() = client

        private var port: Int? = null
        val delegatedPort: Int?
            get() = port

        private var instance: CallBuilderSpy? = null
        val lastInstance: CallBuilderSpy?
            get() = instance

        override fun getInstance(
            environment: ModelContract.Environment,
            client: HttpClient,
            port: Int?
        ): Networking.CallBuilder {
            return CallBuilderSpy(onExecute).also {
                Factory.environment = environment
                Factory.client = client
                Factory.port = port
                instance = it
            }
        }

        override fun clear() {
            onExecute = null
            environment = null
            client = null
            port = null
            instance = null
        }
    }
}
