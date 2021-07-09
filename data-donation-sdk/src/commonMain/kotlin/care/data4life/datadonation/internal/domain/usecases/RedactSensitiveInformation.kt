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

import care.data4life.datadonation.internal.domain.usecases.UsecaseContract.RedactSensitiveInformation.Companion.REDACTED
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer

internal class ReactSensitiveInformationFactory : UsecaseContract.RedactSensitiveInformation {
    private fun <T> mapOrNull(
        list: List<T>?,
        action: (T) -> T
    ): List<T>? {
        return if (list.isNullOrEmpty()) {
            null
        } else {
            list.map { item -> action(item) }
        }
    }

    private fun mapFhir(resource: FhirResource): FhirResource {
        return if (resource is QuestionnaireResponse) {
            mapQuestionnaireResponse(resource)
        } else {
            resource
        }
    }

    private fun mapQuestionnaireResponse(
        questionnaireResponse: QuestionnaireResponse
    ): QuestionnaireResponse {
        return questionnaireResponse.copy(
            item = mapOrNull(questionnaireResponse.item, ::mapQuestionnaireResponseItem)
        )
    }

    private fun mapQuestionnaireResponseItem(
        responseItem: QuestionnaireResponseItem
    ): QuestionnaireResponseItem {
        val item = mapOrNull(responseItem.item, ::mapQuestionnaireResponseItem)
        val answer = mapOrNull(responseItem.answer, ::mapQuestionnaireResponseItemAnswer)

        return responseItem.copy(
            item = item,
            answer = answer
        )
    }

    private fun mapQuestionnaireResponseItemAnswer(
        itemAnswer: QuestionnaireResponseItemAnswer
    ): QuestionnaireResponseItemAnswer {
        val item = mapOrNull(itemAnswer.item, ::mapQuestionnaireResponseItem)
        val valueString = maskQuestionnaireResponseItemAnswerValueString(itemAnswer.valueString)

        return itemAnswer.copy(
            item = item,
            valueString = valueString
        )
    }

    private fun maskQuestionnaireResponseItemAnswerValueString(valueString: String?): String? {
        return if (valueString is String) {
            REDACTED
        } else {
            null
        }
    }

    override suspend fun execute(parameter: List<FhirResource>): List<FhirResource> {
        return parameter.map { resource -> mapFhir(resource) }
    }
}
