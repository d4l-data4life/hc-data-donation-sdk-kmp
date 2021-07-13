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

package care.data4life.datadonation.mock.stub.service

import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.lang.HttpRuntimeError
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

internal class ConsentErrorHandlerStub : ServiceContract.ConsentService.ConsentErrorHandler, MockContract.Stub {
    var whenHandleFetchConsentDocuments: ((HttpRuntimeError) -> Unit)? = null
    var whenHandleFetchUserConsents: ((HttpRuntimeError) -> Unit)? = null
    var whenHandleCreateUserConsent: ((HttpRuntimeError) -> Unit)? = null
    var whenHandleRequestSignatureConsentRegistration: ((HttpRuntimeError) -> Unit)? = null
    var whenHandleRequestSignatureDonation: ((HttpRuntimeError) -> Unit)? = null
    var whenHandleRevokeUserConsent: ((HttpRuntimeError) -> Unit)? = null

    override fun handleFetchConsentDocuments(error: HttpRuntimeError) {
        whenHandleFetchConsentDocuments?.invoke(error) ?: throw MockException()
    }

    override fun handleFetchUserConsents(error: HttpRuntimeError) {
        whenHandleFetchUserConsents?.invoke(error) ?: throw MockException()
    }

    override fun handleCreateUserConsent(error: HttpRuntimeError) {
        whenHandleCreateUserConsent?.invoke(error) ?: throw MockException()
    }

    override fun handleRequestSignatureConsentRegistration(error: HttpRuntimeError) {
        whenHandleRequestSignatureConsentRegistration?.invoke(error) ?: throw MockException()
    }

    override fun handleRequestSignatureDonation(error: HttpRuntimeError) {
        whenHandleRequestSignatureDonation?.invoke(error) ?: throw MockException()
    }

    override fun handleRevokeUserConsent(error: HttpRuntimeError) {
        whenHandleRevokeUserConsent?.invoke(error) ?: throw MockException()
    }

    override fun clear() {
        whenHandleFetchConsentDocuments = null
        whenHandleFetchUserConsents = null
        whenHandleCreateUserConsent = null
        whenHandleRequestSignatureConsentRegistration = null
        whenHandleRequestSignatureDonation = null
        whenHandleRevokeUserConsent = null
    }
}
