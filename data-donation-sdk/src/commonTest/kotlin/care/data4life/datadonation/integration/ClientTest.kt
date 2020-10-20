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

package care.data4life.datadonation.integration

import care.data4life.datadonation.Client
import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.listener.Callback
import care.data4life.datadonation.core.listener.ResultListener
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.core.model.UserConsent
import runTest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

open class ClientTest {

    private val language = "en"

    val client = Client(object : Contract.Configuration {
        override fun getServicePublicKey(): String =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWYsUPv7etCQYYhMtwkP" +
                    "xGH7144My0yUnqCmF38w40S7CCd54fa1zhijyvAEU67gMgxesyi2bMHPQJp2E63f" +
                    "g/0IcY4kY//9NrtWY7QovOJaFa8ov+wiIbKa3Y5zy4sxq8VoBJlr1EBYaQNX6I9f" +
                    "NG+IcQlkoTTqL+qt7lYsW0P4H3vR/92HHaJjA+yvSbXhePMh2IN4ESTqbBSSwWfd" +
                    "AHtFlH63hV65EB0pUudPumWpUrJWYczveoUO3XUU4qmJ7lZU0kTUFBwwfdeprZtG" +
                    "nEgS+ZIQAp4Y9BId1Ris5XgZDwmMYF8mB1sqGEnbQkmkaMPoboeherMio0Z/PD6J" +
                    "rQIDAQAB"

        override fun getDonorKeyPair(): KeyPair? = null

        override fun getUserSessionToken(): String? =
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcjoyYmVkNjZmZC1hMjkwLTRiNWUtODRlMC0zMmFiNTNmNjJkNDYiLCJpc3MiOiJ1cm46Z2hjIiwiZXhwIjoxNjAzMDkwMDMyLCJuYmYiOjE2MDMwODk2MTIsImlhdCI6MTYwMzA4OTY3MiwianRpIjoiOGY4MGEyNWMtZWQzNy00NGQ2LTg5NmYtMjI2YmI2YjA1N2NhIiwiZ2hjOmFpZCI6ImJjOTg2ZDEzLTg1Y2EtNGZhYy04ZTQyLTNlMWY2MjI4NDdiNSIsImdoYzpjaWQiOiI4OWRiYzg3Ni1hYzdjLTQzYjctODc0MS0yNWIxNDA2NWZiOTEjYW5kcm9pZF9odWIiLCJnaGM6dWlkIjoiMmJlZDY2ZmQtYTI5MC00YjVlLTg0ZTAtMzJhYjUzZjYyZDQ2IiwiZ2hjOnNjb3BlIjoicGVybTpyIHJlYzpyIHJlYzp3IGF0dGFjaG1lbnQ6ciBhdHRhY2htZW50OncgdXNlcjpyIHVzZXI6cSJ9.pceV8eu8YCyVK_oLLJhkiq7j1Y1Uby8IF3yjQcOEYmGZrtGI5n4ePPPDGmhPHdVw8mBgxYtXPXImkzgKRx3XXGL5v_YZKyIYgP5hTE9YJXeebzSkz9UWp6OyXtfqNO9AOoGy5CW6p--xJ_EDkgV5WUxTGTjOTMItwqcKcjgkfowVxb7rcSTo0NHbkeZi-YewL4-TEz1a0x4OI3q-7csXw70f47YuDgbWmtyAFlSBAnolaRxjitopstcJU9yGBkexf51guk3s1mszNaNIbtOT7xDHj3KVOxgL23wQE3s0SMGiE9-s6I7OITY5zEHiPh1eAn9pNj3Czg7Fe3UkEyecnKlo6O5OQ_a7bOfzT64L1MmI8mCgPS1a4jREnnjMlhGXDxMz5H5Y2YPP0zCmlpT_wvFf4GY_zFDm-3NKPiG_40HEhSpzvmoaLEd691pVQ4X_3zbazL7v-4n2WqQnQHZgEXnRdYU1Re8AIx59Xve211Z4cTRCrJ9GPCkeuVj-t80fUPV-ddUQPuUBXhZ7D6OVBbp2t_Q0qZtLxme_Zq5-ZcLVb0SJ08mimcFvxMdbmXKB585tF_ilRVfF61xF97noxpW2AkE_ES1sjsHINyCEiRC0MmDErxMhyQ2rYSHL7UIJ32ue8C1OIXJPnxsDc3bI6RBKCCM4LIggftRcjPR9Vxk"

        override fun getEnvironment() = Environment.LOCAL

    })

    @Ignore
    @Test
    fun fetchConsentDocumentTest() = runTest {

        //When
        val result = fetchConsentDocument(null, language)

        //Then
        assertEquals(1, result.size)
    }


    @Ignore
    @Test
    fun createUserConsentTest() = runTest {
        //Given
        val consentDocument = fetchConsentDocument(null, language).first()

        //When
        val result = createUserConsent(consentDocument.version, consentDocument.language)
        //Then
        println("UserConsent ${result.first}")
        println("KeyPair ${result.second}")
    }

    @Test
    @Ignore
    fun fetchUserConsentsTest() = runTest {
        //Given

        //When
        val result = fetchUserConsent()
        //Then
        println(result)
    }

    @Test
    @Ignore
    fun revokeUserConsentsTest() = runTest {
        //Given

        //When
        revokeUserConsent(language)
        //Then
    }

    private suspend fun fetchConsentDocument(
        consentDucomentVersion: Int?,
        language: String?
    ): List<ConsentDocument> =
        suspendCoroutine { continuation ->
            client.fetchConsentDocument(
                consentDucomentVersion,
                language,
                object : ResultListener<List<ConsentDocument>> {
                    override fun onSuccess(t: List<ConsentDocument>) {
                        continuation.resume(t)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                })
        }

    private suspend fun createUserConsent(
        consentDocumentVersion: Int,
        language: String?
    ): Pair<UserConsent, KeyPair> =
        suspendCoroutine { continuation ->
            client.createUserConsent(
                consentDocumentVersion,
                language,
                object : ResultListener<Pair<UserConsent, KeyPair>> {
                    override fun onSuccess(t: Pair<UserConsent, KeyPair>) {
                        continuation.resume(t)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                })
        }


    private suspend fun fetchUserConsent(): List<UserConsent> =
        suspendCoroutine { continuation ->
            client.fetchUserConsents(
                object : ResultListener<List<UserConsent>> {
                    override fun onSuccess(t: List<UserConsent>) {
                        continuation.resume(t)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                })
        }

    private suspend fun revokeUserConsent(language: String?) =
        suspendCoroutine<Unit> { continuation ->
            client.revokeUserConsent(language,
                object : Callback {
                    override fun onSuccess() {
                        continuation.resume(Unit)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                })
        }
}


