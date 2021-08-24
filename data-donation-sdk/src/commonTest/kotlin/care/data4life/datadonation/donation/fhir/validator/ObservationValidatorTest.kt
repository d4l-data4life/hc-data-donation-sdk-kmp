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

import care.data4life.datadonation.donation.program.model.ProgramFhirResourceBlur
import care.data4life.hl7.fhir.stu3.codesystem.ObservationStatus
import care.data4life.hl7.fhir.stu3.model.CodeableConcept
import care.data4life.hl7.fhir.stu3.model.Coding
import care.data4life.hl7.fhir.stu3.model.Observation
import care.data4life.hl7.fhir.stu3.model.Quantity
import care.data4life.hl7.fhir.stu3.primitive.Decimal
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObservationValidatorTest {
    @Test
    fun `It fulfils ObservationValidator`() {
        val validator: Any = ObservationValidator

        assertTrue(validator is FhirResourceValidatorContract.ObservationValidator)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns false if the Observation has no valueQuantity`() {
        // Given
        val reference = "reference"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept()
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns false if the Observation has no CodeItem`() {
        // Given
        val reference = "reference"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = emptyList()
            ),
            valueQuantity = Quantity()
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns false if the BlurMapping matches not the first Coding`() {
        // Given
        val reference = "reference"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(Coding())
            ),
            valueQuantity = Quantity()
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns false if the Quantity fulfils not the minimum requirements`() {
        // Given
        val code = "abc"
        val system = "dfg"

        val reference = "Observation|$system|$code"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(
                    Coding(
                        system = system,
                        code = code
                    )
                )
            ),
            valueQuantity = Quantity()
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns true if the BlurMapping contains a the first Coding and Quantity fulfils has a value`() {
        // Given
        val code = "abc"
        val system = "dfg"

        val reference = "Observation|$system|$code"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(
                    Coding(
                        system = system,
                        code = code
                    )
                )
            ),
            valueQuantity = Quantity(
                value = Decimal(0.1)
            )
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns true if the BlurMapping contains a the first Coding and Quantity fulfils has a unit`() {
        // Given
        val code = "abc"
        val system = "dfg"

        val reference = "Observation|$system|$code"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(
                    Coding(
                        system = system,
                        code = code
                    )
                )
            ),
            valueQuantity = Quantity(
                unit = "something"
            )
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns true if the BlurMapping contains a the first Coding and Quantity fulfils has a code`() {
        // Given
        val code = "abc"
        val system = "dfg"

        val reference = "Observation|$system|$code"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(
                    Coding(
                        system = system,
                        code = code
                    )
                )
            ),
            valueQuantity = Quantity(
                code = "something"
            )
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given isAllowed with a Observation and BlurMapping, it returns true if the BlurMapping contains a the first Coding and Quantity fulfils has a system`() {
        // Given
        val code = "abc"
        val system = "dfg"

        val reference = "Observation|$system|$code"

        val observation = Observation(
            status = ObservationStatus.AMENDED,
            code = CodeableConcept(
                coding = listOf(
                    Coding(
                        system = system,
                        code = code
                    )
                )
            ),
            valueQuantity = Quantity(
                system = "something"
            )
        )
        val blurMapping = mapOf(
            reference to ProgramFhirResourceBlur(
                itemBlurs = emptyList()
            )
        )

        // When
        val result = ObservationValidator.isAllowed(
            observation,
            blurMapping
        )

        // Then
        assertTrue(result)
    }
}
