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

package care.data4life.datadonation.consentdocument

import care.data4life.datadonation.mock.fixture.ConsentDocumentFixture.sampleConsentDocument
import care.data4life.datadonation.mock.stub.consentdocument.ConsentDocumentRepositoryStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentDocumentInteractorTest {
    @Test
    fun `It fulfils ConsentDocumentInteractor`() {
        val interactor: Any = ConsentDocumentInteractor(
            ConsentDocumentRepositoryStub(),
            UserSessionTokenRepositoryStub()
        )

        assertTrue(interactor is ConsentDocumentContract.Interactor)
    }

    @Test
    fun `Given fetchConsentDocuments is called, it delegates the call to the ConsentDocumentRepository with the given parameters`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessToken = "session"
        val consentDocumentVersion = "42"
        val language = "de-j-old-n-kotlin-x-done"
        val consentDocumentKey = "tomato"

        var capturedVersion: String? = null
        var capturedLanguage: String? = null
        var capturedConsentDocumentKey: String? = null
        var capturedAccessToken: String? = null

        val consentDocuments = listOf(sampleConsentDocument)

        val repo = ConsentDocumentRepositoryStub()

        sessionTokenRepository.whenSessionToken = { accessToken }

        repo.whenFetchConsentDocuments = { delegatedToken, delegatedKey, delegatedVersion, delegatedLanguage ->
            capturedVersion = delegatedVersion
            capturedLanguage = delegatedLanguage
            capturedConsentDocumentKey = delegatedKey
            capturedAccessToken = delegatedToken
            consentDocuments
        }

        // When
        val result = ConsentDocumentInteractor(
            repo,
            sessionTokenRepository
        ).fetchConsentDocuments(
            consentDocumentKey = consentDocumentKey,
            consentDocumentVersion = consentDocumentVersion,
            language = language
        )

        // Then
        assertSame(
            actual = consentDocuments,
            expected = result
        )
        assertEquals(
            actual = capturedLanguage,
            expected = language
        )
        assertEquals(
            actual = capturedVersion,
            expected = consentDocumentVersion
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
        assertEquals(
            actual = capturedAccessToken,
            expected = accessToken
        )
    }
}
