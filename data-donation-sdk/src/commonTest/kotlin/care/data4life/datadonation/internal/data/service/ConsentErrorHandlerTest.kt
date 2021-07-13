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

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.lang.ConsentServiceError
import care.data4life.datadonation.lang.HttpRuntimeError
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ConsentErrorHandlerTest {
    @Test
    fun `It fulfils ConsentErrorHandler`() {
        val switch: Any = ConsentErrorHandler

        assertTrue(switch is ServiceContract.ConsentService.ConsentErrorHandler)
    }

    @Test
    fun `Given handleFetchConsentDocuments is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleFetchConsentDocuments(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleFetchConsentDocuments is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleFetchConsentDocuments(error)
        }
    }

    @Test
    fun `Given handleFetchUserConsents is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleFetchUserConsents(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleFetchUserConsents is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleFetchUserConsents(error)
        }
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleCreateUserConsent(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 400 as BadRequestError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.BadRequest)

        // Then
        assertFailsWith<ConsentServiceError.BadRequestError> {
            // When
            ConsentErrorHandler.handleCreateUserConsent(error)
        }
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // Then
        assertFailsWith<ConsentServiceError.NotFoundError> {
            // When
            ConsentErrorHandler.handleCreateUserConsent(error)
        }
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // Then
        assertFailsWith<ConsentServiceError.UnprocessableEntityError> {
            // When
            ConsentErrorHandler.handleCreateUserConsent(error)
        }
    }

    @Test
    fun `Given handleCreateUserConsent is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleFetchUserConsents(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // Then
        assertFailsWith<ConsentServiceError.UnauthorizedError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 403 as ForbiddenError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Forbidden)

        // Then
        assertFailsWith<ConsentServiceError.ForbiddenError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // Then
        assertFailsWith<ConsentServiceError.NotFoundError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // Then
        assertFailsWith<ConsentServiceError.DocumentConflictError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // Then
        assertFailsWith<ConsentServiceError.UnprocessableEntityError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 429 as TooManyRequestsError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)

        // Then
        assertFailsWith<ConsentServiceError.TooManyRequestsError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureConsentRegistration is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleRequestSignatureConsentRegistration(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureDonation is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleRequestSignatureDonation(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRequestSignatureDonation is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // Then
        assertFailsWith<ConsentServiceError.UnauthorizedError> {
            // When
            ConsentErrorHandler.handleRequestSignatureDonation(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureDonation is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // Then
        assertFailsWith<ConsentServiceError.DocumentConflictError> {
            // When
            ConsentErrorHandler.handleRequestSignatureDonation(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureDonation is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // Then
        assertFailsWith<ConsentServiceError.UnprocessableEntityError> {
            // When
            ConsentErrorHandler.handleRequestSignatureDonation(error)
        }
    }

    @Test
    fun `Given handleRequestSignatureDonation is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleRequestSignatureDonation(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as UnexpectedError by default`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.ServiceUnavailable)

        // Then
        val result = assertFailsWith<ConsentServiceError.UnexpectedError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }

        assertEquals(
            actual = result.httpStatus,
            expected = 503
        )
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 401 as UnauthorizedError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Unauthorized)

        // Then
        assertFailsWith<ConsentServiceError.UnauthorizedError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 403 as ForbiddenError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Forbidden)

        // Then
        assertFailsWith<ConsentServiceError.ForbiddenError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 404 as NotFoundError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.NotFound)

        // Then
        assertFailsWith<ConsentServiceError.NotFoundError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 409 as DocumentConflictError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.Conflict)

        // Then
        assertFailsWith<ConsentServiceError.DocumentConflictError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 422 as UnprocessableEntityError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.UnprocessableEntity)

        // Then
        assertFailsWith<ConsentServiceError.UnprocessableEntityError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }

    @Test
    fun `Given handleRevokeUserConsent is called with HttpRuntimeError, it propagates it as 500 as InternalServerError`() {
        // Given
        val error = HttpRuntimeError(HttpStatusCode.InternalServerError)

        // Then
        assertFailsWith<ConsentServiceError.InternalServerError> {
            // When
            ConsentErrorHandler.handleRevokeUserConsent(error)
        }
    }
}
