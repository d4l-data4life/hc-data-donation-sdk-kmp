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
import care.data4life.datadonation.donation.fhir.wrapper.Fhir3QuestionnaireResponseWrapper
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.Reference
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QuestionnaireResponseValidatorTest {
    @Test
    fun `It fulfils QuestionnaireResponseValidator`() {
        val filter: Any = QuestionnaireResponseValidator

        assertTrue(filter is FhirResourceValidatorContract.QuestionnaireResponseValidator)
    }

    @Test
    fun `Given isAllowed with a QuestionnaireResponse and BlurMapping, it returns false if the BlurMapping does not contain the reference of the QuestionnaireResponse`() {
        // Given
        val questionnaireResponse = Fhir3QuestionnaireResponseWrapper(
            QuestionnaireResponse(
                status = QuestionnaireResponseStatus.COMPLETED
            )
        )
        val blurMapping = mapOf(
            "something" to QuestionnaireResponseBlur(
                questionnaireResponseItemBlurs = emptyList()
            )
        )

        // When
        val result = QuestionnaireResponseValidator.canBeDonated(
            questionnaireResponse as CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
            blurMapping
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a QuestionnaireResponse and BlurMapping, it returns true if the BlurMapping contains the reference of the QuestionnaireResponse`() {
        // Given
        val reference = "reference"

        val questionnaireResponse = Fhir3QuestionnaireResponseWrapper(
            QuestionnaireResponse(
                status = QuestionnaireResponseStatus.COMPLETED,
                questionnaire = Reference(
                    reference = reference
                )
            )
        )
        val blurMapping = mapOf(
            reference to QuestionnaireResponseBlur(
                questionnaireResponseItemBlurs = emptyList()
            )
        )

        // When
        val result = QuestionnaireResponseValidator.canBeDonated(
            questionnaireResponse as CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
            blurMapping
        )

        // Then
        assertTrue(result)
    }
}
