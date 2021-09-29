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

package care.data4life.datadonation

import care.data4life.datadonation.ConsentDataContract.ConsentDocument
import care.data4life.datadonation.ConsentDataContract.UserConsent
import care.data4life.datadonation.donation.publickeyservice.model.EnvironmentSerializer
import care.data4life.datadonation.networking.AccessToken
import care.data4life.sdk.flow.D4LSDKFlow
import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

sealed class Result<Success, Error>(
    val value: Success?,
    val error: Error?
) {
    init {
        this.freeze()
    }

    class Error<Succ : Any?, Err : Throwable>(value: Err) : Result<Succ, Err>(
        value = null,
        error = value
    )

    class Success<Succ, Err : Throwable?>(value: Succ) : Result<Succ, Err>(
        value = value,
        error = null
    )
}

interface DataDonationSDK {
    @Serializable(with = EnvironmentSerializer::class)
    enum class Environment(val url: String) {
        DEVELOPMENT("api-phdp-dev.hpsgc.de"),
        SANDBOX("api-phdp-sandbox.hpsgc.de"),
        STAGING("api-staging.data4life.care"),
        PRODUCTION("api.data4life.care")
    }

    interface DataDonationClient {
        fun fetchConsentDocuments(
            consentDocumentKey: String,
            consentDocumentVersion: String?,
            language: String?,
        ): D4LSDKFlow<List<ConsentDocument>>

        fun createUserConsent(
            consentDocumentKey: String,
            consentDocumentVersion: String
        ): D4LSDKFlow<UserConsent>

        fun fetchUserConsents(consentDocumentKey: String): D4LSDKFlow<List<UserConsent>>

        fun fetchAllUserConsents(): D4LSDKFlow<List<UserConsent>>

        fun revokeUserConsent(consentDocumentKey: String): D4LSDKFlow<UserConsent>
    }

    interface DataDonationClientFactory {
        fun getInstance(
            environment: Environment,
            userSession: UserSessionTokenProvider,
            coroutineScope: CoroutineScope? = null
        ): DataDonationClient
    }

    interface Pipe<Success, Error : Throwable> {
        fun onSuccess(value: Success)
        fun onError(error: Error)
        suspend fun receive(): Result<Success, Error>
    }

    fun interface UserSessionTokenProvider {
        fun getUserSessionToken(pipe: ResultPipe<AccessToken, Throwable>)
    }

    data class DonorKeyRecord(
        val recordId: RecordId,
        val data: EncodedDonorIdentity
    )

    interface DonorKeyStorageProvider {
        fun load(
            annotations: Annotations,
            pipe: ResultPipe<DonorKeyRecord?, Throwable>
        )

        fun save(
            donorKey: DonationDataContract.DonorRecord,
            pipe: ResultPipe<Unit?, Throwable>
        )

        fun delete(
            recordId: RecordId,
            pipe: ResultPipe<Unit?, Throwable>
        )
    }
}
