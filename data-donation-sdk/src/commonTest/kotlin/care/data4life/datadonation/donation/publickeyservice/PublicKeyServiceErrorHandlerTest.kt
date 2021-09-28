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

package care.data4life.datadonation.donation.publickeyservice

import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PublicKeyServiceErrorHandlerTest {
    @Test
    fun `It fulfils PublicKeyServiceErrorHandler`() {
        val switch: Any = PublicKeyServiceErrorHandler

        assertTrue(switch is PublicKeyServiceContract.ApiService.ErrorHandler)
    }

    @Test
    fun `Given handleFetchPublicKeys is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = PublicKeyServiceErrorHandler.handleFetchPublicKeys(error)

        // Then
        assertTrue(result is PublicKeyServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleLatestUpdate is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = PublicKeyServiceErrorHandler.handleFetchLatestUpdate(error)

        // Then
        assertTrue(result is PublicKeyServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }
}
