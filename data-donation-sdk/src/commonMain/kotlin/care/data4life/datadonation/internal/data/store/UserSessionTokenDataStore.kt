/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package care.data4life.datadonation.internal.data.store

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.listener.ResultListener
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.minutes

internal interface UserSessionTokenDataStore {
    suspend fun getUserSessionToken(): String?
}

class CachedUserSessionTokenDataStore(
    private val configuration: Contract.Configuration,
    private val clock: Clock
) :
    UserSessionTokenDataStore {

    private lateinit var cachedValue: String
    private var cachedAt = Instant.fromEpochSeconds(0)

    override suspend fun getUserSessionToken(): String? =
        suspendCoroutine { continuation ->

            if (cachedAt > clock.now().minus(1.minutes)) {
                continuation.resume(cachedValue)
            } else {
                configuration.getUserSessionToken(object : ResultListener<String> {
                    override fun onSuccess(t: String) {
                        cachedValue = t
                        cachedAt = clock.now()
                        continuation.resume(t)
                    }

                    override fun onError(exception: Exception) {
                        continuation.resume(null)
                    }

                })
            }


        }

}
