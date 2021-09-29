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
import care.data4life.datadonation.Result
import care.data4life.datadonation.ResultPipe
import care.data4life.datadonation.networking.AccessToken
import care.data4life.datadonation.session.SessionTokenRepositoryContract.Companion.CACHE_LIFETIME_IN_SECONDS
import care.data4life.datadonation.util.Cache
import care.data4life.sdk.lang.D4LRuntimeException
import co.touchlab.stately.isolate.IsolateState
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock

internal class CachedUserSessionTokenRepository(
    private val provider: DataDonationSDK.UserSessionTokenProvider,
    clock: Clock,
    scope: CoroutineScope
) : SessionTokenRepositoryContract {
    private val cache = IsolateState { Cache(clock, CACHE_LIFETIME_IN_SECONDS) }
    private val pipe = ResultPipe<AccessToken, Throwable>(scope)

    private suspend fun fetchTokenFromApi(): Result<AccessToken, Throwable> {
        provider.getUserSessionToken(pipe)
        return pipe.receive()
    }

    private fun fetchCachedTokenIfNotExpired(): AccessToken? {
        return try {
            cache.access { it.fetch() }
        } catch (e: D4LRuntimeException) {
            null
        }
    }

    private fun resolveSessionToken(result: Result<AccessToken, Throwable>): AccessToken {
        return when (result) {
            is Result.Success<*, *> -> (result.value as AccessToken).also { token ->
                cache.access { it.update(token) }
            }
            is Result.Error<*, *> -> throw UserSessionError.MissingSession(result.error)
        }
    }

    override suspend fun getUserSessionToken(): AccessToken {
        val cachedToken = fetchCachedTokenIfNotExpired()

        return if (cachedToken is AccessToken) {
            cachedToken
        } else {
            resolveSessionToken(fetchTokenFromApi())
        }
    }
}
