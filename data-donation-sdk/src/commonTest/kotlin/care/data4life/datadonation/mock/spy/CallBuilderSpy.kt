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
import io.ktor.client.HttpClient

internal class CallBuilderSpy private constructor() : ServiceContract.CallBuilder {
    override fun setHeaders(header: Header): ServiceContract.CallBuilder {
        TODO("Not yet implemented")
    }

    override fun setParameter(parameter: Parameter): ServiceContract.CallBuilder {
        TODO("Not yet implemented")
    }

    override fun setAccessToken(token: AccessToken): ServiceContract.CallBuilder {
        TODO("Not yet implemented")
    }

    override fun useJsonContentType(): ServiceContract.CallBuilder {
        TODO("Not yet implemented")
    }

    override fun setBody(body: Any): ServiceContract.CallBuilder {
        TODO("Not yet implemented")
    }

    override suspend fun execute(method: ServiceContract.Method, path: Path, port: Int?): Any {
        TODO("Not yet implemented")
    }

    companion object : ServiceContract.CallBuilderFactory {
        override fun getInstance(
            environment: Environment,
            client: HttpClient
        ): ServiceContract.Service = CallBuilderSpy(
            environment,
            client
        )
    }
}
