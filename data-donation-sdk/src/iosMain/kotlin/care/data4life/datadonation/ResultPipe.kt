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

package care.data4life.datadonation

import care.data4life.datadonation.Result.Error
import care.data4life.datadonation.Result.Success
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

actual class ResultPipe<Success, Error : Throwable> actual constructor(
    scope: CoroutineScope
) : DataDonationSDK.Pipe<Success, Error> {
    private val scope: AtomicReference<CoroutineScope>
    private val channel: AtomicReference<Channel<Result<Success, Error>>>

    init {
        this.scope = AtomicReference(scope)
        this.channel = AtomicReference(Channel())
        this.freeze()
    }

    private fun sendResult(result: Result<Success, Error>) {
        scope.get().launch {
            channel.get().send(result)
        }.start()
    }

    actual override fun onSuccess(value: Success) {
        sendResult(Success(value))
    }

    actual override fun onError(error: Error) {
        sendResult(Error(error))
    }

    actual override suspend fun receive(): Result<Success, Error> {
        return channel.get().receive()
    }
}
