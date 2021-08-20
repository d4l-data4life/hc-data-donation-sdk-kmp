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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsentSignatureErrorHandlerTest {
    @Test
    fun `It fulfils ConsentSignatureErrorHandler`() {
        val switch: Any = ConsentSignatureErrorHandler

        assertTrue(switch is ConsentSignatureContract.ApiService.ErrorHandler)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // / When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.Unauthorized)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 403 as ForbiddenError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Forbidden)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.Forbidden)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.NotFound)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.DocumentConflict)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnprocessableEntity)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 429 as TooManyRequestsError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.TooManyRequests)
    }

    @Test
    fun `Given handleEnableSigning is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.InternalServer)
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentSignatureErrorHandler.handleSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // When
        val result = ConsentSignatureErrorHandler.handleSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.Unauthorized)
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // When
        val result = ConsentSignatureErrorHandler.handleEnableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.NotFound)
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // When
        val result = ConsentSignatureErrorHandler.handleSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.DocumentConflict)
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // When
        val result = ConsentSignatureErrorHandler.handleSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnprocessableEntity)
    }

    @Test
    fun `Given handleSigning is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentSignatureErrorHandler.handleSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.InternalServer)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnexpectedFailure)
        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.Unauthorized)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 403 as Forbidden`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Forbidden)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.Forbidden)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.NotFound)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.DocumentConflict)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.UnprocessableEntity)
    }

    @Test
    fun `Given handleDisableSigning is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // When
        val result = ConsentSignatureErrorHandler.handleDisableSigning(error)

        // Then
        assertTrue(result is ConsentSignatureError.InternalServer)
    }
}
