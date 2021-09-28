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

import care.data4life.hl7.fhir.r4.primitive.Decimal
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Fhir4QuantityTest {
    @Test
    fun `It fulfils Qunatity`() {
        val quantity: Any = Fhir4QuantityWrapper(
            Fhir4Quantity()
        )

        assertTrue(quantity is CompatibilityWrapperContract.Quantity)
    }

    @Test
    fun `Given hasValue is called it returns false, if the wrapped Qunatity has no Value`() {
        // Given
        val givenQuantity = Fhir4Quantity()

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasValue()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given hasValue is called it returns true, if the wrapped Qunatity has a Value`() {
        // Given
        val givenQuantity = Fhir4Quantity(
            value = Decimal(0.2)
        )

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasValue()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given hasCode is called it returns false, if the wrapped Qunatity has no Code`() {
        // Given
        val givenQuantity = Fhir4Quantity()

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasCode()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given hasCode is called it returns true, if the wrapped Qunatity has an Code`() {
        // Given
        val givenQuantity = Fhir4Quantity(
            code = "23"
        )

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasCode()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given hasSystem is called it returns false, if the wrapped Qunatity has no System`() {
        // Given
        val givenQuantity = Fhir4Quantity()

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasSystem()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given hasSystem is called it returns true, if the wrapped Qunatity has an System`() {
        // Given
        val givenQuantity = Fhir4Quantity(
            system = "42"
        )

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasSystem()

        // Then
        assertTrue(actual)
    }

    @Test
    fun `Given hasUnit is called it returns false, if the wrapped Qunatity has no Unit`() {
        // Given
        val givenQuantity = Fhir4Quantity()

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasUnit()

        // Then
        assertFalse(actual)
    }

    @Test
    fun `Given hasUnit is called it returns true, if the wrapped Qunatity has a Unit`() {
        // Given
        val givenQuantity = Fhir4Quantity(
            unit = "potato"
        )

        // When
        val actual = Fhir4QuantityWrapper(givenQuantity).hasUnit()

        // Then
        assertTrue(actual)
    }
}
