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

internal object ConsentSignatureErrorHandler : ConsentSignatureContract.ApiService.ErrorHandler {
    private fun mapError(
        error: HttpRuntimeError,
        map: Map<HttpStatusCode, ConsentSignatureError>
    ): ConsentSignatureError {
        return map.getOrElse(error.statusCode) {
            ConsentSignatureError.UnexpectedFailure(error.statusCode.value)
        }
    }

    override fun handleEnableSigning(error: HttpRuntimeError): ConsentSignatureError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentSignatureError.Unauthorized(),
                HttpStatusCode.Forbidden to ConsentSignatureError.Forbidden(),
                HttpStatusCode.NotFound to ConsentSignatureError.NotFound(),
                HttpStatusCode.Conflict to ConsentSignatureError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentSignatureError.UnprocessableEntity(),
                HttpStatusCode.TooManyRequests to ConsentSignatureError.TooManyRequests(),
                HttpStatusCode.InternalServerError to ConsentSignatureError.InternalServer()
            )
        )
    }

    override fun handleSigning(error: HttpRuntimeError): ConsentSignatureError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentSignatureError.Unauthorized(),
                HttpStatusCode.NotFound to ConsentSignatureError.NotFound(),
                HttpStatusCode.Conflict to ConsentSignatureError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentSignatureError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to ConsentSignatureError.InternalServer()
            )
        )
    }

    override fun handleDisableSigning(error: HttpRuntimeError): ConsentSignatureError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.Unauthorized to ConsentSignatureError.Unauthorized(),
                HttpStatusCode.Forbidden to ConsentSignatureError.Forbidden(),
                HttpStatusCode.NotFound to ConsentSignatureError.NotFound(),
                HttpStatusCode.Conflict to ConsentSignatureError.DocumentConflict(),
                HttpStatusCode.UnprocessableEntity to ConsentSignatureError.UnprocessableEntity(),
                HttpStatusCode.InternalServerError to ConsentSignatureError.InternalServer()
            )
        )
    }
}
