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

            Contract.Service.ALP -> // TODO update with correct ALP test key
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWYsUPv7etCQYYhMtwkP" +
                        "xGH7144My0yUnqCmF38w40S7CCd54fa1zhijyvAEU67gMgxesyi2bMHPQJp2E63f" +
                        "g/0IcY4kY//9NrtWY7QovOJaFa8ov+wiIbKa3Y5zy4sxq8VoBJlr1EBYaQNX6I9f" +
                        "NG+IcQlkoTTqL+qt7lYsW0P4H3vR/92HHaJjA+yvSbXhePMh2IN4ESTqbBSSwWfd" +
                        "AHtFlH63hV65EB0pUudPumWpUrJWYczveoUO3XUU4qmJ7lZU0kTUFBwwfdeprZtG" +
                        "nEgS+ZIQAp4Y9BId1Ris5XgZDwmMYF8mB1sqGEnbQkmkaMPoboeherMio0Z/PD6J" +
                        "rQIDAQAB"
        }

        override fun getDonorKeyPair(): KeyPair? = null

        override fun getUserSessionToken(tokenListener: ResultListener<String>) {
            tokenListener.onSuccess(
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcjoyYmVkNjZmZC1hMjkwLTRiNWUtODRlMC0zMmFiNTNmNjJkNDYiLCJpc3MiOiJ1cm46Z2hjIiwiZXhwIjoxNjAzMjAyODc1LCJuYmYiOjE2MDMyMDI0NTUsImlhdCI6MTYwMzIwMjUxNSwianRpIjoiYWRjYzI2Y2EtMzdlMS00Y2NkLTlhMjUtMmI4NzFlODFmZWRjIiwiZ2hjOmFpZCI6ImJjOTg2ZDEzLTg1Y2EtNGZhYy04ZTQyLTNlMWY2MjI4NDdiNSIsImdoYzpjaWQiOiI4OWRiYzg3Ni1hYzdjLTQzYjctODc0MS0yNWIxNDA2NWZiOTEjYW5kcm9pZF9odWIiLCJnaGM6dWlkIjoiMmJlZDY2ZmQtYTI5MC00YjVlLTg0ZTAtMzJhYjUzZjYyZDQ2IiwiZ2hjOnNjb3BlIjoicGVybTpyIHJlYzpyIHJlYzp3IGF0dGFjaG1lbnQ6ciBhdHRhY2htZW50OncgdXNlcjpyIHVzZXI6cSJ9.MaVF4l6Xasg1f4wFT-ometDj6z7FKTRFOvdvsalmSTVPcEgXvIK84HZX8dF8Z3knACMaEFdcz7vmTF_BGkrjro3yU0Xod0HHtoqPkEexLs-DC-2vgThHYLgZlaajgvpins-Ei_qCHK71dCrZ2osVPp0XLVnt-X3zdbj84fr1mywyvQ7wwEcwyectBb50FGzaR39LOUvAam-sbsfMVGmflrNYRf2oHnOTIJ4HgAnN-zLk4j5JxK1AZqy1XMEtsacKKDBLdzi3WgU7SZhHXZA-9LN3k21PaxegeyU1-NpLGOELZ9CC0ezrBDy9saZUc8GlFjP-E2mP4ZqvWy2jOp4HUVRrppbHwN9z2a2QTvJf6X-2bx_I2jxzg0LsqzvQ8nk_TUZkIo071ls1MHi7Kn7MeLowWUBHZ4rsR-6puhtnHEKJoPZazguK-p_FrpFgOOHbZuksFrtUhcw64FTma6FzMpNtlQc6gvi_VQoZx8fqcPBfIpZ9r_sWLEDhjXtTkfPUNKjY6J0CiRu0_wlmErmBdAKwlk8oyoXiNz69VFm4y4qc3a02MPEtwoAnB0LD3D-lmVTr79uFC4vY-NtF5fjjk8zjygP4pznb-V3UC7l8s2BpyXKil8ckvx9FgmRI0WoKPeFqYh3NvS8U5C-P0T0Ca23x4_oUgTwEPlMDzMpXw1U"
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
}


