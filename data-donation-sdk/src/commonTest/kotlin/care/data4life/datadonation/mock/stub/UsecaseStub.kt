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

import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

abstract class UsecaseStub<Parameter : Any, ReturnType : Any> :
    UsecaseContract.Usecase<Parameter, ReturnType>,
    MockContract.Stub {
    var whenExecute: ((Parameter) -> ReturnType)? = null

    override suspend fun execute(parameter: Parameter): ReturnType {
        return whenExecute?.invoke(parameter) ?: throw MockException()
    }

    override fun clear() {
        whenExecute = null
    }
}