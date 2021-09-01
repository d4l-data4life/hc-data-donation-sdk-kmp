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
import care.data4life.datadonation.networking.AccessToken
import care.data4life.datadonation.session.SessionTokenRepositoryContract.Companion.CACHE_LIFETIME_IN_SECONDS
import care.data4life.datadonation.util.Cache
import care.data4life.sdk.lang.D4LRuntimeException
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
    /*
     * Please note: A propagation via Koin leads at the moment to freezing issue, therefore we must keep it in the class
     */
    private val cache = IsolateState { Cache(clock, CACHE_LIFETIME_IN_SECONDS) }
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
            onSuccess = { sessionToken: AccessToken ->
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

    private fun fetchCachedTokenIfNotExpired(): AccessToken? {
        return try {
            cache.access { it.fetch() }
        } catch (e: D4LRuntimeException) {
            null
        }
    }

    private fun resolveSessionToken(result: Any): AccessToken {
        return when (result) {
            is AccessToken -> result.also { cache.access { it.update(result) } }
            is Exception -> throw UserSessionError.MissingSession(result)
            else -> throw UserSessionError.MissingSession()
        }
    }

    override suspend fun getUserSessionToken(): AccessToken {
        val cachedToken = fetchCachedTokenIfNotExpired()

        return if (cachedToken is AccessToken) {
            cachedToken
        } else {
            return resolveSessionToken(fetchTokenFromApi())
        }
    }
}
