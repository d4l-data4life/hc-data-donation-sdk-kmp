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

internal object DonationServiceErrorHandler : DonationServiceContract.ApiService.ErrorHandler {
    private fun mapError(
        error: HttpRuntimeError,
        map: Map<HttpStatusCode, DonationServiceError>
    ): DonationServiceError {
        return map.getOrElse(error.statusCode) {
            DonationServiceError.UnexpectedFailure(error.statusCode.value)
        }
    }

    override fun handleFetchToken(error: HttpRuntimeError): DonationServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.InternalServerError to DonationServiceError.InternalServer()
            )
        )
    }

    override fun handleRegister(error: HttpRuntimeError): DonationServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to DonationServiceError.BadRequest(),
                HttpStatusCode.InternalServerError to DonationServiceError.InternalServer()
            )
        )
    }

    override fun handleDonate(error: HttpRuntimeError): DonationServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to DonationServiceError.BadRequest(),
                HttpStatusCode.Unauthorized to DonationServiceError.Unauthorized(),
                HttpStatusCode.InternalServerError to DonationServiceError.InternalServer()
            )
        )
    }

    override fun handleRevoke(error: HttpRuntimeError): DonationServiceError {
        return mapError(
            error,
            mapOf(
                HttpStatusCode.BadRequest to DonationServiceError.BadRequest(),
                HttpStatusCode.Unauthorized to DonationServiceError.Unauthorized(),
                HttpStatusCode.InternalServerError to DonationServiceError.InternalServer()
            )
        )
    }
}
