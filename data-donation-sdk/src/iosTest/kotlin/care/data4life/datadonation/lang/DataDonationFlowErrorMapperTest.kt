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

package care.data4life.datadonation.lang

import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DataDonationFlowErrorMapperTest {
    @Test
    fun `It fulfils the ErrorMapperContract`() {
        val mapper: Any = DataDonationFlowErrorMapper

        assertTrue(mapper is DomainErrorMapperContract)
    }

    @Test
    fun `Given mapError is called with a Throwable it maps to a generic error by default`() {
        // Given
        val error = RuntimeException()

        // When
        val result = DataDonationFlowErrorMapper.mapError(error)

        // Then
        assertEquals(
            actual = result.code,
            expected = 815
        )
        assertEquals(
            actual = result.domain,
            expected = "care.data4life.datadonation"
        )
        assertEquals(
            actual = result.localizedDescription,
            expected = "Internal failure"
        )
        assertSame(
            actual = result.userInfo["kotlinError"],
            expected = error
        )
    }
}
