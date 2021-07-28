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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleUserConsent
import care.data4life.datadonation.mock.stub.repository.UserConsentRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FetchUserConsentsTest {
    @Test
    fun `It fulfils FetchUserConsents`() {
        val usecase: Any = FetchUserConsents(
            UserConsentRepositoryStub()
        )

        assertTrue(usecase is UsecaseContract.FetchUserConsents)
    }

    @Test
    fun `Given a Usecase had been created and execute is called, it delegates the call to the ConsentRepository without the consentDocumentKey, if the key is null`() = runBlockingTest {
        // Given
        val parameter = FetchUserConsents.Parameter(consentDocumentKey = null)
        val dummyConsentList = listOf(sampleUserConsent)
        var capturedConsentDocumentKey: String? = null
        val userContentRepository = UserConsentRepositoryStub()

        userContentRepository.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        // When
        val usecase = FetchUserConsents(userContentRepository)
        val result = usecase.execute(parameter)

        // Then
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given a Usecase had been created and execute is called, it delegates the call to the ConsentRepository with the consentDocumentKey, if the key is not null`() = runBlockingTest {
        // Given
        val consentDocumentKey = "key"
        val parameter = FetchUserConsents.Parameter(consentDocumentKey = consentDocumentKey)
        val dummyConsentList = listOf(sampleUserConsent)
        var capturedConsentDocumentKey: String? = null
        val userContentRepository = UserConsentRepositoryStub()

        userContentRepository.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        // When
        val usecase = FetchUserConsents(userContentRepository)
        val result = usecase.execute(parameter)

        // Then
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }
}
