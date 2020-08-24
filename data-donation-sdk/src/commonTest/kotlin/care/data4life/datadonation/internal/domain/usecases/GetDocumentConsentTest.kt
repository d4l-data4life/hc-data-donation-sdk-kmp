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

import care.data4life.datadonation.core.listener.ResultListener
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.internal.domain.repositories.ConsentDocumentRepository
import io.mockk.*
import runTest
import kotlin.test.Ignore
import kotlin.test.Test

abstract class GetDocumentConsentTest {

    private val consentDocumentRepository = mockk<ConsentDocumentRepository>()
    private val consentDocument = GetConsentDocuments(consentDocumentRepository)
    private val listener = spyk<DocumentContentListener>()
    private var consentDocDummy = ConsentDocument("", 1, "","","","en","",true,"","")

    @Test
    fun createUserContentFullParams() = runTest {
        //Given
        coEvery { consentDocumentRepository.getConsentDocument(any(), any(), any()) } returns listOf(consentDocDummy)

        //When
        consentDocument.runWithParams(
            GetConsentDocuments.Parameters("version", "en"),
            listener
        )

        //Then
        coVerify(ordering = Ordering.SEQUENCE){
            consentDocumentRepository.getConsentDocument(any(), any(), any())
            listener.onSuccess(any())
        }
    }

    @Ignore
    @Test
    fun getConsentDocumentMissingLanguage() = runTest {
        //Given
        var consentDocLDummy = ConsentDocument("", 1, "","","","","",true,"","")
        coEvery { consentDocumentRepository.getConsentDocument(any(), any(), any()) } returns listOf(consentDocDummy)

        //When
        consentDocument.runWithParams(
            GetConsentDocuments.Parameters("version", "en"),
            listener
        )

        //Then
        coVerify(ordering = Ordering.SEQUENCE){
            consentDocumentRepository.getConsentDocument(any(), any(), any())
            listener.onSuccess(any())
        }
    }

    @Ignore
    @Test
    fun getConsentDocumentWrongVersion() = runTest {
        //Given
        var consentDocVDummy = ConsentDocument("", 0, "","","","en","",true,"","")
        coEvery { consentDocumentRepository.getConsentDocument(any(), any(), any()) } returns listOf(consentDocVDummy)

        //When
        consentDocument.runWithParams(
            GetConsentDocuments.Parameters("version", "en"),
            listener
        )

        //Then
        coVerify(ordering = Ordering.SEQUENCE){
            consentDocumentRepository.getConsentDocument(any(), any(), any())
            listener.onSuccess(any())
        }
    }

    class DocumentContentListener : ResultListener<List<ConsentDocument>> {
        override fun onSuccess(t: List<ConsentDocument>) {
        }

        override fun onError(exception: Exception) {
        }
    }
}


