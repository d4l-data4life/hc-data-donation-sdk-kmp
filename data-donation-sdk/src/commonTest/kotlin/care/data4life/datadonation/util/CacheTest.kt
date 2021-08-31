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

package care.data4life.datadonation.util

import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.stub.ClockStub
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CacheTest {
    @Test
    fun `It fulfils the CacheContract`() {
        val cache: Any = Cache(ClockStub(), 23)

        assertTrue(cache is CacheContract)
    }

    @Test
    fun `Given fetch is called, it returns an empty String, if no value was updated`() {
        // Given
        val clock = ClockStub()

        clock.whenNow = { Instant.fromEpochSeconds(0) }

        // When
        val result = Cache(clock, 23).fetch()

        // Then
        assertEquals(
            actual = result,
            expected = ""
        )
    }

    @Test
    fun `Given fetch is called and the value was updated, it returns the value, if it is not expired`() {
        // Given
        val clock = ClockStub()
        val timePassed = mutableListOf(
            Instant.fromEpochSeconds(0),
            Instant.fromEpochSeconds(22)
        )
        val value = "ABC"

        clock.whenNow = { timePassed.removeAt(0) }

        // When
        val cache = Cache(clock, 23)

        cache.update(value)
        val result = cache.fetch()

        // Then
        assertEquals(
            actual = result,
            expected = value
        )
    }

    @Test
    fun `Given fetch is called and the value was updated, it fails, if it is expired`() {
        // Given
        val clock = ClockStub()
        val value = "ABC"

        val timePassed = mutableListOf(
            Instant.fromEpochSeconds(0),
            Instant.fromEpochSeconds(23)
        )

        clock.whenNow = { timePassed.removeAt(0) }

        // When
        val cache = Cache(clock, 23)

        cache.update(value)

        val error = assertFailsWith<CoreRuntimeError.InternalFailure> {
            cache.fetch()
        }

        assertEquals(
            actual = error.message,
            expected = "Cache expired"
        )
    }
}
