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
import care.data4life.datadonation.DataDonationSDKPublicAPI
import care.data4life.datadonation.internal.data.service.ServiceContract.UserSessionTokenService.Companion.CACHE_LIFETIME_IN_SECONDS
import care.data4life.datadonation.internal.data.service.resolveServiceModule
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.domain.repository.resolveRepositoryModule
import care.data4life.datadonation.internal.domain.usecases.resolveUsecaseModule
import care.data4life.datadonation.lang.ConsentServiceError
import care.data4life.datadonation.mock.ResourceLoader
import care.data4life.datadonation.mock.fixture.ConsentFixtures
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.networking.plugin.resolveKtorPlugins
import care.data4life.datadonation.networking.resolveNetworking
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createMockClientWithResponse
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClientConsentFlowModuleTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given fetchConsentDocuments is called with its appropriate parameter, it returns a List of ConsentDocument`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentDocumentKey = "potato"
        val language = "en"
        val version = "42"

        val httpClient = createMockClientWithResponse { scope, request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/consent/api/v1/consentDocuments?key=$consentDocumentKey&version=$version&language=$language"
            )
            assertEquals(
                actual = request.headers,
                expected = headersOf(
                    "Authorization" to listOf("Bearer ${UserSessionTokenProvider.sessionToken}"),
                    "Accept" to listOf("application/json"),
                    "Accept-Charset" to listOf("UTF-8")
                )
            )

            scope.respond(
                content = ResourceLoader.loader.load("/fixture/consent/ConsentDocuments.json"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    "Content-Type" to listOf("application/json")
                )
            )
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(
                    DataDonationSDKPublicAPI.Environment.DEV,
                    UserSessionTokenProvider
                ),
                resolveNetworking(),
                resolveKtorPlugins(),
                resolveUsecaseModule(),
                resolveRepositoryModule(),
                resolveServiceModule(),
                module {
                    factory(
                        override = true,
                        qualifier = named("blankHttpClient")
                    ) { httpClient }
                }
            )
        }

        val client = Client(koin)

        // When
        client.fetchConsentDocuments(
            consentDocumentKey,
            version,
            language,
        ).ktFlow.collect { result ->
            // Then
            assertEquals(
                actual = result,
                expected = listOf(ConsentFixtures.sampleConsentDocument)
            )
        }
    }

    @Test
    fun `Given fetchConsentDocuments is called with its appropriate parameter, it propagates Errors`() {
        // Given
        val consentDocumentKey = "tomato"
        val language = "en"
        val version = "42"
        val result = Channel<Any> { }

        val httpClient = createMockClientWithResponse { scope, _ ->
            scope.respond(
                content = "potato",
                status = HttpStatusCode.InternalServerError
            )
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(
                    DataDonationSDKPublicAPI.Environment.DEV,
                    UserSessionTokenProvider
                ),
                resolveNetworking(),
                resolveKtorPlugins(),
                resolveUsecaseModule(),
                resolveRepositoryModule(),
                resolveServiceModule(),
                module {
                    factory(
                        override = true,
                        qualifier = named("blankHttpClient")
                    ) { httpClient }
                }
            )
        }

        // When
        val client = Client(koin)
        val job = client.fetchConsentDocuments(
            consentDocumentKey,
            version,
            language,
        ).subscribe(
            scope = GlobalScope,
            onEach = {},
            onError = { error ->
                GlobalScope.launch {
                    result.send(error)
                }
            },
            onComplete = {}
        )

        runBlockingTest {
            job.join()

            // Then
            assertTrue(result.receive() is ConsentServiceError.InternalServer)
        }
    }

    @Test
    fun `Given fetchUserConsents is called with a consentDocumentKey it returns a List of UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentDocumentKey = "salt"

        val httpClient = createMockClientWithResponse { scope, request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/consent/api/v1/userConsents?latest=false&consentDocumentKey=$consentDocumentKey"
            )
            assertEquals(
                actual = request.headers,
                expected = headersOf(
                    "Authorization" to listOf("Bearer ${UserSessionTokenProvider.sessionToken}"),
                    "Accept" to listOf("application/json"),
                    "Accept-Charset" to listOf("UTF-8")
                )
            )

            scope.respond(
                content = ResourceLoader.loader.load("/fixture/consent/UserConsents.json"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    "Content-Type" to listOf("application/json")
                )
            )
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(
                    DataDonationSDKPublicAPI.Environment.DEV,
                    UserSessionTokenProvider
                ),
                resolveNetworking(),
                resolveKtorPlugins(),
                resolveUsecaseModule(),
                resolveRepositoryModule(),
                resolveServiceModule(),
                module {
                    factory(
                        override = true,
                        qualifier = named("blankHttpClient")
                    ) { httpClient }
                }
            )
        }

        val client = Client(koin)

        // When
        client.fetchUserConsents(consentDocumentKey).ktFlow.collect { result ->
            // Then
            assertEquals(
                actual = result,
                expected = listOf(ConsentFixtures.sampleUserConsent)
            )
        }
    }

    @KtorExperimentalAPI
    @Test
    fun `Given createUserConsent is called with a consentDocumentKey and a consentDocumentVersion, it returns a UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentDocumentKey = "pepper"
        val version = "23"

        val httpClient = createMockClientWithResponse { scope, request ->
            // Then
            if (request.method == HttpMethod.Post) {
                assertEquals(
                    actual = request.url.fullPath,
                    expected = "/consent/api/v1/userConsents"
                )
                assertEquals(
                    actual = request.headers,
                    expected = headersOf(
                        "Authorization" to listOf("Bearer ${UserSessionTokenProvider.sessionToken}"),
                        "Accept" to listOf("application/json"),
                        "Accept-Charset" to listOf("UTF-8")
                    )
                )
                assertEquals(
                    actual = request.body.contentType.toString(),
                    expected = "application/json"
                )
                launch {
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = "{\"consentDocumentKey\":\"$consentDocumentKey\",\"consentDocumentVersion\":\"$version\",\"consentDate\":\"1970-01-01T00:01:30Z\"}"
                    )
                }
                scope.respond(
                    content = "",
                    status = HttpStatusCode.OK,
                )
            } else {
                assertEquals(
                    actual = request.url.fullPath,
                    expected = "/consent/api/v1/userConsents?latest=false"
                )
                assertEquals(
                    actual = request.headers,
                    expected = headersOf(
                        "Authorization" to listOf("Bearer ${UserSessionTokenProvider.sessionToken}"),
                        "Accept" to listOf("application/json"),
                        "Accept-Charset" to listOf("UTF-8")
                    )
                )

                scope.respond(
                    content = ResourceLoader.loader.load("/fixture/consent/UserConsents.json"),
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        "Content-Type" to listOf("application/json")
                    )
                )
            }
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(
                    DataDonationSDKPublicAPI.Environment.DEV,
                    UserSessionTokenProvider
                ),
                resolveNetworking(),
                resolveKtorPlugins(),
                resolveUsecaseModule(),
                resolveRepositoryModule(),
                resolveServiceModule(),
                module {
                    factory(
                        override = true,
                        qualifier = named("blankHttpClient")
                    ) { httpClient }
                    single<Clock>(override = true) {
                        ClockStub().also {
                            it.whenNow = { Instant.fromEpochSeconds(CACHE_LIFETIME_IN_SECONDS.toLong() + 30) }
                        }
                    }
                }
            )
        }

        val client = Client(koin)

        // When
        client.createUserConsent(
            consentDocumentKey,
            version,
        ).ktFlow.collect { result ->
            // Then
            assertEquals(
                actual = result,
                expected = ConsentFixtures.sampleUserConsent
            )
        }
    }

    @KtorExperimentalAPI
    @Test
    fun `Given revokeUserConsents is called with consentDocumentKey it just runs`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentDocumentKey = "water"

        val httpClient = createMockClientWithResponse { scope, request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/consent/api/v1/userConsents"
            )
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Delete
            )
            launch {
                assertEquals(
                    actual = request.body.toByteReadPacket().readText(),
                    expected = "{\"consentDocumentKey\":\"$consentDocumentKey\"}"
                )
            }
            assertEquals(
                actual = request.headers,
                expected = headersOf(
                    "Authorization" to listOf("Bearer ${UserSessionTokenProvider.sessionToken}"),
                    "Accept" to listOf("application/json"),
                    "Accept-Charset" to listOf("UTF-8")
                )
            )

            scope.respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val koin = koinApplication {
            modules(
                resolveRootModule(
                    DataDonationSDKPublicAPI.Environment.DEV,
                    UserSessionTokenProvider
                ),
                resolveNetworking(),
                resolveKtorPlugins(),
                resolveUsecaseModule(),
                resolveRepositoryModule(),
                resolveServiceModule(),
                module {
                    factory(
                        override = true,
                        qualifier = named("blankHttpClient")
                    ) { httpClient }
                }
            )
        }

        val client = Client(koin)

        // When
        client.revokeUserConsent(consentDocumentKey).ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = Unit
            )
        }
    }

    private object UserSessionTokenProvider : DataDonationSDKPublicAPI.UserSessionTokenProvider {
        const val sessionToken = "sessionToken"

        override fun getUserSessionToken(
            onSuccess: (sessionToken: String) -> Unit,
            onError: (error: Exception) -> Unit
        ) { onSuccess(sessionToken) }
    }
}
