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

package care.data4life.datadonation.mock.stub

import care.data4life.datadonation.core.listener.ListenerContract.Callback
import care.data4life.datadonation.core.listener.ListenerContract.ResultListener
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.runner.UsecaseRunnerContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class UsecaseRunnerStub : UsecaseRunnerContract, MockContract.Stub {
    var whenRunListener: ((ResultListener<*>, UsecaseContract.Usecase<*, *>, Any) -> Unit)? = null
    var whenRunCallback: ((Callback, UsecaseContract.Usecase<*, *>, Any) -> Unit)? = null

    override fun <Parameter : Any, ReturnType : Any> run(
        listener: ResultListener<ReturnType>,
        usecase: UsecaseContract.Usecase<Parameter, ReturnType>,
        parameter: Parameter
    ) {
        whenRunListener?.invoke(listener, usecase as UsecaseContract.Usecase<*, *>, parameter as Any) ?: throw MockException()
    }

    override fun <Parameter : Any, ReturnType : Any> run(
        listener: Callback,
        usecase: UsecaseContract.Usecase<Parameter, ReturnType>,
        parameter: Parameter
    ) {
        whenRunCallback?.invoke(listener, usecase as UsecaseContract.Usecase<*, *>, parameter as Any) ?: throw MockException()
    }

    override fun clear() {
        whenRunCallback = null
        whenRunListener = null
    }
}
