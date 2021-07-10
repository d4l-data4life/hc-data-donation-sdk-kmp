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

import care.data4life.datadonation.internal.domain.usecases.UsecaseContract.RedactSensitiveInformation.Companion.REDACTED
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.DomainResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.sdk.util.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RedactSensitiveInformationTest {
    private val questionnaireResponseTemplate = QuestionnaireResponse(status = QuestionnaireResponseStatus.COMPLETED)
    private val questionnaireResponseItemTemplate = QuestionnaireResponseItem(linkId = "does not matter")
    private val questionnaireResponseItemAnswerTemplate = QuestionnaireResponseItemAnswer()

    @Test
    fun `It fulfils RedactSensitiveInformation`() {
        val factory: Any = RedactSensitiveInformation()

        assertTrue(factory is UsecaseContract.RedactSensitiveInformation)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it will pass through non QuestionnaireResponses`() = runBlockingTest {
        // Given
        val parameter = listOf(DomainResource(), DomainResource())

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter)

        // Then
        assertSame(
            actual = parameter[0],
            expected = result[0]
        )
        assertSame(
            actual = parameter[1],
            expected = result[1]
        )
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponses to null, if they are null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = null
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponses to null, if they are empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = emptyList()
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponses, if they are not null or empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(questionnaireResponseItemTemplate)
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponseItem to null, if they are null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = null
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponseItem to null, if they are empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = emptyList()
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Items of a QuestionnaireResponseItem, if they are not null or empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = listOf(questionnaireResponseItemTemplate)
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item!!.first().item)
    }

    @Test
    fun `Given a Uses had been create with and execute is called it maps Items of a QuestionnaireResponseItem recursively`() = runBlockingTest {
        // Given
        val innerItem = questionnaireResponseItemTemplate.copy(
            linkId = "potato",
            item = null
        )

        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = listOf(
                            questionnaireResponseItemTemplate.copy(
                                item = listOf(innerItem)
                            )
                        )
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertEquals(
            actual = result.item!!.first().item!!.first().item!!.first(),
            expected = innerItem
        )
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Answers of a QuestionnaireResponseItem to null, if they are null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = null
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Answers of a QuestionnaireResponseItem to null, if they are empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = emptyList()
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Answers of a QuestionnaireResponseItem, if they are not empty or null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(questionnaireResponseItemAnswerTemplate)
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(questionnaireResponseItemAnswerTemplate)
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(
                            questionnaireResponseItemAnswerTemplate.copy(
                                item = emptyList()
                            )
                        )
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps Item of the QuestionnaireResponseItemAnswer, if they are not null or empty`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(
                            questionnaireResponseItemAnswerTemplate.copy(
                                item = listOf(questionnaireResponseItemTemplate)
                            )
                        )
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps ValueStrings of the QuestionnaireResponseItemAnswer to null, if they are null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(
                            questionnaireResponseItemAnswerTemplate.copy(
                                valueString = null
                            )
                        )
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().valueString)
    }

    @Test
    fun `Given a Usecase had been create with and execute is called, it maps ValueStrings of the QuestionnaireResponseItemAnswer to REDACTED, if they are not null`() = runBlockingTest {
        // Given
        val parameter = listOf(
            questionnaireResponseTemplate.copy(
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = listOf(
                            questionnaireResponseItemAnswerTemplate.copy(
                                valueString = "tomato"
                            )
                        )
                    )
                )
            )
        )

        // When
        val usecase = RedactSensitiveInformation()
        val result = usecase.execute(parameter).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueString,
            expected = REDACTED
        )
    }
}
