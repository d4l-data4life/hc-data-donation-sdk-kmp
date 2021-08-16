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

package care.data4life.datadonation.consent

import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsentErrorHandlerTest {
    @Test
    fun `It fulfils ConsentErrorHandler`() {
        val switch: Any = ConsentErrorHandler

        assertTrue(switch is ConsentContract.ApiService.ErrorHandler)
    }

    @Test
    fun `Given handleFetchUserConsents is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentErrorHandler.handleFetchUserConsents(error)

        // Then
        assertTrue(result is ConsentServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleFetchUserConsents is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentErrorHandler.handleFetchUserConsents(error)

        // Then
        assertTrue(result is ConsentServiceError.InternalServer)
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentErrorHandler.handleCreateUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 400 as BadRequestError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.BadRequest)

        // When
        val result = ConsentErrorHandler.handleCreateUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.BadRequest)
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // When
        val result = ConsentErrorHandler.handleCreateUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.NotFound)
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // When
        val result = ConsentErrorHandler.handleCreateUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.UnprocessableEntity)
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentErrorHandler.handleCreateUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.InternalServer)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.Unauthorized)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 403 as ForbiddenError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Forbidden)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.Forbidden)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.NotFound)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.DocumentConflict)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.UnprocessableEntity)
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentErrorHandler.handleRevokeUserConsent(error)

        // Then
        assertTrue(result is ConsentServiceError.InternalServer)
    }
}
