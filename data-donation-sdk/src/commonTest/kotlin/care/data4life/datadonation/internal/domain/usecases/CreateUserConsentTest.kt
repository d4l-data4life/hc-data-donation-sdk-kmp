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

import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.stub.repository.UserConsentRepositoryStub
import care.data4life.sdk.util.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CreateUserConsentTest {
    @Test
    fun `It fulfils CreateUserConsent`() {
        val factory: Any = CreateUserConsentFactory(UserConsentRepositoryStub())

        assertTrue(factory is UsecaseContract.CreateUserConsent)
    }

    @Test
    fun `Given withParams is called with the appropriate Parameter it creates a Usecase`() {
        // Given
        val parameter = CreateUserConsentFactory.Parameter(
            KeyPair(ByteArray(23), ByteArray(42)),
            "custom-consent-key",
            23
        )

        // When
        val usecase: Any = CreateUserConsentFactory(UserConsentRepositoryStub()).withParams(parameter)

        // Then
        assertTrue(usecase is UsecaseContract.Usecase<*>)
    }

    @Test
    fun `Given a Usecase had been created and execute is called, it delegates the call to the UserContentRepository with the given parameters and returns the first consent`() = runBlockingTest {
        // Given
        val consentKey = "custom-consent-key"
        val version = 42
        val keyPair = KeyPair(ByteArray(23), ByteArray(42))

        var capturedCreationConsentKey: String? = "NotNull"
        var capturedVersion: Int? = null

        var capturedFetchingConsentKey: String? = null

        val consent = DummyData.userConsent

        val repo = UserConsentRepositoryStub()

        repo.whenCreateUserConsent = { delegatedConsentKey, delegatedVersion ->
            capturedCreationConsentKey = delegatedConsentKey
            capturedVersion = delegatedVersion
        }
        repo.whenFetchUserConsents = { delegatedConsentKey ->
            capturedFetchingConsentKey = delegatedConsentKey
            listOf(consent, DummyData.userConsent.copy(accountId = "not expected"))
        }

        val parameter = CreateUserConsentFactory.Parameter(keyPair, consentKey, version)

        // When
        val result = CreateUserConsentFactory(repo).withParams(parameter).execute()

        // Then
        assertSame(
            actual = consent,
            expected = result
        )
        assertEquals(
            actual = capturedCreationConsentKey,
            expected = consentKey
        )
        assertEquals(
            actual = capturedVersion,
            expected = version
        )
        assertNull(capturedFetchingConsentKey)
    }
}
