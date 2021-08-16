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

package care.data4life.datadonation.mock.stub.userconsent

import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.userconsent.UserConsentContract
import care.data4life.datadonation.userconsent.UserConsentError

internal class UserConsentErrorHandlerStub : UserConsentContract.ApiService.ErrorHandler, MockContract.Stub {
    var whenHandleFetchUserConsents: ((HttpRuntimeError) -> UserConsentError)? = null
    var whenHandleCreateUserConsent: ((HttpRuntimeError) -> UserConsentError)? = null
    var whenHandleRevokeUserConsent: ((HttpRuntimeError) -> UserConsentError)? = null

    override fun handleFetchUserConsents(error: HttpRuntimeError): UserConsentError {
        return whenHandleFetchUserConsents?.invoke(error) ?: throw MockException()
    }

    override fun handleCreateUserConsent(error: HttpRuntimeError): UserConsentError {
        return whenHandleCreateUserConsent?.invoke(error) ?: throw MockException()
    }

    override fun handleRevokeUserConsent(error: HttpRuntimeError): UserConsentError {
        return whenHandleRevokeUserConsent?.invoke(error) ?: throw MockException()
    }

    override fun clear() {
        whenHandleFetchUserConsents = null
        whenHandleCreateUserConsent = null
        whenHandleRevokeUserConsent = null
    }
}
