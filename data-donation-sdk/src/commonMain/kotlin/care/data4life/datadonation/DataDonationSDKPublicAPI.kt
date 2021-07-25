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

import care.data4life.datadonation.core.model.ModelContract.ConsentDocument
import care.data4life.datadonation.core.model.ModelContract.UserConsent
import care.data4life.sdk.util.coroutine.D4LSDKFlowContract

interface DataDonationSDKPublicAPI {
    enum class Environment(val url: String) {
        DEV("api-phdp-dev.hpsgc.de"),
        SANDBOX("api-phdp-sandbox.hpsgc.de"),
        STAGING("api-staging.data4life.care"),
        PRODUCTION("api.data4life.care")
    }

    fun interface UserSessionTokenProvider {
        fun getUserSessionToken(
            onSuccess: (sessionToken: String) -> Unit,
            onError: (error: Exception) -> Unit
        )
    }

    interface DataDonationClient {
        fun fetchConsentDocuments(
            consentKey: String,
            consentDocumentVersion: Int?,
            language: String?,
        ): D4LSDKFlowContract<List<ConsentDocument>>

        fun createUserConsent(
            consentKey: String,
            consentDocumentVersion: Int
        ): D4LSDKFlowContract<UserConsent>

        fun fetchUserConsents(consentKey: String): D4LSDKFlowContract<List<UserConsent>>

        fun fetchAllUserConsents(): D4LSDKFlowContract<List<UserConsent>>

        fun revokeUserConsent(consentKey: String): D4LSDKFlowContract<Unit>
    }

    interface DataDonationClientFactory {
        fun getInstance(
            environment: Environment,
            userSession: UserSessionTokenProvider
        ): DataDonationClient
    }
}
