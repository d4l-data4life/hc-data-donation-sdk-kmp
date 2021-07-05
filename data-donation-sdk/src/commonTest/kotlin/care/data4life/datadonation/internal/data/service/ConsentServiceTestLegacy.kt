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

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.ConsentEvent
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.model.TokenVerificationResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.serialization.builtins.ListSerializer
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal abstract class ConsentServiceTestLegacy : BaseServiceTest<ConsentServiceLegacy>() {

    private val tokenVerificationResult = TokenVerificationResult("StudyId", "externalId", "")
    private val userConsent = UserConsent("key", "version", "accountId", ConsentEvent.Consent, "0")
    private val consentSignature = ConsentSignature("signature")
    private var consentDocDummy = ConsentDocument("", 1, "", "", "", "en", "", true, "", "")

    override fun getService(httpClient: HttpClient, environment: Environment) =
        ConsentServiceLegacy(httpClient, environment)

    @Test
    fun createUserConsentTest() = runBlockingTest {
        // Given
        givenServiceToResponse(
            Pair(
                "xsrf",
                { respond("", headers = headersOf("X-Csrf-Token", "anyThing")) }
            ),
            Pair(
                "userConsents",
                {
                    responseWith(
                        TokenVerificationResult.serializer(),
                        tokenVerificationResult
                    )
                }
            ),
            contentType = ContentType.Application.OctetStream
        )

        // When
        service.createUserConsent("T", 1, null)

        // Then
        assertEquals(HttpMethod.Post, lastRequest.method)
        assertEquals(
            ServiceContract.ConsentService.Companion.Endpoints.userConsents,
            lastRequest.url.encodedPath
        )
        assertEquals(ContentType.Application.Json, lastRequest.body.contentType)
    }

    @Test
    fun fetchUserConsentsTest() = runBlockingTest {
        // Given
        givenServiceResponseWith(ListSerializer(UserConsent.serializer()), listOf(userConsent))

        // When
        val result = service.fetchUserConsents("T", false)

        // Then
        assertEquals(listOf(userConsent), result)
        assertEquals(HttpMethod.Get, lastRequest.method)
        assertEquals(
            ServiceContract.ConsentService.Companion.Endpoints.userConsents,
            lastRequest.url.encodedPath
        )
        assertTrue(lastRequest.url.parameters.contains("latest"))
        assertEquals(lastRequest.url.parameters["latest"], false.toString())
    }

    @Test
    fun fetchDocumentConsentsTest() = runBlockingTest {
        // Given
        val consentKey = "custom-consent-key"
        givenServiceResponseWith(
            ListSerializer(ConsentDocument.serializer()),
            listOf(consentDocDummy)
        )

        // When
        val result = service.fetchConsentDocuments("T", 1, "DE", consentKey)

        // Then
        assertEquals(listOf(consentDocDummy), result)
        assertEquals(HttpMethod.Get, lastRequest.method)
        assertEquals(
            ServiceContract.ConsentService.Companion.Endpoints.consentDocuments,
            lastRequest.url.encodedPath
        )
        assertTrue(lastRequest.url.parameters.contains("key"))
        assertEquals(lastRequest.url.parameters["key"], consentKey)
        assertTrue(lastRequest.url.parameters.contains("version"))
        assertEquals(lastRequest.url.parameters["version"], "1")
    }

    @Test
    fun requestSignatureRegistrationTest() = runBlockingTest {
        // Given
        givenServiceToResponse(
            Pair(
                "xsrf",
                { respond("", headers = headersOf("X-Csrf-Token", "anyThing")) }
            ),
            Pair(
                "userConsents",
                {
                    responseWith(
                        ConsentSignature.serializer(),
                        consentSignature
                    )
                }
            ),
            contentType = ContentType.Application.OctetStream
        )

        // When
        val result = service.requestSignatureRegistration("T", "message")

        // Then
        assertEquals(result, consentSignature)
        assertEquals(HttpMethod.Post, lastRequest.method)
    }

    @Test
    fun requestSignatureDonationTest() = runBlockingTest {
        // Given
        givenServiceToResponse(
            Pair(
                "xsrf",
                { respond("", headers = headersOf("X-Csrf-Token", "anyThing")) }
            ),
            Pair(
                "userConsents",
                {
                    responseWith(
                        ConsentSignature.serializer(),
                        consentSignature
                    )
                }
            ),
            contentType = ContentType.Application.OctetStream
        )

        // When
        val result = service.requestSignatureDonation("T", "message")

        // Then
        assertEquals(result, consentSignature)
        assertEquals(HttpMethod.Put, lastRequest.method)
    }

    @Test
    fun revokeConsentTest() = runBlockingTest {
        // Given
        givenServiceToResponse(
            Pair(
                "xsrf",
                { respond("", headers = headersOf("X-Csrf-Token", "anyThing")) }
            ),
            contentType = ContentType.Application.OctetStream
        )

        // When
        service.revokeUserConsent("T", "DE")

        // Then
        assertEquals(HttpMethod.Delete, lastRequest.method)
    }
}
