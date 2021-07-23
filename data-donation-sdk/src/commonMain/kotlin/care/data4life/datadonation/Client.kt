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

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.di.initKoin
import care.data4life.datadonation.internal.domain.usecases.*
import care.data4life.datadonation.wrapper.D4LSDKFlow
import care.data4life.datadonation.wrapper.D4LSDKFlowContract
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinApplication

class Client internal constructor(
    koinApplication: KoinApplication
) : DataDonationSDKPublicAPI.DataDonationClient {
    private val createUserContent: UsecaseContract.CreateUserConsent = koinApplication.koin.get()
    private val fetchConsentDocuments: UsecaseContract.FetchConsentDocuments = koinApplication.koin.get()
    private val fetchUserConsents: UsecaseContract.FetchUserConsents = koinApplication.koin.get()
    private val revokeUserConsent: UsecaseContract.RevokeUserConsent = koinApplication.koin.get()

    override fun fetchConsentDocuments(
        consentDocumentVersion: Int?,
        language: String?,
        consentKey: String
    ): D4LSDKFlowContract<List<ConsentDocument>> {
        val flow = flow {
            val parameter = FetchConsentDocuments.Parameter(
                version = consentDocumentVersion,
                language = language,
                consentKey = consentKey
            )

            emit(fetchConsentDocuments.execute(parameter))
        }

        return D4LSDKFlow(flow)
    }

    override fun createUserConsent(
        consentKey: String,
        consentDocumentVersion: Int
    ): D4LSDKFlowContract<UserConsent> {
        val flow = flow {
            val parameter = CreateUserConsent.Parameter(
                consentKey,
                consentDocumentVersion
            )

            emit(createUserContent.execute(parameter))
        }

        return D4LSDKFlow(flow)
    }

    override fun fetchUserConsents(consentKey: String): D4LSDKFlowContract<List<UserConsent>> {
        val flow = flow {
            val parameter = FetchUserConsents.Parameter(consentKey)

            emit(fetchUserConsents.execute(parameter))
        }

        return D4LSDKFlow(flow)
    }

    override fun fetchAllUserConsents(): D4LSDKFlowContract<List<UserConsent>> {
        val flow = flow {
            val parameter = FetchUserConsents.Parameter()

            emit(fetchUserConsents.execute(parameter))
        }

        return D4LSDKFlow(flow)
    }

    override fun revokeUserConsent(consentKey: String): D4LSDKFlowContract<Unit> {
        val flow = flow {
            val parameter = RevokeUserConsent.Parameter(consentKey)

            emit(revokeUserConsent.execute(parameter))
        }

        return D4LSDKFlow(flow)
    }

    companion object Factory : DataDonationSDKPublicAPI.DataDonationClientFactory {
        override fun getInstance(
            environment: DataDonationSDKPublicAPI.Environment,
            userSession: DataDonationSDKPublicAPI.UserSessionTokenProvider
        ): DataDonationSDKPublicAPI.DataDonationClient {
            return Client(
                initKoin(
                    environment,
                    userSession
                )
            )
        }
    }
}
