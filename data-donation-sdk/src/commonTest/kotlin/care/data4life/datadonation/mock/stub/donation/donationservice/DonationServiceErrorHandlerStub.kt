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

package care.data4life.datadonation.mock.stub.donation.donationservice

import care.data4life.datadonation.donation.donationservice.DonationServiceContract
import care.data4life.datadonation.donation.donationservice.DonationServiceError
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import care.data4life.datadonation.networking.HttpRuntimeError

internal class DonationServiceErrorHandlerStub :
    DonationServiceContract.ApiService.ErrorHandler,
    MockContract.Stub {

    var whenHandleFetchToken: ((error: HttpRuntimeError) -> DonationServiceError)? = null
    var whenHandleRegister: ((error: HttpRuntimeError) -> DonationServiceError)? = null
    var whenHandleDonate: ((error: HttpRuntimeError) -> DonationServiceError)? = null
    var whenHandleRevoke: ((error: HttpRuntimeError) -> DonationServiceError)? = null

    override fun handleFetchToken(error: HttpRuntimeError): DonationServiceError {
        return whenHandleFetchToken?.invoke(error) ?: throw MockException()
    }

    override fun handleRegister(error: HttpRuntimeError): DonationServiceError {
        return whenHandleRegister?.invoke(error) ?: throw MockException()
    }

    override fun handleDonate(error: HttpRuntimeError): DonationServiceError {
        return whenHandleDonate?.invoke(error) ?: throw MockException()
    }

    override fun handleRevoke(error: HttpRuntimeError): DonationServiceError {
        return whenHandleRevoke?.invoke(error) ?: throw MockException()
    }

    override fun clear() {
        whenHandleFetchToken = null
        whenHandleRegister = null
        whenHandleDonate = null
        whenHandleRevoke = null
    }
}
