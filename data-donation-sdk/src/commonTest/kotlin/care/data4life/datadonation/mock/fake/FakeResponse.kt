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

package care.data4life.datadonation.mock.fake

import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import kotlin.coroutines.CoroutineContext

// TODO Move into util rep
fun createFakeResponse(
    statusCode: HttpStatusCode
): HttpResponse {
    return object : HttpResponse() {
        override val call: HttpClientCall
            get() = TODO("Not yet implemented")
        override val content: ByteReadChannel
            get() = TODO("Not yet implemented")
        override val coroutineContext: CoroutineContext
            get() = TODO("Not yet implemented")
        override val headers: Headers
            get() = TODO("Not yet implemented")
        override val requestTime: GMTDate
            get() = TODO("Not yet implemented")
        override val responseTime: GMTDate
            get() = TODO("Not yet implemented")
        override val status: HttpStatusCode
            get() = statusCode
        override val version: HttpProtocolVersion
            get() = TODO("Not yet implemented")

        override fun toString(): String {
            return "Fake Response"
        }
    }
}
