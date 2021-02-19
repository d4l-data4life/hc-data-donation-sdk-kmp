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

import care.data4life.hl7.fhir.stu3.FhirStu3Parser
import care.data4life.hl7.fhir.stu3.codesystem.CarePlanIntent
import care.data4life.hl7.fhir.stu3.codesystem.CarePlanStatus
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.*
import care.data4life.hl7.fhir.stu3.primitive.Integer
import kotlinx.serialization.json.Json
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class RemoveInternalInformationTest {

    private val usecase: RemoveInternalInformation = RemoveInternalInformation(Json { })

    private val stringAnswer =
        QuestionnaireResponseItemAnswer(id = "stringAnswer", valueString = "something")

    private val intAnswer =
        QuestionnaireResponseItemAnswer(
            id = "intAnswer",
            valueInteger = Integer(12)
        )


    @Test
    fun `QuestionnaireResponse id is removed`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            id = "imAnId",
            status = QuestionnaireResponseStatus.COMPLETED
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
        assertEquals(
            result[0].contains("imAnId"),
            false
        )
        val responseParsed =
            FhirStu3Parser.defaultJsonParser().fromJson(QuestionnaireResponse::class, result[0])
        assertNotNull(responseParsed)
        assertEquals(response.copy(id = null), responseParsed)
    }

    @Test
    fun `Multiple different resources id removed but other information kept`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            id = "QuestionnaireResponse1",
            status = QuestionnaireResponseStatus.COMPLETED,
            item = answers(stringAnswer)
        )
        val care = CarePlan(
            id = "CarePlan1",
            status = CarePlanStatus.ACTIVE,
            intent = CarePlanIntent.ORDER,
            subject = Reference(reference = "whatever")
        )

        val resources =
            listOf(response, care)

        //When
        val result = usecase.withParams(resources).execute()

        //Then
        assertEquals(
            result.size,
            2
        )
        val responseParsed =
            FhirStu3Parser.defaultJsonParser().fromJson(QuestionnaireResponse::class, result[0])

        val careParsed =
            FhirStu3Parser.defaultJsonParser().fromJson(CarePlan::class, result[1])

        assertEquals(response.copy(id = null), responseParsed)
        assertEquals(care.copy(id = null), careParsed)
    }


    @Test
    fun `QuestionnaireResponse not changed when there is no ID`() = runTest {
        //Given
        val response = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.COMPLETED,
            item = answers(stringAnswer.copy())
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
        val responseParsed =
            FhirStu3Parser.defaultJsonParser().fromJson(QuestionnaireResponse::class, result[0])
        assertEquals(response, responseParsed)
    }

    private fun answers(vararg answers: QuestionnaireResponseItemAnswer): List<QuestionnaireResponseItem> =
        answers.map { QuestionnaireResponseItem(answer = listOf(it), linkId = it.toString()) }
}
