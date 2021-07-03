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

import care.data4life.datadonation.core.listener.ResultListener
import care.data4life.datadonation.internal.mock.MockContract
import care.data4life.datadonation.internal.mock.MockException

class ResultListenerStub<ReturnType : Any> : ResultListener<ReturnType>, MockContract.Stub {
    var whenOnSuccess: ((ReturnType) -> Unit)? = null
    var whenOnError: ((Exception) -> Unit)? = null

    override fun onSuccess(result: ReturnType) {
        return whenOnSuccess?.invoke(result) ?: throw MockException()
    }

    override fun onError(exception: Exception) {
        return whenOnError?.invoke(exception) ?: throw MockException()
    }

    override fun clear() {
        whenOnSuccess = null
        whenOnError = null
    }
}
