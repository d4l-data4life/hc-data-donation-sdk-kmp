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

package care.data4life.datadonation.wrapper

// TODO: use import kotlin.native.concurrent.freeze, with Kotlin 1.5.x
import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

actual class D4LSDKFlow<T> actual constructor(
    internalFlow: Flow<T>
) : D4LSDKFlowContract<T> {
    private val flow = internalFlow

    init {
        freeze()
    }

    actual override val ktFlow: Flow<T>
        get() = flow.freeze()

    actual override fun subscribe(
        scope: CoroutineScope,
        onEach: (item: T) -> Unit,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Job {
        return flow
            .onEach { item -> onEach(item.freeze()) }
            .catch { error -> onThrow(error.freeze()) }
            .onCompletion { onComplete() }
            .launchIn(scope)
            .freeze()
    }
}
