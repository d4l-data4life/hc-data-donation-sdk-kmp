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
import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract
import care.data4life.datadonation.consent.userconsent.UserConsentContract
import care.data4life.datadonation.di.initKoin
import care.data4life.sdk.flow.D4LSDKFlow
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import org.koin.core.KoinApplication

class Client internal constructor(
    koinApplication: KoinApplication
) : DataDonationSDK.DataDonationClient {
    private val userConsent: UserConsentContract.Controller = koinApplication.koin.get()
    private val consentDocuments: ConsentDocumentContract.Controller = koinApplication.koin.get()
    private val backgroundThread: CoroutineScope = koinApplication.koin.get()
    private val errorMapper: DomainErrorMapperContract = koinApplication.koin.get()
    private val flowFactory: D4LSDKFlowFactoryContract = koinApplication.koin.get()

    private inline fun <T : Any> wrapResult(
        crossinline call: suspend () -> T
    ): D4LSDKFlow<T> {
        val flow = flow { emit(call()) }

        return flowFactory.getInstance(
            backgroundThread,
            flow,
            errorMapper
        )
    }

    override fun createUserConsent(
        consentDocumentKey: String,
        consentDocumentVersion: String
    ): D4LSDKFlow<UserConsent> {

        return wrapResult {
            userConsent.createUserConsent(
                consentDocumentKey = consentDocumentKey,
                consentDocumentVersion = consentDocumentVersion
            )
        }
    }

    override fun fetchConsentDocuments(
        consentDocumentKey: String,
        consentDocumentVersion: String?,
        language: String?,
    ): D4LSDKFlow<List<ConsentDocument>> {
        return wrapResult {
            consentDocuments.fetchConsentDocuments(
                consentDocumentKey = consentDocumentKey,
                consentDocumentVersion = consentDocumentVersion,
                language = language
            )
        }
    }

    override fun fetchUserConsents(consentDocumentKey: String): D4LSDKFlow<List<UserConsent>> {
        return wrapResult {
            userConsent.fetchUserConsents(
                consentDocumentKey = consentDocumentKey
            )
        }
    }

    override fun fetchAllUserConsents(): D4LSDKFlow<List<UserConsent>> {
        return wrapResult { userConsent.fetchAllUserConsents() }
    }

    override fun revokeUserConsent(consentDocumentKey: String): D4LSDKFlow<UserConsent> {
        return wrapResult {
            userConsent.revokeUserConsent(
                consentDocumentKey = consentDocumentKey
            )
        }
    }

    companion object Factory : DataDonationSDK.DataDonationClientFactory {
        override fun getInstance(
            environment: DataDonationSDK.Environment,
            userSession: DataDonationSDK.UserSessionTokenProvider
        ): DataDonationSDK.DataDonationClient {
            return Client(
                initKoin(
                    environment,
                    userSession
                )
            )
        }
    }
}
