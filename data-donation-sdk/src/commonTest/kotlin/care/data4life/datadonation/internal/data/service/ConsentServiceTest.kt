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

import care.data4life.datadonation.internal.data.model.TokenVerificationResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.config
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class ConsentServiceTest {

    private lateinit var service: ConsentService
    private lateinit var lastRequest : HttpRequestData
    private val tokenVerificationResult = TokenVerificationResult("StudyId","externalId","")

    @Test
    fun createUserConsentTest() = runTest {
        //Given
        givenConsentServiceResponseWith(TokenVerificationResult.serializer(), tokenVerificationResult)

        //When
        val result = service.createUserConsent("1",null)

        //Then
        assertEquals(result,tokenVerificationResult)
        assertEquals(lastRequest.method, HttpMethod.Post)
    }

    private fun <T> givenConsentServiceResponseWith(strategy: SerializationStrategy<T>, response : T) {
        val engine= MockEngine.config {
            addHandler { request ->
                lastRequest  = request
                respond(Json(JsonConfiguration.Stable).stringify(strategy, response),
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString())) }
        }
        service= ConsentService(HttpClient(engine){
            install(JsonFeature){
                serializer = KotlinxSerializer(Json(JsonConfiguration.Stable))
                accept(ContentType.Application.Json)
            }
        })
    }

}
