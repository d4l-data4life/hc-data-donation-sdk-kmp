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

package care.data4life.datadonation.donation.anonymization

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RedactorTest {
    @Test
    fun `It fulfils Redaction`() {
        val redaction: Any = Redactor

        assertTrue(redaction is AnonymizationContract.Redactor)
    }

    @Test
    fun `Given a redact is called with null, it returns null`() {
        // Given
        val valueString = null

        // When
        val result = Redactor.redact(valueString)

        // Then
        assertNull(result)
    }

    @Test
    fun `Given a redact is called with a String, it returns REDACTED`() {
        val valueString = "something"

        // When
        val result = Redactor.redact(valueString)

        // Then
        assertEquals(
            actual = result,
            expected = "REDACTED"
        )
    }
}
