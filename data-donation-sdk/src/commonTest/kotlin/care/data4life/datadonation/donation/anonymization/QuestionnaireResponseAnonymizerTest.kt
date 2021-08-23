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

import care.data4life.datadonation.donation.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import care.data4life.datadonation.mock.stub.donation.anonymization.DateTimeSmearerStub
import care.data4life.datadonation.mock.stub.donation.anonymization.RedactorStub
import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class QuestionnaireResponseAnonymizerTest {
    private val questionnaireResponseTemplate = QuestionnaireResponse(
        status = QuestionnaireResponseStatus.COMPLETED
    )
    private val questionnaireResponseItemTemplate = QuestionnaireResponseItem(linkId = "does not matter")
    private val questionnaireResponseItemAnswerTemplate = QuestionnaireResponseItemAnswer()

    @Test
    fun `It fulfils QuestionnaireResponseAnonymizer`() {
        val anonymizer: Any = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        )

        assertTrue(anonymizer is AnonymizationContract.QuestionnaireResponseAnonymizer)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and null as a BlurRule it ignores the authored field, if no BlurFunction was provided for the field`() {
        // Given
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val rule = null

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule it ignores the authored field, if no BlurFunction was provided for the field`() {
        // Given
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule it ignores the authored field, if it does not exists`() {
        // Given
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule it blurs the authored field`() {
        // Given
        val bluredAuthoredDate = XsDateTime(
            date = XsDate(2022, 1, 23)
        )
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val dateTimeSmearer = DateTimeSmearerStub()

        val rule = BlurRule(
            targetTimeZone = "somewhere",
            questionnaireResponseAuthored = BlurFunction.END_OF_DAY
        )

        var capturedDateTime: XsDateTime? = null
        var capturedLocation: String? = null
        var capturedBlurFunction: BlurFunction? = null

        dateTimeSmearer.whenBlur = { delegatedDateTime, delegatedLocation, delegatedBlurFunction ->
            capturedDateTime = delegatedDateTime
            capturedLocation = delegatedLocation
            capturedBlurFunction = delegatedBlurFunction

            bluredAuthoredDate
        }

        // When
        val result = QuestionnaireResponseAnonymizer(
            dateTimeSmearer,
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource.copy(
                authored = resource.authored!!.copy(
                    value = bluredAuthoredDate
                )
            )
        )

        assertSame(
            actual = capturedDateTime,
            expected = resource.authored!!.value
        )
        assertEquals(
            actual = capturedLocation,
            expected = rule.targetTimeZone
        )
        assertSame(
            actual = capturedBlurFunction,
            expected = rule.questionnaireResponseAuthored
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Items of a QuestionnaireResponses to null, if they are null`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = null
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Items of a QuestionnaireResponses, if they are not null or empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(questionnaireResponseItemTemplate)
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNotNull(result.item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Items of a QuestionnaireResponseItem to null, if they are null`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    item = null
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Items of a QuestionnaireResponseItem to null, if they are empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    item = emptyList()
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Items of a QuestionnaireResponseItem, if they are not null or empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    item = listOf(questionnaireResponseItemTemplate)
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNotNull(result.item!!.first().item)
    }

    @Test
    fun `Given anonymize is called it maps Items of a QuestionnaireResponseItem recursively`() {
        // Given
        val innerItem = questionnaireResponseItemTemplate.copy(
            linkId = "potato",
            item = null
        )

        val resource = questionnaireResponseTemplate.copy(
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

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result.item!!.first().item!!.first().item!!.first(),
            expected = innerItem
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Answers of a QuestionnaireResponseItem to null, if they are null`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = null
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Answers of a QuestionnaireResponseItem to null, if they are empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = emptyList()
                )
            )
        )

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            RedactorStub()
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().answer)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Answers of a QuestionnaireResponseItem, if they are not empty or null`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = listOf(questionnaireResponseItemAnswerTemplate)
                )
            )
        )

        val redactor = RedactorStub()

        redactor.whenRedact = { it }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNotNull(result.item!!.first().answer)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are null`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = listOf(questionnaireResponseItemAnswerTemplate)
                )
            )
        )

        val redactor = RedactorStub()

        redactor.whenRedact = { it }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Item of the QuestionnaireResponseItemAnswer to null, if they are empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
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

        val redactor = RedactorStub()

        redactor.whenRedact = { it }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it maps Item of the QuestionnaireResponseItemAnswer, if they are not null or empty`() {
        // Given
        val resource = questionnaireResponseTemplate.copy(
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

        val redactor = RedactorStub()

        redactor.whenRedact = { it }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNotNull(result.item!!.first().answer!!.first().item)
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it ignores the valueString of a QuestionnaireResponseItemAnswer if the ProgramType is STUDY`() {
        // Given
        val valueString = "potato"
        val expected = "soup"

        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = listOf(
                        questionnaireResponseItemAnswerTemplate.copy(
                            item = listOf(questionnaireResponseItemTemplate),
                            valueString = valueString
                        )
                    )
                )
            )
        )

        val redactor = RedactorStub()

        var capturedValueString: String? = null

        redactor.whenRedact = { delegatedValueString ->
            capturedValueString = delegatedValueString

            delegatedValueString
        }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertNull(capturedValueString)
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueString,
            expected = valueString
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it delegates the valueString of a QuestionnaireResponseItemAnswer to the Redactor and uses its result if the ProgramType is DIARY`() {
        // Given
        val valueString = "potato"
        val expected = "soup"

        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    answer = listOf(
                        questionnaireResponseItemAnswerTemplate.copy(
                            item = listOf(questionnaireResponseItemTemplate),
                            valueString = valueString
                        )
                    )
                )
            )
        )

        val redactor = RedactorStub()

        var capturedValueString: String? = null

        redactor.whenRedact = { delegatedValueString ->
            capturedValueString = delegatedValueString

            expected
        }

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.DIARY,
            rule
        )

        // Then
        assertEquals(
            actual = capturedValueString,
            expected = valueString
        )
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueString,
            expected = expected
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it ignores the valueDateTime of a QuestionnaireResponseItemAnswer if no BlurFunction could be determined`() {
        // Given
        val expected = DateTime(
            value = XsDateTime(XsDate(1, 2, 3))
        )

        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    linkId = "bcc",
                    answer = listOf(
                        questionnaireResponseItemAnswerTemplate.copy(
                            item = listOf(questionnaireResponseItemTemplate),
                            valueDateTime = expected
                        )
                    )
                )
            )
        )

        val redactor = RedactorStub()
        redactor.whenRedact = { it }

        var wasCalled = false
        val smearer = DateTimeSmearerStub()

        smearer.whenBlur = { _, _, _ ->
            wasCalled = true

            expected.value
        }

        val rule = BlurRule(
            targetTimeZone = "somewhere",
            questionnaireResponseItemBlurMapping = listOf(
                QuestionnaireResponseItemBlur(
                    linkId = "abc",
                    function = BlurFunction.END_OF_WEEK
                )
            )
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertFalse(wasCalled)
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueDateTime,
            expected = expected
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and null as a BlurRule, it ignores the valueDateTime of a QuestionnaireResponseItemAnswer if no BlurFunction could be determined`() {
        // Given
        val expected = DateTime(
            value = XsDateTime(XsDate(1, 2, 3))
        )

        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    linkId = "bcc",
                    answer = listOf(
                        questionnaireResponseItemAnswerTemplate.copy(
                            item = listOf(questionnaireResponseItemTemplate),
                            valueDateTime = expected
                        )
                    )
                )
            )
        )

        val redactor = RedactorStub()
        redactor.whenRedact = { it }

        var wasCalled = false
        val smearer = DateTimeSmearerStub()

        smearer.whenBlur = { _, _, _ ->
            wasCalled = true

            expected.value
        }

        // When
        val result = QuestionnaireResponseAnonymizer(
            DateTimeSmearerStub(),
            redactor
        ).anonymize(resource, ProgramType.STUDY, null)

        // Then
        assertFalse(wasCalled)
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueDateTime,
            expected = expected
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a BlurRule, it blurs the valueDateTime of a QuestionnaireResponseItemAnswer with the first matching ResourceItem`() {
        // Given
        val valueDateTimeValue = XsDateTime(XsDate(42, 12, 1))
        val expected = XsDateTime(XsDate(1, 2, 3))

        val resource = questionnaireResponseTemplate.copy(
            item = listOf(
                questionnaireResponseItemTemplate.copy(
                    linkId = "match",
                    answer = listOf(
                        questionnaireResponseItemAnswerTemplate.copy(
                            item = listOf(questionnaireResponseItemTemplate),
                            valueDateTime = DateTime(
                                value = valueDateTimeValue
                            )
                        )
                    )
                )
            )
        )

        val redactor = RedactorStub()
        redactor.whenRedact = { it }

        var capturedXsDateTime: XsDateTime? = null
        var capturedTargetZone: TargetTimeZone? = null
        var capturedBlurFunction: BlurFunction? = null

        val smearer = DateTimeSmearerStub()

        smearer.whenBlur = { delegatedXsDateTime, delegatedTargetZone, delegatedBlurFunction ->
            capturedXsDateTime = delegatedXsDateTime
            capturedTargetZone = delegatedTargetZone
            capturedBlurFunction = delegatedBlurFunction

            expected
        }

        val rule = BlurRule(
            targetTimeZone = "somewhere",
            questionnaireResponseItemBlurMapping = listOf(
                QuestionnaireResponseItemBlur(
                    linkId = "match",
                    function = BlurFunction.START_OF_DAY
                ),
                QuestionnaireResponseItemBlur(
                    linkId = "match",
                    function = BlurFunction.END_OF_MONTH
                )
            )
        )

        // When
        val result = QuestionnaireResponseAnonymizer(
            smearer,
            redactor
        ).anonymize(
            resource,
            ProgramType.STUDY,
            rule
        )

        // Then
        assertEquals(
            actual = result.item!!.first().answer!!.first().valueDateTime,
            expected = DateTime(
                value = expected
            )
        )

        assertSame(
            actual = capturedXsDateTime,
            expected = valueDateTimeValue
        )
        assertSame(
            actual = capturedTargetZone,
            expected = rule.targetTimeZone
        )
        assertSame(
            actual = capturedBlurFunction,
            expected = rule.questionnaireResponseItemBlurMapping.first().function
        )
    }
}
