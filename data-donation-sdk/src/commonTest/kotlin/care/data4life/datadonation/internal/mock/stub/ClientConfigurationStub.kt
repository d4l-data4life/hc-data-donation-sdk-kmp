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

package care.data4life.datadonation.internal.mock.stub

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.listener.ResultListener
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.internal.mock.MockContract
import care.data4life.datadonation.internal.mock.MockException
import kotlinx.coroutines.CoroutineScope

class ClientConfigurationStub : Contract.Configuration, MockContract.Stub {
    var whenGetServicePublicKey: ((Contract.Service) -> String)? = null
    var whenGetDonorKeyPair: (() -> KeyPair?)? = null
    var whenGetUserSessionToken: ((ResultListener<String>) -> Unit)? = null
    var whenGetEnvironment: (() -> Environment)? = null
    var whenGetCoroutineContext: (() -> CoroutineScope)? = null

    override fun getServicePublicKey(service: Contract.Service): String {
        return whenGetServicePublicKey?.invoke(service) ?: throw MockException()
    }

    override fun getDonorKeyPair(): KeyPair? {
        whenGetDonorKeyPair ?: throw MockException()

        return whenGetDonorKeyPair!!.invoke()
    }

    override fun getUserSessionToken(tokenListener: ResultListener<String>) {
        return whenGetUserSessionToken?.invoke(tokenListener) ?: throw MockException()
    }

    override fun getEnvironment(): Environment {
        return whenGetEnvironment?.invoke() ?: throw MockException()
    }

    override fun getCoroutineContext(): CoroutineScope {
        return whenGetCoroutineContext?.invoke() ?: throw MockException()
    }

    override fun clear() {
        whenGetServicePublicKey = null
        whenGetDonorKeyPair = null
        whenGetUserSessionToken = null
        whenGetEnvironment = null
        whenGetCoroutineContext = null
    }
}
