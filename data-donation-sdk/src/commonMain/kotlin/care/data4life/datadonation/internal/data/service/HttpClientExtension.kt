/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.core.model.Environment
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend inline fun <reified T> HttpClient.getWithQuery(
    environment: Environment,
    accessToken: String? = null,
    baseUrl: String,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): T =
    get(
        scheme = if (environment == Environment.LOCAL) "http" else "https",
        host = baseUrl, path = path
    ) {
        if (accessToken != null) {
            header(
                "Authorization", "Bearer $accessToken"
            )
        }
        contentType(ContentType.Application.Json)
        apply(block)
    }

suspend inline fun <reified T> HttpClient.putWithBody(
    environment: Environment,
    accessToken: String? = null,
    baseUrl: String,
    path: String,
    body: Any,
    contentType: ContentType = ContentType.Application.Json,
    block: HttpRequestBuilder.() -> Unit = {}
): T = put(
    scheme = if (environment == Environment.LOCAL) "http" else "https",
    host = baseUrl, path = path
) {
    if (accessToken != null) {
        header(
            "Authorization", "Bearer $accessToken"
        )
    }
    contentType(contentType)
    this.body = body
    apply(block)
}

suspend inline fun <reified T> HttpClient.putWithoutHeader(
    environment: Environment,
    accessToken: String? = null,
    baseUrl: String,
    path: String,
    body: Any,
    block: HttpRequestBuilder.() -> Unit = {}
): T = put(
    scheme = if (environment == Environment.LOCAL) "http" else "https",
    host = baseUrl, path = path
) {
    if (accessToken != null) {
        header(
            "Authorization", "Bearer $accessToken"
        )
    }
    this.body = body
    apply(block)
}

suspend inline fun <reified T> HttpClient.postWithoutContentType(
    environment: Environment,
    accessToken: String,
    baseUrl: String,
    path: String,
    body: Any,
    block: HttpRequestBuilder.() -> Unit = {}
): T = post(
    scheme = if (environment == Environment.LOCAL) "http" else "https",
    host = baseUrl, path = path
) {
    header("Authorization", "Bearer $accessToken")
    contentType(ContentType.Application.Json)
    this.body = body
    apply(block)
}

suspend inline fun <reified T> HttpClient.postWithoutContentType(
    environment: Environment,
    baseUrl: String,
    path: String,
    body: Any,
    block: HttpRequestBuilder.() -> Unit = {}
): T = post(
    scheme = if (environment == Environment.LOCAL) "http" else "https",
    host = baseUrl, path = path
) {
    this.body = body
    apply(block)
}

suspend inline fun <reified T> HttpClient.deleteWithBody(
    environment: Environment,
    accessToken: String,
    baseUrl: String,
    path: String,
    body: Any,
    block: HttpRequestBuilder.() -> Unit = {}
): T = delete(
    scheme = if (environment == Environment.LOCAL) "http" else "https",
    host = baseUrl, path = path
) {
    header("Authorization", "Bearer $accessToken")
    contentType(ContentType.Application.Json)
    this.body = body
    apply(block)
}
