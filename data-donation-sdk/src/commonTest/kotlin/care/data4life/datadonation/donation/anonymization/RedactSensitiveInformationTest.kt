/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.datadonation.donation.anonymization

import care.data4life.datadonation.donation.anonymization.AnonymizationContract.Redaction.Companion.REDACTED
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.DomainResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RedactSensitiveInformationTest {
    private val questionnaireResponseTemplate = QuestionnaireResponse(
        status = QuestionnaireResponseStatus.COMPLETED
    )
    private val referenceTemplate = Reference(
        reference = "http://fhir.data4life.care/covid-19/stu3/Questionnaire/covid19-covhub-symptom-tracking|someNotSoIntresting"
    )
    private val questionnaireResponseItemTemplate = QuestionnaireResponseItem(linkId = "does not matter")
    private val questionnaireResponseItemAnswerTemplate = QuestionnaireResponseItemAnswer()

    @Test
    fun `It fulfils Redaction`() {
        val factory: Any = RedactSensitiveInformation()

        assertTrue(factory is AnonymizationContract.Redaction)
    }

    @Test
    fun `Given a redact is called, it will pass through non QuestionnaireResponses`() = runBlockingTest {
        // Given
        val resource = listOf(DomainResource(), DomainResource())

        // When
        val result = RedactSensitiveInformation().redact(resource)

        // Then
        assertSame(
            actual = resource[0],
            expected = result[0]
        )
        assertSame(
            actual = resource[1],
            expected = result[1]
        )
    }

    @Test
    fun `Given a redact is called, it will pass through QuestionnaireResponses, which do not match the domain criteria`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(),
            questionnaireResponseTemplate.copy(
                questionnaire = Reference()
            ),
            questionnaireResponseTemplate.copy(
                questionnaire = Reference(reference = "not the thing you are looking for")
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource)

        // Then
        assertSame(
            actual = resource[0],
            expected = result[0]
        )
        assertSame(
            actual = resource[1],
            expected = result[1]
        )
        assertSame(
            actual = resource[2],
            expected = result[2]
        )
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponses to null, if they are null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = null
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item)
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponses to null, if they are empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = emptyList()
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item)
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponses, if they are not null or empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(questionnaireResponseItemTemplate)
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item)
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponseItem to null, if they are null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = null
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponseItem to null, if they are empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = emptyList()
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given a redact is called, it maps Items of a QuestionnaireResponseItem, if they are not null or empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        item = listOf(questionnaireResponseItemTemplate)
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

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

        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertEquals(
            actual = result.item!!.first().item!!.first().item!!.first(),
            expected = innerItem
        )
    }

    @Test
    fun `Given a redact is called, it maps Answers of a QuestionnaireResponseItem to null, if they are null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        answer = null
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a redact is called, it ignores ItemAnswers, which are not FreeTextFields `() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertSame(
            expected = resource.first().item!!.first().answer,
            actual = result.item!!.first().answer
        )
    }

    @Test
    fun `Given a redact is called, it maps Answers of a QuestionnaireResponseItem to null, if they are empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
                        answer = emptyList()
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a redact is called, it maps Answers of a QuestionnaireResponseItem, if they are not empty or null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
                        answer = listOf(questionnaireResponseItemAnswerTemplate)
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item!!.first().answer)
    }

    @Test
    fun `Given a redact is called, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
                        answer = listOf(questionnaireResponseItemAnswerTemplate)
                    )
                )
            )
        )

        // When
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a redact is called, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a redact is called, it maps Item of the QuestionnaireResponseItemAnswer, if they are not null or empty`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNotNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given a redact is called, it maps ValueStrings of the QuestionnaireResponseItemAnswer to null, if they are null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertNull(result.item!!.first().answer!!.first().valueString)
    }

    @Test
    fun `Given a redact is called, it maps ValueStrings of the QuestionnaireResponseItemAnswer to REDACTED, if they are not null`() = runBlockingTest {
        // Given
        val resource = listOf(
            questionnaireResponseTemplate.copy(
                questionnaire = referenceTemplate,
                item = listOf(
                    questionnaireResponseItemTemplate.copy(
                        linkId = "FN",
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
        val result = RedactSensitiveInformation().redact(resource).first()

        // Then
        assertTrue(result is QuestionnaireResponse)
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueString,
            expected = REDACTED
        )
    }
}
