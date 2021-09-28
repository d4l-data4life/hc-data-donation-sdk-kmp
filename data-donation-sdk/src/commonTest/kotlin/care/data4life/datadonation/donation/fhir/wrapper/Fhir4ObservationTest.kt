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

import care.data4life.hl7.fhir.r4.codesystem.ObservationStatus
import care.data4life.hl7.fhir.r4.model.CodeableConcept
import care.data4life.hl7.fhir.r4.model.Coding
import care.data4life.hl7.fhir.r4.model.Quantity
import care.data4life.hl7.fhir.r4.primitive.Decimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir4ObservationTest {
    @Test
    fun `It fulfils Observation`() {
        val observation: Any = Fhir4ObservationWrapper(
            Fhir4Observation(
                status = ObservationStatus.FINAL,
                code = CodeableConcept()
            )
        )

        assertTrue(observation is CompatibilityWrapperContract.Observation<*, *>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir4Observation`() {
        // Given
        val givenObservation = Fhir4Observation(
            status = ObservationStatus.FINAL,
            code = CodeableConcept()
        )

        // When
        val actual = Fhir4ObservationWrapper(givenObservation).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenObservation
        )
    }

    @Test
    fun `It exposes null for coding, if the wrapped Fhir4Observation has no Code Coding`() {
        val givenObservation = Fhir4Observation(
            status = ObservationStatus.FINAL,
            code = CodeableConcept()
        )

        // When
        val actual = Fhir4ObservationWrapper(givenObservation).coding

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes coding, if the wrapped Fhir4Observation has a Code Coding`() {
        val givenObservation = Fhir4Observation(
            status = ObservationStatus.FINAL,
            code = CodeableConcept(
                coding = listOf(
                    Coding()
                )
            )
        )

        // When
        val actual: Any? = Fhir4ObservationWrapper(givenObservation).coding

        // Then
        assertTrue(actual is CompatibilityWrapperContract.CodingList<*>)

        assertSame(
            actual = actual.unwrap(),
            expected = givenObservation.code.coding
        )
    }

    @Test
    fun `It exposes null for valueQunatity, if the wrapped Fhir4Observation has no ValueQunatity`() {
        val givenObservation = Fhir4Observation(
            status = ObservationStatus.FINAL,
            code = CodeableConcept()
        )

        // When
        val actual = Fhir4ObservationWrapper(givenObservation).coding

        // Then
        assertNull(actual)
    }

    @Test
    fun `It exposes valueQunatity, if the wrapped Fhir4Observation has a ValueQunatity`() {
        val givenObservation = Fhir4Observation(
            status = ObservationStatus.FINAL,
            code = CodeableConcept(),
            valueQuantity = Quantity(
                value = Decimal(42.0)
            )
        )

        // When
        val actual: Any? = Fhir4ObservationWrapper(givenObservation).valueQuantity

        // Then
        assertTrue(actual is CompatibilityWrapperContract.Quantity)

        assertEquals(
            actual = actual.hasValue(),
            expected = givenObservation.valueQuantity?.value is Decimal
        )
    }
}
