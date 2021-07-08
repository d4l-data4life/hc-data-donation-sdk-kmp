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
import care.data4life.datadonation.core.listener.ListenerContract.TaskRunner
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract.Usecase
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class UsecaseRunnerStub : ListenerInternalContract.UsecaseRunner, MockContract.Stub {
    var whenRunListener: ((ResultListener<*>, Usecase<*>) -> Unit)? = null
    var whenRunCallback: ((Callback, Usecase<*>) -> Unit)? = null

    var whenRunListenerNew: ((ResultListener<*>, UsecaseContract.NewUsecase<*, *>, Any) -> Unit)? = null
    var whenRunCallbackNew: ((Callback, UsecaseContract.NewUsecase<*, *>, Any) -> Unit)? = null

    override fun <Parameter : Any, ReturnType : Any> run(
        listener: ResultListener<ReturnType>,
        usecase: UsecaseContract.NewUsecase<Parameter, ReturnType>,
        parameter: Parameter
    ) {
        whenRunListenerNew?.invoke(listener, usecase as UsecaseContract.NewUsecase<*, *>, parameter as Any) ?: throw MockException()
    }

    override fun <Parameter : Any, ReturnType : Any> run(
        listener: Callback,
        usecase: UsecaseContract.NewUsecase<Parameter, ReturnType>,
        parameter: Parameter
    ) {
        whenRunCallbackNew?.invoke(listener, usecase as UsecaseContract.NewUsecase<*, *>, parameter as Any) ?: throw MockException()
    }

    override fun <ReturnType : Any> run(
        listener: ResultListener<ReturnType>,
        usecase: Usecase<ReturnType>
    ) {
        whenRunListener?.invoke(listener, usecase as Usecase<*>) ?: throw MockException()
    }

    override fun <ReturnType : Any> run(
        listener: Callback,
        usecase: Usecase<ReturnType>
    ) {
        whenRunCallback?.invoke(listener, usecase as Usecase<*>) ?: throw MockException()
    }

    override fun clear() {
        whenRunCallback = null
        whenRunListener = null
    }
}
