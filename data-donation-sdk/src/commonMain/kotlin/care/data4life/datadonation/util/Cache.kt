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

package care.data4life.datadonation.util

import care.data4life.datadonation.error.CoreRuntimeError
import kotlinx.datetime.Clock

internal class Cache(
    private val clock: Clock,
    private val lifetimeInSeconds: Int
) : CacheContract {
    private var cachedValue = ""
    private var cachedAt = 0L

    override fun fetch(): String {
        return if(isNotExpired()) {
            cachedValue
        } else {
            throw CoreRuntimeError.InternalFailure("Cache expired")
        }
    }

    override fun update(value: String) {
        cachedValue = value
        cachedAt = clock.now().epochSeconds
    }

    override fun isNotExpired(): Boolean {
        return cachedAt > nowMinusLifeTime()
    }

    private fun nowMinusLifeTime() = clock.now().epochSeconds - lifetimeInSeconds
}
