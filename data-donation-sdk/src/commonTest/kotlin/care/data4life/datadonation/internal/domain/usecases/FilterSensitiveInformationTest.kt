/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2021, D4L data4life gGmbH
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

import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class FilterSensitiveInformationTest {

    private val usecase: FilterSensitiveInformation = FilterSensitiveInformation()

    private val stringAnswer =
        QuestionnaireResponseItemAnswer(id = "stringAnswer", valueString = "something")

    private val intAnswer =
        QuestionnaireResponseItemAnswer(
            id = "intAnswer",
            valueInteger = "12"
        ) //TODO replace when fhir lib updated


    @Test
    fun `empty QuestionnaireResponse is untouched`() = runTest {
        //Given
        val response = QuestionnaireResponse(status = QuestionnaireResponseStatus.COMPLETED)

        val resources =
            listOf(response)

        //When
        val result = usecase.withParams(resources).execute()

        //Then
        assertEquals(
            result.size,
            1
        )
        assertEquals(
            result[0],
            response
        )
    }

    @Test
    fun `QuestionnaireResponse with only string answer is redacted`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = answers(stringAnswer)
        )

        val resources =
            listOf(response)

        //When
        val result = usecase.withParams(resources).execute()

        //Then
        assertEquals(
            result.size,
            1
        )
        assertTrue { result[0] is QuestionnaireResponse }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.size == 1 }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.first().answer!!.size == 1 }
        assertEquals(
            (result[0] as QuestionnaireResponse).item!!.first().answer!!.first().valueString,
            FilterSensitiveInformation.redacted
        )
    }


    @Test
    fun `QuestionnaireResponse with multiple string answer, all redacted`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = answers(stringAnswer.copy(), stringAnswer.copy(valueString = "somethingElse"))
        )
        val resources =
            listOf(response)

        //When
        val result = usecase.withParams(resources).execute()

        //Then
        assertEquals(
            result.size,
            1
        )
        assertTrue { result[0] is QuestionnaireResponse }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.size == 2 }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.first().answer!!.size == 1 }
        assertEquals(
            (result[0] as QuestionnaireResponse).item!!.first().answer!!.first().valueString,
            FilterSensitiveInformation.redacted
        )
        assertEquals(
            (result[0] as QuestionnaireResponse).item!![1].answer!!.first().valueString,
            FilterSensitiveInformation.redacted
        )
    }

    @Test
    fun `QuestionnaireResponse with multiple different answer, only string redacted`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = answers(stringAnswer, intAnswer)
        )
        val resources =
            listOf(response)

        //When
        val result = usecase.withParams(resources).execute()

        //Then
        assertTrue { result[0] is QuestionnaireResponse }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.size == 2 }
        assertTrue { (result[0] as QuestionnaireResponse).item!!.first().answer!!.size == 1 }
        assertEquals(
            (result[0] as QuestionnaireResponse).item!!.first().answer!!.first().valueString,
            FilterSensitiveInformation.redacted
        )
        assertEquals(
            (result[0] as QuestionnaireResponse).item!![1].answer!!.first(),
            intAnswer
        )
    }

    @Test
    fun `QuestionnaireResponse with multi level answers, string filtered in deeper levels`() =
        runTest {
            //Given
            val response = QuestionnaireResponse(
                status = QuestionnaireResponseStatus.COMPLETED,
                item = listOf(
                    QuestionnaireResponseItem(
                        item = answers(stringAnswer),
                        linkId = "group"
                    )
                )
            )

            val resources =
                listOf(response)

            //When
            val result = usecase.withParams(resources).execute()

            //Then
            assertEquals(
                result.size,
                1
            )
            assertTrue { result[0] is QuestionnaireResponse }
            assertTrue { (result[0] as QuestionnaireResponse).item!!.size == 1 }
            assertTrue { (result[0] as QuestionnaireResponse).item!!.first().item!!.size == 1 }
            assertTrue { (result[0] as QuestionnaireResponse).item!!.first().item!!.first().answer!!.size == 1 }
            assertEquals(
                (result[0] as QuestionnaireResponse).item!!.first().item!!.first().answer!!.first().valueString,
                FilterSensitiveInformation.redacted
            )
        }


    private fun answers(vararg answers: QuestionnaireResponseItemAnswer): List<QuestionnaireResponseItem> =
        answers.map { QuestionnaireResponseItem(answer = listOf(it), linkId = it.toString()) }
}
