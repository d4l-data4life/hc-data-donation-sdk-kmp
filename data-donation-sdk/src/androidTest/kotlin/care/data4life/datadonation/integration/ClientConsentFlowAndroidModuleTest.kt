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
import care.data4life.datadonation.internal.data.service.networking.plugin.resolveKtorPlugins
import care.data4life.datadonation.internal.data.service.networking.resolveNetworking
import care.data4life.datadonation.internal.data.service.resolveServiceModule
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.domain.repository.resolveRepositoryModule
import care.data4life.datadonation.internal.domain.usecases.resolveUsecaseModule
import care.data4life.datadonation.lang.ConsentServiceError
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createMockClientWithResponse
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientConsentFlowAndroidModuleTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given fetchConsentDocuments is called with its appropriate parameter, it propagates Errors`() {
        // Given
        val consentDocumentKey = "tomato"
        val language = "en"
        val version = "42"

        val capturedError = Channel<Throwable>()

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
        Client(koin).fetchConsentDocuments(
            consentDocumentKey,
            version,
            language,
        ).ktFlow.catch { delegatedError ->
            GlobalScope.launch {
                capturedError.send(delegatedError)
            }
        }.launchIn(GlobalScope)

        runBlockingTest {
            // Then
            assertTrue(capturedError.receive() is ConsentServiceError.InternalServer)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun `Given revokeUserConsents is called with consentDocumentKey it fails at unexpected Response`() {
        // Given
        val consentDocumentKey = "water"

        val capturedError = Channel<Throwable>()

        val httpClient = createMockClientWithResponse { scope, _ ->
            // Then
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

        // When
        Client(koin).revokeUserConsent(consentDocumentKey)
            .ktFlow.catch { delegatedError ->
                GlobalScope.launch {
                    capturedError.send(delegatedError)
                }
            }.launchIn(GlobalScope)

        runBlockingTest {
            // Then
            assertTrue(capturedError.receive() is ConsentServiceError.UnexpectedFailure)
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
