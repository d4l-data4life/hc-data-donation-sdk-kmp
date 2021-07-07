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

import CapturingResultListener
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.domain.mock.MockUserConsentRepository
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class FetchUserConsentsTest {

    private val mockUserConsentRepository = MockUserConsentRepository()

    private val userConsents = FetchUserConsents(mockUserConsentRepository)

    private val capturingListener = FetchUserConsentListener()

    @Test
    fun fetchAllUserConsents() = runBlockingTest {
        fetchUserConsents()
    }

    @Test
    fun fetchSingleUserConsent() = runBlockingTest {
        fetchUserConsents("custom-consent-key")
    }

    private suspend fun fetchUserConsents(consentKeyParam: String? = null) {
        // Given
        var consentKeyInput: String? = "dummy"
        val dummyConsentList = listOf(DummyData.userConsent)

        mockUserConsentRepository.whenFetchUserConsents = { consentKey ->
            consentKeyInput = consentKey
            dummyConsentList
        }

        // When
        userConsents.runWithParams(
            FetchUserConsents.Parameters(consentKeyParam),
            capturingListener
        )

        // Then
        assertEquals(consentKeyParam, consentKeyInput)
        assertEquals(capturingListener.captured, dummyConsentList)
        assertNull(capturingListener.error)
    }

    class FetchUserConsentListener : CapturingResultListener<List<UserConsent>>()
}
