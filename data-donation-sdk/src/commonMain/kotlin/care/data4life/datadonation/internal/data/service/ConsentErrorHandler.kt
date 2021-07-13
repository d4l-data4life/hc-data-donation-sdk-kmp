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

internal object ConsentErrorHandler : ServiceContract.ConsentService.ConsentErrorHandler {
    private fun mapAndThrowError(
        error: HttpRuntimeError,
        map: Map<HttpStatusCode, ConsentServiceError>
    ) {
        throw map.getOrElse(error.statusCode) {
            ConsentServiceError.UnexpectedError(error.statusCode.value)
        }
    }

    override fun handleFetchConsentDocuments(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }

    override fun handleFetchUserConsents(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }

    override fun handleCreateUserConsent(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to ConsentServiceError.BadRequestError(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFoundError(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntityError(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }

    override fun handleRequestSignatureConsentRegistration(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.UnauthorizedError(),
                HttpStatusCode.Forbidden to ConsentServiceError.ForbiddenError(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFoundError(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflictError(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntityError(),
                HttpStatusCode.TooManyRequests to ConsentServiceError.TooManyRequestsError(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }

    override fun handleRequestSignatureDonation(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.UnauthorizedError(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflictError(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntityError(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }

    override fun handleRevokeUserConsent(error: HttpRuntimeError) {
        mapAndThrowError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.UnauthorizedError(),
                HttpStatusCode.Forbidden to ConsentServiceError.ForbiddenError(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFoundError(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflictError(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntityError(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServerError()
            )
        )
    }
}
