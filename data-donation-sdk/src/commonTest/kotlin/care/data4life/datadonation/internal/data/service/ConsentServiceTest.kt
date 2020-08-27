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
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.model.TokenVerificationResult
import care.data4life.datadonation.internal.data.service.ConsentService.Companion.dataDonationKey
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.serialization.builtins.list
import runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class ConsentServiceTest : BaseServiceTest<ConsentService>() {
    private val tokenVerificationResult = TokenVerificationResult("StudyId", "externalId", "")
    private val userConsent = UserConsent("key", "version", "accountId", "event", "0")
    private val consentSignature = ConsentSignature("signature")
    private var consentDocDummy = ConsentDocument("", 1, "","","","en","",true,"","")

    override fun getService(httpClient: HttpClient, environment: Environment) =
        ConsentService(httpClient, environment)

    @Test
    fun createUserConsentTest() = runTest {
        //Given
        givenServiceResponseWith(
            TokenVerificationResult.serializer(),
            tokenVerificationResult
        )

        //When
        val result = service.createUserConsent("T", "1")

        //Then
        assertEquals(result, tokenVerificationResult)
        assertEquals(HttpMethod.Post, lastRequest.method)
    }

    @Test
    fun fetchUserConsentsTest() = runTest {
        //Given
        givenServiceResponseWith(UserConsent.serializer().list, listOf(userConsent))

        //When
        val result = service.fetchUserConsents("T", false)

        //Then
        assertEquals(listOf(userConsent), result)
        assertEquals(HttpMethod.Get, lastRequest.method)
        assertEquals(ConsentService.Companion.Endpoints.userConsents, lastRequest.url.encodedPath)
        assertTrue(lastRequest.url.parameters.contains("consentDocumentKey"))
        assertEquals(lastRequest.url.parameters["consentDocumentKey"], dataDonationKey)
        assertTrue(lastRequest.url.parameters.contains("latest"))
        assertEquals(lastRequest.url.parameters["latest"], false.toString())
    }

    @Test
    fun fetchDocumentConsentsTest() = runTest {
        //Given
        givenServiceResponseWith(ConsentDocument.serializer().list, listOf(consentDocDummy))

        //When
        val result = service.fetchConsentDocument("T", "data donation", "1", "DE")

        //Then
        assertEquals(listOf(consentDocDummy), result)
        assertEquals(HttpMethod.Get, lastRequest.method)
        assertEquals(ConsentService.Companion.Endpoints.consentDocuments, lastRequest.url.encodedPath)
        assertTrue(lastRequest.url.parameters.contains("consentDocumentKey"))
        assertEquals(lastRequest.url.parameters["consentDocumentKey"], dataDonationKey)
        assertTrue(lastRequest.url.parameters.contains("version"))
        assertEquals(lastRequest.url.parameters["version"], "1")

    }

    @Test
    fun requestSignatureTest() = runTest {
        //Given
        givenServiceResponseWith(
            ConsentSignature.serializer(),
            consentSignature
        )

        //When
        val result = service.requestSignature("T")

        //Then
        assertEquals(result, consentSignature)
        assertEquals(HttpMethod.Post, lastRequest.method)
    }

}
