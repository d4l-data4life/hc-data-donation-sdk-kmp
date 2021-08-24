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
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceBlur
import care.data4life.hl7.fhir.stu3.model.Coding
import care.data4life.hl7.fhir.stu3.model.FhirObservation
import care.data4life.hl7.fhir.stu3.model.FhirQuantity
import care.data4life.hl7.fhir.stu3.primitive.Decimal

internal object ObservationValidator : FhirResourceValidatorContract.ObservationValidator {
    private fun hasQuantityValue(
        quantity: FhirQuantity
    ): Boolean {
        return quantity.value is Decimal ||
            quantity.code is String ||
            quantity.system is String ||
            quantity.unit is String
    }

    private fun matchesReference(
        resource: FhirObservation,
        blurMapping: Map<AllowedReference, ProgramFhirResourceBlur?>
    ): Boolean {
        val code = resource.code.coding?.first()

        return if (code is Coding) {
            blurMapping.containsKey(
                "Observation${REFERENCE_SEPARATOR}${code.system}${REFERENCE_SEPARATOR}${code.code}"
            )
        } else {
            false
        }
    }

    override fun isAllowed(
        resource: FhirObservation,
        blurMapping: Map<AllowedReference, ProgramFhirResourceBlur?>
    ): Boolean {
        return when {
            resource.valueQuantity == null -> false
            resource.code.coding.isNullOrEmpty() -> false
            else -> matchesReference(resource, blurMapping) &&
                hasQuantityValue(resource.valueQuantity!!)
        }
    }
}
