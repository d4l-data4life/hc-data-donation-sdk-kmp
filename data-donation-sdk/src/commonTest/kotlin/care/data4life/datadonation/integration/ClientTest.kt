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
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqwTD2GY3SClkbmDVtgIE" +
                    "it7RGK1MqK8G1n6f7N7qixBTzTbtHCG0Z/gS7fGhZ3PlOJJZdrQakNvtNAk91i05" +
                    "IffIo1Uw3501R3wvv7KJ8NtT5EIvHL8/7nZBTl+M4+FwFS60VGS3C0ZRWuuNe8SP" +
                    "yuVrf/0ZYfI+jXVzhdUJ3qkm11GPQ4jSVKivDRISkPBgEXk36lm9+IsqC6Fk3dsv" +
                    "eON0/CEMpxvpTBWu5vc3t6qNAWO0qWVVc0XcJt9nKsTi1D75TC5JuvujpgLOKnJj" +
                    "Wt1u6JeH+bnf828NsURM/HJjU8iYksFkEateoX59qbqadWyaTmBU7gb406l2CdwF" +
                    "JQIDAQAB"

        override fun getDonorKeyPair(): KeyPair? = null

        override fun getUserSessionToken(): String? =
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcjoyYmVkNjZmZC1hMjkwLTRiNWUtODRlMC0zMmFiNTNmNjJkNDYiLCJpc3MiOiJ1cm46Z2hjIiwiZXhwIjoxNjAxMzA4ODg4LCJuYmYiOjE2MDEzMDg0NjgsImlhdCI6MTYwMTMwODUyOCwianRpIjoiZTcwMTA5MjUtNzAyNC00MmQ4LTkyZWItZTM2MDcyYTNkNGMxIiwiZ2hjOmFpZCI6IjY5M2FhNmNlLWU0MjctNDUwNC05NGEzLWRiNDliYzU4NWRiYiIsImdoYzpjaWQiOiI4OWRiYzg3Ni1hYzdjLTQzYjctODc0MS0yNWIxNDA2NWZiOTEjYW5kcm9pZF9odWIiLCJnaGM6dWlkIjoiMmJlZDY2ZmQtYTI5MC00YjVlLTg0ZTAtMzJhYjUzZjYyZDQ2IiwiZ2hjOnNjb3BlIjoicGVybTpyIHJlYzpyIHJlYzp3IGF0dGFjaG1lbnQ6ciBhdHRhY2htZW50OncgdXNlcjpyIHVzZXI6cSJ9.YkJ-7IYq7-TnEJeKlaGBykCqfy2zVAwVN8aflXRqFCMhfpq7rh0OpZ6ZUHOSZY3G0CxZ2oYjpfwGjY4AZgq_G8540ZK089BW5YvmEC0XokbCLmp-khPhQTd0qvBsVQ6UKxqcP1tH7I_E7NOdrWT5ozv_e4Bx8xRPl-Qe5WaDn6mvekmmpoQloB5Fr2_kM28lENuduuZO5NaT2PK5gXI92QYpTJIWx0oeXWPd9kwumqJS7cCy_IiI4TjgIxxXFrIuqZchhaGPftNXg8yQ2ODmYYGvLKOJOqzFcetsjAvHXa1m7EvRW9Ik5Mlb434lOTBfv3N3VRjCHD6an8eFTZZWjnL0gOiEFS1TdBCxT0LjCDktYGm_jsceyOLxy2IpXryez_vw4ibuoIa1qtmSeY3Z3NWUlvcICk9iGAn0XAjGnWk5xsOhR2on69DH7iMRTZeFDfBWFH6ONtTZga4WUWuUcu-F-qjTb2En-xj9l7sDV_L6zuC8VKQiBFG4Xf03jEuzgM3NC_5ROxciYLxUNC0KJzHJ2DYV54WqN3qYzJxEXBkqmxukamSENHQBFEekJh9Dax3ECaT6qhWy-W_81I87RNtcZOUwcGdxyl5xgFTp0F7mzp8SkZT5159X1pklzs7963HBpXjluZ8hlsNnwV7J9bCsaOzusuhd9g3bbrsmp-Y"

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


