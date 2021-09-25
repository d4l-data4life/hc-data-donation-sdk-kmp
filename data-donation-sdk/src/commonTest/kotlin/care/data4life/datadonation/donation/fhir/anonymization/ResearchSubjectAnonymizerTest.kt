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

package care.data4life.datadonation.donation.fhir.anonymization

import care.data4life.datadonation.donation.fhir.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.mock.stub.donation.fhir.anonymization.DateTimeConcealerStub
import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.codesystem.ResearchSubjectStatus
import care.data4life.hl7.fhir.stu3.model.Period
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.model.ResearchSubject
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ResearchSubjectAnonymizerTest {
    private val researchSubjectTemplate = ResearchSubject(
        status = ResearchSubjectStatus.ACTIVE,
        study = Reference(),
        individual = Reference()
    )

    @Test
    fun `It fulfils ResearchSubjectAnonymizer`() {
        val anonymizer: Any = ResearchSubjectAnonymizer(DateTimeConcealerStub())

        assertTrue(anonymizer is AnonymizationContract.ResearchSubjectAnonymizer)
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and null as BlurRule it reflects the given ResearchSubject`() {
        // Given
        val resource = researchSubjectTemplate.copy()

        // When
        val result = ResearchSubjectAnonymizer(
            DateTimeConcealerStub()
        ).anonymize(
            resource,
            null
        )

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it reflects the given ResearchSubject, if the rule contains no matching BlurFunction`() {
        // Given
        val resource = researchSubjectTemplate.copy()

        val rule = BlurRule(
            targetTimeZone = "any"
        )
        // When
        val result = ResearchSubjectAnonymizer(
            DateTimeConcealerStub()
        ).anonymize(
            resource,
            rule
        )

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it reflects the given ResearchSubject, if the ReseachSubject has no periode property`() {
        // Given
        val resource = researchSubjectTemplate.copy()

        val rule = BlurRule(
            targetTimeZone = "any",
            researchSubject = BlurFunction.START_OF_DAY
        )
        // When
        val result = ResearchSubjectAnonymizer(
            DateTimeConcealerStub()
        ).anonymize(
            resource,
            rule
        )

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it reflects the given ResearchSubject, if the Periode has neigther start nor end`() {
        // Given
        val resource = researchSubjectTemplate.copy(
            period = Period()
        )

        val rule = BlurRule(
            targetTimeZone = "any",
            researchSubject = BlurFunction.START_OF_DAY
        )
        // When
        val result = ResearchSubjectAnonymizer(
            DateTimeConcealerStub()
        ).anonymize(
            resource,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it blurs the start of the period`() {
        // Given
        val expected = XsDateTime(date = XsDate(42, 12, 23))
        val startDateTimeValue = XsDateTime(date = XsDate(1, 2, 3))

        val resource = researchSubjectTemplate.copy(
            period = Period(
                start = DateTime(
                    value = startDateTimeValue
                )
            )
        )

        var capturedXsDateTime: XsDateTime? = null
        var capturedTargetZone: TargetTimeZone? = null
        var capturedBlurFunction: BlurFunction? = null

        val concealer = DateTimeConcealerStub()

        concealer.whenBlur = { delegatedXsDateTime, delegatedTargetZone, delegatedBlurFunction ->
            capturedXsDateTime = delegatedXsDateTime
            capturedTargetZone = delegatedTargetZone
            capturedBlurFunction = delegatedBlurFunction

            expected
        }

        val rule = BlurRule(
            targetTimeZone = "any",
            researchSubject = BlurFunction.START_OF_DAY
        )
        // When
        val result = ResearchSubjectAnonymizer(concealer).anonymize(
            resource,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource.copy(
                period = resource.period!!.copy(
                    start = resource.period!!.start!!.copy(
                        value = expected
                    )
                )
            )
        )

        assertSame(
            actual = capturedXsDateTime,
            expected = startDateTimeValue
        )
        assertSame(
            actual = capturedTargetZone,
            expected = rule.targetTimeZone
        )
        assertSame(
            actual = capturedBlurFunction,
            expected = rule.researchSubject
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it blurs the end of the period`() {
        // Given
        val expected = XsDateTime(date = XsDate(42, 12, 23))
        val endDateTimeValue = XsDateTime(date = XsDate(1, 2, 3))

        val resource = researchSubjectTemplate.copy(
            period = Period(
                end = DateTime(
                    value = endDateTimeValue
                )
            )
        )

        var capturedXsDateTime: XsDateTime? = null
        var capturedTargetZone: TargetTimeZone? = null
        var capturedBlurFunction: BlurFunction? = null

        val concealer = DateTimeConcealerStub()

        concealer.whenBlur = { delegatedXsDateTime, delegatedTargetZone, delegatedBlurFunction ->
            capturedXsDateTime = delegatedXsDateTime
            capturedTargetZone = delegatedTargetZone
            capturedBlurFunction = delegatedBlurFunction

            expected
        }

        val rule = BlurRule(
            targetTimeZone = "any",
            researchSubject = BlurFunction.START_OF_DAY
        )
        // When
        val result = ResearchSubjectAnonymizer(concealer).anonymize(
            resource,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource.copy(
                period = resource.period!!.copy(
                    end = resource.period!!.end!!.copy(
                        value = expected
                    )
                )
            )
        )

        assertSame(
            actual = capturedXsDateTime,
            expected = endDateTimeValue
        )
        assertSame(
            actual = capturedTargetZone,
            expected = rule.targetTimeZone
        )
        assertSame(
            actual = capturedBlurFunction,
            expected = rule.researchSubject
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and BlurRule it blurs the start and end of the period`() {
        // Given
        val expectedStart = XsDateTime(date = XsDate(23, 12, 23))
        val expectedEnd = XsDateTime(date = XsDate(42, 12, 23))

        val startDateTimeValue = XsDateTime(date = XsDate(42, 2, 3))
        val endDateTimeValue = XsDateTime(date = XsDate(1, 2, 3))

        val resource = researchSubjectTemplate.copy(
            period = Period(
                start = DateTime(
                    value = startDateTimeValue
                ),
                end = DateTime(
                    value = endDateTimeValue
                )
            )
        )

        val concealer = DateTimeConcealerStub()

        concealer.whenBlur = { delegatedXsDateTime, _, _ ->
            if (delegatedXsDateTime == startDateTimeValue) {
                expectedStart
            } else {
                expectedEnd
            }
        }

        val rule = BlurRule(
            targetTimeZone = "any",
            researchSubject = BlurFunction.START_OF_DAY
        )
        // When
        val result = ResearchSubjectAnonymizer(concealer).anonymize(
            resource,
            rule
        )

        // Then
        assertEquals(
            actual = result,
            expected = resource.copy(
                period = resource.period!!.copy(
                    start = resource.period!!.end!!.copy(
                        value = expectedStart
                    ),
                    end = resource.period!!.end!!.copy(
                        value = expectedEnd
                    )
                )
            )
        )
    }
}
