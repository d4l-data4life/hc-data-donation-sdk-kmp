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

package care.data4life.datadonation.donation.donationservice

import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DonationServiceErrorHandlerTest {
    @Test
    fun `It fulfils DonationServiceErrorHandler`() {
        val handler: Any = DonationServiceErrorHandler

        assertTrue(handler is DonationServiceContract.ApiService.ErrorHandler)
    }

    @Test
    fun `Given handleFetchToken is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = DonationServiceErrorHandler.handleFetchToken(error)

        // Then
        assertTrue(result is DonationServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleFetchToken is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = DonationServiceErrorHandler.handleFetchToken(error)

        // Then
        assertTrue(result is DonationServiceError.InternalServer)
    }

    @Test
    fun `Given handleRegister is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = DonationServiceErrorHandler.handleRegister(error)

        // Then
        assertTrue(result is DonationServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRegister is called with HttpRuntimeError, it propagates it as 400 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.BadRequest)

        // When
        val result = DonationServiceErrorHandler.handleRegister(error)

        // Then
        assertTrue(result is DonationServiceError.BadRequest)
    }

    @Test
    fun `Given handleRegister is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = DonationServiceErrorHandler.handleRegister(error)

        // Then
        assertTrue(result is DonationServiceError.InternalServer)
    }

    @Test
    fun `Given handleDonate is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = DonationServiceErrorHandler.handleDonate(error)

        // Then
        assertTrue(result is DonationServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleDonate is called with HttpRuntimeError, it propagates it as 400 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.BadRequest)

        // When
        val result = DonationServiceErrorHandler.handleDonate(error)

        // Then
        assertTrue(result is DonationServiceError.BadRequest)
    }

    @Test
    fun `Given handleDonate is called with HttpRuntimeError, it propagates it as 401 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // When
        val result = DonationServiceErrorHandler.handleDonate(error)

        // Then
        assertTrue(result is DonationServiceError.Unauthorized)
    }

    @Test
    fun `Given handleDonate is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = DonationServiceErrorHandler.handleDonate(error)

        // Then
        assertTrue(result is DonationServiceError.InternalServer)
    }

    @Test
    fun `Given handleRevoke is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = DonationServiceErrorHandler.handleRevoke(error)

        // Then
        assertTrue(result is DonationServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRevoke is called with HttpRuntimeError, it propagates it as 400 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.BadRequest)

        // When
        val result = DonationServiceErrorHandler.handleRevoke(error)

        // Then
        assertTrue(result is DonationServiceError.BadRequest)
    }

    @Test
    fun `Given handleRevoke is called with HttpRuntimeError, it propagates it as 401 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // When
        val result = DonationServiceErrorHandler.handleRevoke(error)

        // Then
        assertTrue(result is DonationServiceError.Unauthorized)
    }

    @Test
    fun `Given handleRevoke is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = DonationServiceErrorHandler.handleRevoke(error)

        // Then
        assertTrue(result is DonationServiceError.InternalServer)
    }
}
