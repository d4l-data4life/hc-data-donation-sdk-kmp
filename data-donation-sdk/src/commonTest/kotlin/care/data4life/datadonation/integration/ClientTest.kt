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
import care.data4life.datadonation.core.listener.ListenerContract.Callback
import care.data4life.datadonation.core.listener.ListenerContract.ResultListener
import care.data4life.datadonation.core.listener.listenerModule
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.storage.storageModule
import care.data4life.datadonation.internal.di.coreModule
import care.data4life.datadonation.internal.di.platformModule
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.domain.repository.repositoryModule
import care.data4life.datadonation.internal.domain.usecases.usecaseModule
import care.data4life.datadonation.mock.DummyData
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.hl7.fhir.stu3.model.Reference
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import runWithBlockingTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// TODO: use MockEngine and make separated E2E tests
class ClientTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    @Ignore
    // TODO "Use MockEngine"
    // TODO: infinity loop caused by the refused connection - If so we need to figure out of setting a timeout
    fun fetchConsentDocumentTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentKey = "custom-consent-key"
        val language = "en"
        val version = 42

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair? = null
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)
        val capturedResult = Channel<List<ConsentDocument>>()
        val listener = object : ResultListener<List<ConsentDocument>> {
            override fun onSuccess(result: List<ConsentDocument>) {
                launch {
                    capturedResult.send(result)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        // When
        client.fetchConsentDocuments(
            version,
            language,
            consentKey,
            listener
        )

        // Then
        val result = capturedResult.receive()
        assertEquals(
            actual = result,
            expected = emptyList() // TODO use a actual response
        )
    }

    @Test
    @Ignore
    // TODO "Use MockEngine"
    // TODO: infinity loop caused by the refused connection - If so we need to figure out of setting a timeout
    fun createUserConsentTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentKey = "custom-consent-key"
        val language = "en"
        val version = 42

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair? = null
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)
        val capturedConsentDoc = Channel<List<ConsentDocument>>()
        val consentDocListener = object : ResultListener<List<ConsentDocument>> {
            override fun onSuccess(result: List<ConsentDocument>) {
                launch {
                    capturedConsentDoc.send(result)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        val capturedConsent = Channel<UserConsent>()
        val consentListener = object : ResultListener<UserConsent> {
            override fun onSuccess(result: UserConsent) {
                launch {
                    capturedConsent.send(result)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        // When
        client.fetchConsentDocuments(
            version,
            language,
            consentKey,
            consentDocListener
        )

        val consentDoc = capturedConsentDoc.receive().first()
        client.createUserConsent(
            consentDoc.version,
            consentDoc.language,
            consentListener
        )
        val result = capturedConsent.receive()

        // Then
        assertEquals(
            actual = result,
            expected = DummyData.userConsent // TODO
        )
    }

    @Test
    // TODO: No API Call?
    fun registerNewDonorTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val keyPair = KeyPair(ByteArray(42), ByteArray(23))

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair = keyPair
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)
        val capturedRegistrationKey = Channel<KeyPair>()
        val consentRegistrationKeyListener = object : ResultListener<KeyPair> {
            override fun onSuccess(result: KeyPair) {
                launch {
                    capturedRegistrationKey.send(result)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        // When
        client.registerDonor(consentRegistrationKeyListener)
        val result = capturedRegistrationKey.receive()

        // Then
        assertEquals(
            actual = result,
            expected = keyPair
        )
    }

    @Test
    @Ignore // TODO "Use MockEngine"
    // TODO: infinity loop caused by the refused connection - If so we need to figure out of setting a timeout
    fun fetchUserConsentsTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentKey = "custom-consent-key"

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair? = null
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)
        val capturedResult = Channel<List<UserConsent>>()
        val listener = object : ResultListener<List<UserConsent>> {
            override fun onSuccess(result: List<UserConsent>) {
                launch {
                    capturedResult.send(result)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }
        // When
        client.fetchUserConsents(listener, consentKey)
        val result = capturedResult.receive()
        // Then
        assertEquals(
            actual = result,
            expected = emptyList() // TODO
        )
    }

    @Test
    @Ignore
    // TODO: Use MockEngine
    // TODO: infinity loop caused by the refused connection - If so we need to figure out of setting a timeout
    fun revokeUserConsentsTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val language = "en"

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair? = null
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)
        val capturedResult = Channel<Boolean>()
        val callback = object : Callback {
            override fun onSuccess() {
                launch {
                    capturedResult.send(true)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        // When
        client.revokeUserConsent(language, callback)
        val result = capturedResult.receive()

        // Then
        assertTrue(result)
    }

    @Test
    // TODO: Workaround Signature Key
    @Ignore
    fun donateResourcesTest() = runWithBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val keyPair = KeyPair(
            "tomato".toByteArray(),
            "soup".toByteArray()
        )

        val config = object : ConfigurationBase() {
            override fun getDonorKeyPair(): KeyPair = keyPair
            override fun getCoroutineScope(): CoroutineScope = GlobalScope
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(config),
                platformModule(),
                coreModule(),
                listenerModule(config),
                storageModule(),
                usecaseModule(),
                repositoryModule()
            )
        }

        val client = Client(config, koin)

        val capturedResult = Channel<Boolean>()
        val callback = object : Callback {
            override fun onSuccess() {
                launch {
                    capturedResult.send(true)
                }
            }

            override fun onError(exception: Exception) = throw exception
        }

        val donation = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            id = "id",
            language = "en",
            questionnaire = Reference(id = "questionnaire_id"),
            item = listOf(
                QuestionnaireResponseItem(
                    linkId = "linkId",
                    text = "dummy text question",
                    answer = listOf(QuestionnaireResponseItemAnswer(valueString = "dummy answer"))
                )
            )
        )

        // When
        client.donateResources(
            listOf(donation),
            callback
        )
        val result = capturedResult.receive()
        // Then
        assertTrue(result)
    }

    private abstract class ConfigurationBase : Contract.Configuration {
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

        override fun getUserSessionToken(tokenListener: ResultListener<String>) {
            tokenListener.onSuccess(
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvd25lcjpmMmNiNDBhZS1lOGQxLTQzOTctOGIzZC1hMjRmODgwZTRjYmQiLCJpc3MiOiJ1cm46Z2hjIiwiZXhwIjoxNjExMTY3ODA3LCJuYmYiOjE2MTExNjczODcsImlhdCI6MTYxMTE2NzQ0NywianRpIjoiMWMxZjVhMmItZjA0Ny00NTc5LWE0YWItZDBjY2ZmOTVkMDEwIiwiZ2hjOmFpZCI6ImVmZGRiOTVhLWFkMTktNDczMS1iOWM5LThjZWMwOTg3MzQzZSIsImdoYzpjaWQiOiI4OWRiYzg3Ni1hYzdjLTQzYjctODc0MS0yNWIxNDA2NWZiOTEjYW5kcm9pZF9odWIiLCJnaGM6dWlkIjoiZjJjYjQwYWUtZThkMS00Mzk3LThiM2QtYTI0Zjg4MGU0Y2JkIiwiZ2hjOnRpZCI6ImQ0bCIsImdoYzpzY29wZSI6InBlcm06ciByZWM6ciByZWM6dyBhdHRhY2htZW50OnIgYXR0YWNobWVudDp3IHVzZXI6ciB1c2VyOnEifQ.JpjUvgAk-wCJAY8xu5J9CT93655lXqyy3dki0Gr0sWtU4PpCZePmeGHPABmBKdg3Bj8kdnbz2463OMfeGELigOLe1Mx8nPPPiGV2SPliCZp-rOOOodxscAd1OTT1AnCPmwJXlXHuNj4CUnR_Urr0dvpNt8a3CdwsezhgDwoyDQpKYPbMN-IrI1WcHaG8SCOPhiX-6zMVrnwpS6fBAtAtUbpvVQZXraKjdbiB3ZCoCWAMKG3_2TtamklOYFCPm1rladkptIFPVXR4y_6LoR_ILN77SERxrEERASvm1ZJXwd2EjLHKkabJ1EgIYiqntiCrRNawtYs0sjo4g_5il4nzUHZbrNWHo0c0m9Qzjahj3vJAQEddJcTZdJyM7_Ootmqwo3DTcn4w_xfIDoTArW5tHJ27TRBIo73PoOCAF4iZA4TGE9NassegeEtLl9jtKBDUr3MZVZkZEPZip8XWvX8E3UtIAVx-cji1hHK6P-jOzTZOCYJNCFx2C5ZK7zhVN9oWsImeiagViEe5OuxXWcFc38QE5eHBGjbOAKEGLchkuxk6zX2HhycJEW6JT73Vcf8iYytLF596SQ_uY4O2m1cr8HshFc10mAhGgnMhqHhe1QUIWlwFe5HHz7P4LTeK7zP8iAAVgEqlx7X1ryvpw5ekz7p1hbLYB9hKvvJ-vx13HkI"
            )
        }

        override fun getEnvironment() = Environment.LOCAL
    }
}
