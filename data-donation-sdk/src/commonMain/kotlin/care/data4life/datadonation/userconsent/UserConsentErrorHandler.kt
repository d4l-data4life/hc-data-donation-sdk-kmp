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

package care.data4life.datadonation.userconsent

import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.http.HttpStatusCode

internal object UserConsentErrorHandler : UserConsentContract.ApiService.ErrorHandler {
    private fun mapError(
        error: HttpRuntimeError,
        map: Map<HttpStatusCode, UserConsentError>
    ): UserConsentError {
        return map.getOrElse(error.statusCode) {
            UserConsentError.UnexpectedFailure(error.statusCode.value)
        }
    }

    override fun handleFetchUserConsents(error: HttpRuntimeError): UserConsentError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to UserConsentError.InternalServer()
            )
        )
    }

    override fun handleCreateUserConsent(error: HttpRuntimeError): UserConsentError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to UserConsentError.BadRequest(),
                HttpStatusCode.NotFound to UserConsentError.NotFound(),
                HttpStatusCode.UnprocessableEntity to UserConsentError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to UserConsentError.InternalServer()
            )
        )
    }

    override fun handleRevokeUserConsent(error: HttpRuntimeError): UserConsentError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to UserConsentError.Unauthorized(),
                HttpStatusCode.Forbidden to UserConsentError.Forbidden(),
                HttpStatusCode.NotFound to UserConsentError.NotFound(),
                HttpStatusCode.Conflict to UserConsentError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to UserConsentError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to UserConsentError.InternalServer(),
            )
        )
    }
}
