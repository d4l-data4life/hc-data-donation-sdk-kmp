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

import care.data4life.datadonation.core.listener.ListenerContract
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.storage.StorageContract
import care.data4life.hl7.fhir.stu3.model.FhirResource
import kotlinx.coroutines.CoroutineScope

interface Contract {

    enum class Service(name: String) {
        DD("DataDonation"),
        ALP("AnalyticsPlatform")
    }

    interface Configuration :
        ListenerContract.ScopeResolver,
        StorageContract.CredentialProvider,
        StorageContract.UserSessionTokenProvider {
        override fun getServicePublicKey(service: Service): String

        fun getDonorKeyPair(): KeyPair?

        override fun getUserSessionToken(tokenListener: ListenerContract.ResultListener<String>)

        fun getEnvironment(): Environment

        override fun getCoroutineScope(): CoroutineScope
    }

    interface DataDonation {
        fun fetchConsentDocuments(
            consentDocumentVersion: Int?,
            language: String?,
            consentKey: String,
            listener: ListenerContract.ResultListener<List<ConsentDocument>>
        )

        fun createUserConsent(
            consentDocumentVersion: Int,
            language: String?,
            listener: ListenerContract.ResultListener<UserConsent>
        )

        fun registerDonor(
            listener: ListenerContract.ResultListener<KeyPair>
        )

        fun fetchUserConsents(
            consentKey: String,
            listener: ListenerContract.ResultListener<List<UserConsent>>
        )

        fun fetchAllUserConsents(
            listener: ListenerContract.ResultListener<List<UserConsent>>
        )

        fun revokeUserConsent(language: String?, callback: ListenerContract.Callback)

        fun <T : FhirResource> donateResources(
            resources: List<T>,
            callback: ListenerContract.Callback
        )
    }

    interface DataDonationFactory {
        fun getInstance(configuration: Configuration): DataDonation
    }
}
