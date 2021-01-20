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
import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.internal.utils.DefaultKeyGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import runTest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

open class ClientTest {

    private val language = "en"
    private val keyPair = DefaultKeyGenerator.newSignatureKeyPrivate(
        2048,
        Algorithm.Signature.RsaPSS(HashSize.Hash256)
    ).let { KeyPair(it.serializedPublic(), it.serializedPrivate()) }

    val client = Client(object : Contract.Configuration {
        override fun getServicePublicKey(service: Contract.Service): String = when (service) {
            Contract.Service.DD ->
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWYsUPv7etCQYYhMtwkP" +
                        "xGH7144My0yUnqCmF38w40S7CCd54fa1zhijyvAEU67gMgxesyi2bMHPQJp2E63f" +
                        "g/0IcY4kY//9NrtWY7QovOJaFa8ov+wiIbKa3Y5zy4sxq8VoBJlr1EBYaQNX6I9f" +
                        "NG+IcQlkoTTqL+qt7lYsW0P4H3vR/92HHaJjA+yvSbXhePMh2IN4ESTqbBSSwWfd" +
                        "AHtFlH63hV65EB0pUudPumWpUrJWYczveoUO3XUU4qmJ7lZU0kTUFBwwfdeprZtG" +
                        "nEgS+ZIQAp4Y9BId1Ris5XgZDwmMYF8mB1sqGEnbQkmkaMPoboeherMio0Z/PD6J" +
                        "rQIDAQAB"

            Contract.Service.ALP ->
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t" +
                        "+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7" +
                        "/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl" +
                        "+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwV" +
                        "I1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t0" +
                        "0G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/" +
                        "1QIDAQAB"
        }

        override fun getDonorKeyPair(): KeyPair = keyPair

        override fun getUserSessionToken(tokenListener: ResultListener<String>) {
            tokenListener.onSuccess(
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcjpmMmNiNDBhZS1lOGQxLTQzOTctOGIzZC1hMjRmODgwZTRjYmQiLCJpc3MiOiJ1cm46Z2hjIiwiZXhwIjoxNjExMTYzOTMzLCJuYmYiOjE2MTExNjM1MTMsImlhdCI6MTYxMTE2MzU3MywianRpIjoiMDA3MzA5YzAtYTc1ZS00N2JkLWIyMGUtMTJiMmM3OWI1YTlmIiwiZ2hjOmFpZCI6ImVmZGRiOTVhLWFkMTktNDczMS1iOWM5LThjZWMwOTg3MzQzZSIsImdoYzpjaWQiOiI4OWRiYzg3Ni1hYzdjLTQzYjctODc0MS0yNWIxNDA2NWZiOTEjYW5kcm9pZF9odWIiLCJnaGM6dWlkIjoiZjJjYjQwYWUtZThkMS00Mzk3LThiM2QtYTI0Zjg4MGU0Y2JkIiwiZ2hjOnRpZCI6ImQ0bCIsImdoYzpzY29wZSI6InBlcm06ciByZWM6ciByZWM6dyBhdHRhY2htZW50OnIgYXR0YWNobWVudDp3IHVzZXI6ciB1c2VyOnEifQ.D9OZg6TvXAGmiHTNdO8J3pq2exhUhFGulDG3RVug8h2Vx6ZfbdXgtIjc8RbnLqTwXIh58CIOBwbEia4BlasXoqOk0v37K30bpBqXwKUpZ6lX92S2tYaSu1WOE7RwpgMHaRF_uiUcPmewKGucWs4cfxdQ3oprVHmrzyfFBY_GFIR5BKaCDPx3Cc07VxMhG7vJQszzuushmaxsCzvH_6wi3FkG7WYOHgz5I6hMbj-R-d-bOrk8Hpx4LabncqHOfeuoXTM0y-Ll-NTIzef3aso0a29KUz--reTgsGFawLwNSSW6Ml_2WpJpnlpEzN-HYX1mhAJY6mNXYAx-RYhrXvZyR2RDDEPwsWrWrY8qDeVHrxSUkCfWGkJGnTJczdAL2FQuVYc0atwr2MeoKiiseJ_b6i4sFshTbuEX9RaFD8oeTkTNc0w8EwQjk3rHg92osdA5Z-loSdWHZxFpChXURTojnup17huMKAvDoP_RKC7CmJ7VON7IwKb9GgmImu-c6PxahhRMIMOj25Tx1wmW0lgF6xe3TFimJRWrYmcf4micWdFpE7vwkTQHC_zwr0IkaJH6lTqwRJclkh-jcB2KJB3Ngweu7auJ3Y6Vi0S7kiKAW361ciAQSuDv7YgM12lgkPAOzn_EaUc0l2HsQOIpD_u16KHo5CYDNgw1yKCSFGE0CeU"
            )
        }

        override fun getEnvironment() = Environment.LOCAL
        override fun getCoroutineContext(): CoroutineScope = GlobalScope

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
        println("UserConsent $result")
    }


    @Ignore
    @Test
    fun registerNewDonorTest() = runTest {
        //Given
        createUserConsentTest()

        //When
        val result = register()
        //Then
        println("Keypair $result")
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

    @Ignore
    @Test
    fun revokeUserConsentsTest() = runTest {
        //Given

        //When
        revokeUserConsent(language)
        //Then
    }

    @Ignore
    @Test
    fun donateResourcesTest() = runTest {
        //Given
        registerNewDonorTest()
        //When
        donateResources(listOf("donated_resource"))
        //Then
    }

    private suspend fun fetchConsentDocument(
        consentDocumentVersion: Int?,
        language: String?
    ): List<ConsentDocument> =
        suspendCoroutine { continuation ->
            client.fetchConsentDocument(
                consentDocumentVersion,
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
    ): UserConsent =
        suspendCoroutine { continuation ->
            client.createUserConsent(
                consentDocumentVersion,
                language,
                object : ResultListener<UserConsent> {
                    override fun onSuccess(t: UserConsent) {
                        continuation.resume(t)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resumeWithException(exception)
                    }
                })
        }

    private suspend fun register(): KeyPair =
        suspendCoroutine { continuation ->
            client.registerDonor(
                object : ResultListener<KeyPair> {
                    override fun onSuccess(t: KeyPair) {
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

    private suspend fun donateResources(resources: List<String>) =
        suspendCoroutine<Unit> { continuation ->
            client.donateResources(resources,
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


