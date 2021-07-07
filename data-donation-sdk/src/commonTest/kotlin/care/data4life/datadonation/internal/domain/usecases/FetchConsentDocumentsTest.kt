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
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.domain.mock.MockConsentDocumentRepository
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class FetchConsentDocumentsTest {

    private val consentDocumentRepository = MockConsentDocumentRepository()
    private val fetchConsentDocument = FetchConsentDocuments(consentDocumentRepository)

    private val capturingListener = FetchConsentDocumentListener()

    @Test
    fun createUserContentFullParams() = runBlockingTest {
        // Given
        val expectedLanguage = "en"
        val expectedVersion = 1
        val expectedConsentKey = "custom-consent-key"
        var languageInput: String? = "dummy"
        var versionInput: Int? = -1
        var consentKeyInput = "dummy"
        val documentList = listOf(DummyData.consentDocument)
        consentDocumentRepository.whenFetchConsentDocuments = { language: String?, version: Int?, consentKey: String ->
            languageInput = language
            versionInput = version
            consentKeyInput = consentKey
            documentList
        }

        // When
        fetchConsentDocument.runWithParams(FetchConsentDocuments.Parameters(expectedVersion, expectedLanguage, expectedConsentKey), capturingListener)

        // Then
        assertEquals(expectedVersion, versionInput)
        assertEquals(expectedLanguage, languageInput)
        assertEquals(expectedConsentKey, consentKeyInput)
        assertEquals(capturingListener.captured, documentList)
        assertNull(capturingListener.error)
    }

    class FetchConsentDocumentListener : CapturingResultListener<List<ConsentDocument>>()
}
