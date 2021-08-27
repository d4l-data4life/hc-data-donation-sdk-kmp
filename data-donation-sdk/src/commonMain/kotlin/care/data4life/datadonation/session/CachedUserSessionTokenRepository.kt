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

package care.data4life.datadonation.session

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.session.SessionTokenRepositoryContract.Companion.CACHE_LIFETIME_IN_SECONDS
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.freeze
import co.touchlab.stately.isolate.IsolateState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class CachedUserSessionTokenRepository(
    private val provider: DataDonationSDK.UserSessionTokenProvider,
    clock: Clock,
    scope: CoroutineScope
) : SessionTokenRepositoryContract {
    private val cache = IsolateState { Cache(clock) }
    private val scope = AtomicReference(scope)

    /*
    * Please note the provider does not share the same Context/Scope/Thread as the SDK.
    * This means the SDK needs to transfer the sessionToken from the Context/Scope/Thread of the provider
    * into it's own. Additionally Closures in Swift are not blocking.
    * Since the SDK Context/Scope/Thread is known and using Atomics like constant values is safe, the
    * SDK is able to launch a new coroutine.
    * The channel then makes the actual transfer from the provider Context/Scope/Thread into the
    * SDK Context/Scope/Thread. Also Channels are blocking which then take care of any async delay caused
    * by the coroutine of the Callbacks or Swift.
    */
    private suspend fun fetchTokenFromApi(): Any {
        val incoming = Channel<Any>()

        provider.getUserSessionToken(
            onSuccess = { sessionToken: SessionToken ->
                scope.get().launch {
                    incoming.send(sessionToken)
                }.start()
                Unit
            }.freeze(),
            onError = { error: Exception ->
                scope.get().launch {
                    incoming.send(error)
                }.start()
                Unit
            }.freeze()
        )

        return incoming.receive()
    }

    private fun fetchCachedTokenIfNotExpired(): SessionToken? {
        return if (cache.access { it.isNotExpired() }) {
            cache.access { it.fetch() }
        } else {
            null
        }
    }

    private fun resolveSessionToken(result: Any): SessionToken {
        return when (result) {
            is SessionToken -> result.also { cache.access { it.update(result) } }
            is Exception -> throw UserSessionError.MissingSession(result)
            else -> throw UserSessionError.MissingSession()
        }
    }

    override suspend fun getUserSessionToken(): SessionToken {
        val cachedToken = fetchCachedTokenIfNotExpired()

        return if (cachedToken is SessionToken) {
            cachedToken
        } else {
            return resolveSessionToken(fetchTokenFromApi())
        }
    }

    private class Cache(private val clock: Clock) {
        private var cachedValue: SessionToken = ""
        private var cachedAt = 0L

        fun fetch(): String {
            return if (cachedValue.isEmpty()) {
                throw UserSessionError.MissingSession()
            } else {
                cachedValue
            }
        }

        fun update(sessionToken: SessionToken) {
            cachedValue = sessionToken
            cachedAt = clock.now().epochSeconds
        }

        fun isNotExpired(): Boolean {
            return cachedAt > nowMinusLifeTime()
        }

        private fun nowMinusLifeTime() = clock.now().epochSeconds - CACHE_LIFETIME_IN_SECONDS
    }
}
