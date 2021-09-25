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

import care.data4life.datadonation.donation.fhir.AllowedReference
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.stu3.model.FhirObservation
import care.data4life.hl7.fhir.stu3.model.FhirQuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.FhirResearchSubject
import care.data4life.hl7.fhir.stu3.model.FhirResource

internal class ResourceValidator(
    private val questionnaireResponseValidator: FhirResourceValidatorContract.QuestionnaireResponseValidator,
    private val observationValidator: FhirResourceValidatorContract.ObservationValidator,
    private val researchSubjectValidator: FhirResourceValidatorContract.ResearchSubjectValidator
) : FhirResourceValidatorContract.ResourceValidator {
    override fun canBeDonated(
        resource: FhirResource,
        studyId: String,
        blurMapping: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): Boolean {
        return when (resource) {
            is FhirQuestionnaireResponse -> questionnaireResponseValidator.canBeDonated(resource, blurMapping)
            is FhirObservation -> observationValidator.canBeDonated(resource, blurMapping)
            is FhirResearchSubject -> researchSubjectValidator.canBeDonated(resource, studyId)
            else -> false
        }
    }
}
