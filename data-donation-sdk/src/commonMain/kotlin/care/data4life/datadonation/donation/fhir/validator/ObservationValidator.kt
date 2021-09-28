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
import care.data4life.datadonation.donation.fhir.FhirContract.FhirResourceBlurMapper.Companion.REFERENCE_SEPARATOR
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion

internal object ObservationValidator : FhirResourceValidatorContract.ObservationValidator {
    private fun hasQuantityValue(
        quantity: CompatibilityWrapperContract.Quantity
    ): Boolean {
        return quantity.hasValue() ||
            quantity.hasCode() ||
            quantity.hasSystem() ||
            quantity.hasUnit()
    }

    private fun matchesReference(
        resource: CompatibilityWrapperContract.Observation<FhirVersion, FhirVersion>,
        blurMapping: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): Boolean {
        val code = resource.coding?.first()

        return if (code is CompatibilityWrapperContract.Coding) {
            blurMapping.containsKey(
                "Observation${REFERENCE_SEPARATOR}${code.system}${REFERENCE_SEPARATOR}${code.code}"
            )
        } else {
            false
        }
    }

    override fun canBeDonated(
        resource: CompatibilityWrapperContract.Observation<FhirVersion, FhirVersion>,
        blurMapping: Map<AllowedReference, QuestionnaireResponseBlur?>
    ): Boolean {
        return if (resource.coding.isNullOrEmpty()) {
            false
        } else {
            matchesReference(resource, blurMapping) && hasQuantityValue(resource.valueQuantity!!)
        }
    }
}
