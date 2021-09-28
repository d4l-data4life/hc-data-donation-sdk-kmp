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

import care.data4life.datadonation.donation.fhir.validator.FhirResourceValidatorContract.ResearchSubjectValidator.Companion.INDIVIDUAL_IDENTIFIER_SYSTEM
import care.data4life.datadonation.donation.fhir.validator.FhirResourceValidatorContract.ResearchSubjectValidator.Companion.STUDY_IDENTIFIER_SYSTEM
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract
import care.data4life.hl7.fhir.FhirVersion

internal object ResearchSubjectValidator : FhirResourceValidatorContract.ResearchSubjectValidator {
    override fun canBeDonated(
        resource: CompatibilityWrapperContract.ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
        studyId: String
    ): Boolean {
        return when {
            resource.studyIdentifierSystem != STUDY_IDENTIFIER_SYSTEM -> false
            resource.studyIdentifierValue != studyId -> false
            resource.individualIdentifierSystem != INDIVIDUAL_IDENTIFIER_SYSTEM -> false
            else -> true
        }
    }
}
