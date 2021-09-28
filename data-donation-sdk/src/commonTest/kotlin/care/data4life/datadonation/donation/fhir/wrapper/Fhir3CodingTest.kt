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

import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

class Fhir3CodingTest {
    @Test
    fun `It fulfils Coding`() {
        val coding: Any = Fhir3CodingWrapper(
            Fhir3Coding()
        )

        assertTrue(coding is CompatibilityWrapperContract.Coding<*>)
    }

    @Test
    fun `Given unwrap is called it returns the given Fhir3Coding`() {
        // Given
        val givenCoding = Fhir3Coding()

        // When
        val actual = Fhir3CodingWrapper(givenCoding).unwrap()

        // Then
        assertSame(
            actual = actual,
            expected = givenCoding
        )
    }

    @Test
    fun `It exposes the code of the wrapped Fhir3Coding`() {
        // Given
        val givenCoding = Fhir3Coding(
            code = "something"
        )

        // When
        val actual = Fhir3CodingWrapper(
            givenCoding
        )

        // Then
        assertSame(
            actual = actual.code,
            expected = givenCoding.code
        )
    }

    @Test
    fun `It exposes the system of the wrapped Fhir3Coding`() {
        // Given
        val givenCoding = Fhir3Coding(
            system = "something"
        )

        // When
        val actual = Fhir3CodingWrapper(
            givenCoding
        )

        // Then
        assertSame(
            actual = actual.system,
            expected = givenCoding.system
        )
    }
}
