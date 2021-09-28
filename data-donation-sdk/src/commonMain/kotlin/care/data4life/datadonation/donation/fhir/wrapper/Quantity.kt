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

internal class Fhir3QuantityWrapper(
    private val quantity: Fhir3Quantity
) : CompatibilityWrapperContract.Quantity {
    override fun hasValue(): Boolean {
        return quantity.value is Fhir3Decimal
    }

    override fun hasCode(): Boolean {
        return quantity.code is String
    }

    override fun hasSystem(): Boolean {
        return quantity.system is String
    }

    override fun hasUnit(): Boolean {
        return quantity.unit is String
    }
}

internal class Fhir4QuantityWrapper(
    private val quantity: Fhir4Quantity
) : CompatibilityWrapperContract.Quantity {
    override fun hasValue(): Boolean {
        return quantity.value is Fhir4Decimal
    }

    override fun hasCode(): Boolean {
        return quantity.code is String
    }

    override fun hasSystem(): Boolean {
        return quantity.system is String
    }

    override fun hasUnit(): Boolean {
        return quantity.unit is String
    }
}
