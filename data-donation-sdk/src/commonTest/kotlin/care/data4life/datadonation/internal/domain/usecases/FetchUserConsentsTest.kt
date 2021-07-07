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

import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.mock.stub.UserConsentRepositoryStub
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FetchUserConsentsTest {
    @Test
    fun `It fulfils FetchUserConsents`() {
        val factory: Any = FetchUserConsentsFactory(UserConsentRepositoryStub())

        assertTrue(factory is UsecaseContract.UsecaseFactory<*, *>)
    }

    @Test
    fun `Given withParams is called with the appropriate Parameter it creates a Usecase`() {
        // Given
        val parameter = FetchUserConsentsFactory.Parameters()

        // When
        val usecase: Any = FetchUserConsentsFactory(UserConsentRepositoryStub()).withParams(parameter)

        // Then
        assertTrue(usecase is UsecaseContract.Usecase<*>)
    }

    @Test
    fun `Given a Usecase had been created and execute is called, it delegates the call to the consentRepository without the consentKey, if the key is null`() = runBlockingTest {
        // Given
        val parameter = FetchUserConsentsFactory.Parameters(consentKey = null)
        val dummyConsentList = listOf(DummyData.userConsent)
        var capturedConsentKey: String? = null
        val userContentRepository = UserConsentRepositoryStub()

        userContentRepository.whenFetchUserConsents = { delegatedConsentKey ->
            capturedConsentKey = delegatedConsentKey
            dummyConsentList
        }

        // When
        val usecase = FetchUserConsentsFactory(userContentRepository).withParams(parameter)
        val result = usecase.execute()

        // Then
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertNull(capturedConsentKey)
    }

    @Test
    fun `Given a Usecase had been created and execute is called, it delegates the call to the consentRepository with the consentKey, if the key is not null`() = runBlockingTest {
        // Given
        val consentKey = "key"
        val parameter = FetchUserConsentsFactory.Parameters(consentKey = consentKey)
        val dummyConsentList = listOf(DummyData.userConsent)
        var capturedConsentKey: String? = null
        val userContentRepository = UserConsentRepositoryStub()

        userContentRepository.whenFetchUserConsents = { delegatedConsentKey ->
            capturedConsentKey = delegatedConsentKey
            dummyConsentList
        }

        // When
        val usecase = FetchUserConsentsFactory(userContentRepository).withParams(parameter)
        val result = usecase.execute()

        // Then
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertEquals(
            actual = capturedConsentKey,
            expected = consentKey
        )
    }
}
