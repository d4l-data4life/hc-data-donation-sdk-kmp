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

package care.data4life.datadonation.donation.fhir.wrapper

import care.data4life.hl7.fhir.r4.codesystem.ResearchSubjectStatus
import care.data4life.hl7.fhir.r4.model.Identifier
import care.data4life.hl7.fhir.r4.model.Reference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4ResearchSubjectTest {
    @Test
    fun `It fulfils ResearchSubject`() {
        val researchSubject: Any = Fhir4ResearchSubjectWrapper(
            Fhir4ResearchSubject(
                status = ResearchSubjectStatus.WITHDRAWN,
                study = Reference(),
                individual = Reference()
            )
        )

        assertTrue(researchSubject is CompatibilityWrapperContract.ResearchSubject<*, *, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir4ResearchSubject`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenResearchSubject
        )
    }

    @Test
    fun `It exposes the StudyIdentifierSystem if the Identifier is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierSystem

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the StudyIdentifierSystem if the Identifier System is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(
                identifier = Identifier()
            ),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierSystem

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the StudyIdentifierSystem with a value`() {
        // Given
        val system = "abc"
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(
                identifier = Identifier(
                    system = system
                )
            ),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierSystem

        // Then
        assertEquals(
            actual = actual,
            expected = system
        )
    }

    @Test
    fun `It exposes the StudyIdentifierValue if the Identifier is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierValue

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the StudyIdentifierValue if the Identifier Value is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(
                identifier = Identifier()
            ),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierValue

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the StudyIdentifierValue with a value`() {
        // Given
        val value = "value"
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(
                identifier = Identifier(
                    value = value
                )
            ),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).studyIdentifierValue

        // Then
        assertEquals(
            actual = actual,
            expected = value
        )
    }

    @Test
    fun `It exposes the IndividualIdentifierSystem if the Identifier is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).individualIdentifierSystem

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the IndividualIdentifierSystem if the Identifier System is null`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference(
                identifier = Identifier()
            )
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).individualIdentifierSystem

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes the IndividualIdentifierSystem with a value`() {
        // Given
        val system = "system"
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference(
                identifier = Identifier(
                    system = system
                )
            )
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).individualIdentifierSystem

        // Then
        assertEquals(
            actual = actual,
            expected = system
        )
    }

    @Test
    fun `It exposes null, if the wrapped ResearchSubject has no Period`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).period

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes a wrapped Period, if the wrapped ResearchSubject has a Period`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference(),
            period = Fhir4Period()
        )

        // When
        val actual: Any? = Fhir4ResearchSubjectWrapper(givenResearchSubject).period

        // Then
        assertTrue(actual is CompatibilityWrapperContract.Period<*, *>)

        assertSame(
            actual = actual.unwrap(),
            expected = givenResearchSubject.period
        )
    }

    @Test
    fun `Given copy is called with null as Period, returns a ResearchSubject which has no Period`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference(),
            period = Fhir4Period()
        )

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).copy(
            period = null
        )

        // Then
        assertNull(actual.period)
        assertNull(actual.unwrap().period)
    }

    @Test
    fun `Given copy is called with a wrapped Period, returns a ResearchSubject which has the modified Period`() {
        // Given
        val givenResearchSubject = Fhir4ResearchSubject(
            status = ResearchSubjectStatus.WITHDRAWN,
            study = Reference(),
            individual = Reference(),
        )

        val newValue = Fhir4PeriodWrapper(Fhir4Period())

        // When
        val actual = Fhir4ResearchSubjectWrapper(givenResearchSubject).copy(
            period = newValue
        )

        // Then
        assertSame(
            actual = actual.period?.unwrap(),
            expected = newValue.unwrap()
        )
        assertSame(
            actual = actual.unwrap().period,
            expected = newValue.unwrap()
        )
    }
}
