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

internal object ConsentErrorHandler : ConsentContract.ConsentApiService.ConsentErrorHandler {
    private fun mapError(
        error: HttpRuntimeError,
        map: Map<HttpStatusCode, ConsentServiceError>
    ): ConsentServiceError {
        return map.getOrElse(error.statusCode) {
            ConsentServiceError.UnexpectedFailure(error.statusCode.value)
        }
    }

    override fun handleFetchConsentDocuments(error: HttpRuntimeError): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer()
            )
        )
    }

    override fun handleFetchUserConsents(error: HttpRuntimeError): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer()
            )
        )
    }

    override fun handleCreateUserConsent(error: HttpRuntimeError): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to ConsentServiceError.BadRequest(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFound(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer()
            )
        )
    }

    override fun handleRequestSignatureConsentRegistration(
        error: HttpRuntimeError
    ): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.Unauthorized(),
                HttpStatusCode.Forbidden to ConsentServiceError.Forbidden(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFound(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntity(),
                HttpStatusCode.TooManyRequests to ConsentServiceError.TooManyRequests(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer()
            )
        )
    }

    override fun handleRequestSignatureDonation(error: HttpRuntimeError): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.Unauthorized(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer()
            )
        )
    }

    override fun handleRevokeUserConsent(error: HttpRuntimeError): ConsentServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentServiceError.Unauthorized(),
                HttpStatusCode.Forbidden to ConsentServiceError.Forbidden(),
                HttpStatusCode.NotFound to ConsentServiceError.NotFound(),
                HttpStatusCode.Conflict to ConsentServiceError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentServiceError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to ConsentServiceError.InternalServer(),
            )
        )
    }
}
