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

internal class Fhir3ObservationWrapper(
    private val observation: Fhir3Observation
) : CompatibilityWrapperContract.Observation<Fhir3Observation, Fhir3Coding> {
    override val coding: CompatibilityWrapperContract.CodingList<Fhir3Coding>?
        get() {
            return if (observation.code.coding is List<*>) {
                Fhir3CodingListWrapper(observation.code.coding!!)
            } else {
                null
            }
        }
    override val valueQuantity: CompatibilityWrapperContract.Quantity?
        get() {
            return if (observation.valueQuantity is Fhir3Quantity) {
                Fhir3QuantityWrapper(observation.valueQuantity!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir3Observation {
        return observation
    }
}

internal class Fhir4ObservationWrapper(
    private val observation: Fhir4Observation
) : CompatibilityWrapperContract.Observation<Fhir4Observation, Fhir4Coding> {
    override val coding: CompatibilityWrapperContract.CodingList<Fhir4Coding>?
        get() {
            return if (observation.code.coding is List<*>) {
                Fhir4CodingListWrapper(observation.code.coding!!)
            } else {
                null
            }
        }
    override val valueQuantity: CompatibilityWrapperContract.Quantity?
        get() {
            return if (observation.valueQuantity is Fhir4Quantity) {
                Fhir4QuantityWrapper(observation.valueQuantity!!)
            } else {
                null
            }
        }

    override fun unwrap(): Fhir4Observation {
        return observation
    }
}
