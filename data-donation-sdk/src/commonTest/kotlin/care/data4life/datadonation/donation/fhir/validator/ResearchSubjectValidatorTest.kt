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

package care.data4life.datadonation.donation.fhir.validator

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract
import care.data4life.datadonation.donation.fhir.wrapper.Fhir3ResearchSubjectWrapper
import care.data4life.hl7.fhir.FhirVersion
import care.data4life.hl7.fhir.stu3.codesystem.ResearchSubjectStatus
import care.data4life.hl7.fhir.stu3.model.Identifier
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.model.ResearchSubject
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ResearchSubjectValidatorTest {
    @Test
    fun `It fulfils ResearchSubjectValidator`() {
        val validator: Any = ResearchSubjectValidator

        assertTrue(validator is FhirResourceValidatorContract.ResearchSubjectValidator)
    }

    @Test
    fun `Given isAllowed with a ResearchSubject and StudyId, it returns false if the ResearchSubject does not statify the StudySystemIdentifier`() {
        // Given
        val researchSubject = Fhir3ResearchSubjectWrapper(
            ResearchSubject(
                status = ResearchSubjectStatus.ACTIVE,
                study = Reference(),
                individual = Reference()
            )
        )

        // When
        val result = ResearchSubjectValidator.canBeDonated(
            researchSubject as CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            "Test"
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a ResearchSubject and StudyId, it returns false if the ResearchSubject does not statify with the StudyIdentifierValues the StudyId`() {
        // Given
        val studyId = "a Test"

        val researchSubject = Fhir3ResearchSubjectWrapper(
            ResearchSubject(
                status = ResearchSubjectStatus.ACTIVE,
                study = Reference(
                    identifier = Identifier(
                        system = "http://fhir.data4life.care/stu3/CodeSystem/research-study-id"
                    )
                ),
                individual = Reference()
            )
        )

        // When
        val result = ResearchSubjectValidator.canBeDonated(
            researchSubject as CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            studyId
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a ResearchSubject and StudyId, it returns false if the ResearchSubject does not statify the IndividualSystemIdentifier`() {
        // Given
        val studyId = "a Test"

        val researchSubject = Fhir3ResearchSubjectWrapper(
            ResearchSubject(
                status = ResearchSubjectStatus.ACTIVE,
                study = Reference(
                    identifier = Identifier(
                        value = studyId,
                        system = "http://fhir.data4life.care/stu3/CodeSystem/research-study-id"
                    )
                ),
                individual = Reference()
            )
        )

        // When
        val result = ResearchSubjectValidator.canBeDonated(
            researchSubject as CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            studyId
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a ResearchSubject and StudyId, it returns true if the ResearchSubject matches all criteria`() {
        // Given
        val studyId = "a Test"

        val researchSubject = Fhir3ResearchSubjectWrapper(
            ResearchSubject(
                status = ResearchSubjectStatus.ACTIVE,
                study = Reference(
                    identifier = Identifier(
                        value = studyId,
                        system = "http://fhir.data4life.care/stu3/CodeSystem/research-study-id"
                    )
                ),
                individual = Reference(
                    identifier = Identifier(
                        system = "http://fhir.data4life.care/stu3/CodeSystem/alp-encrypted-external-id"
                    )
                )
            )
        )

        // When
        val result = ResearchSubjectValidator.canBeDonated(
            researchSubject as CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            studyId
        )

        // Then
        assertTrue(result)
    }
}
