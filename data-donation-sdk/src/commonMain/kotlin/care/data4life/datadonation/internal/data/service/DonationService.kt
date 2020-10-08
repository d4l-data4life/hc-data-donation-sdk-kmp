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
import care.data4life.datadonation.internal.data.service.DonationService.Endpoints.register
import care.data4life.datadonation.internal.utils.CommonBase64Encoder
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class DonationService(
    private val client: HttpClient,
    private val environment: Environment
) {

    private val baseUrl = if (environment == Environment.LOCAL) {
        "${environment.url}:9090/api/v1"
    } else {
        "${environment.url}/donation/api/v1"
    }

    suspend fun requestRegistrationToken(): String {
        return client.getWithQuery(environment, baseUrl = baseUrl, path = Endpoints.token)
    }

    suspend fun registerNewDonor(payload: ByteArray) {
        println("BODY: ${payload.size} ${CommonBase64Encoder.encode(payload)}")
        return client.putWithoutHeader(
            environment,
            baseUrl = baseUrl,
            path = register,
            body = ByteArrayContent(payload)
        )
    }

    object Endpoints {
        const val token = "token"
        const val register = "register"
    }
}
