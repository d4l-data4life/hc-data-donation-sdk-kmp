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

import care.data4life.datadonation.core.listener.Callback
import care.data4life.datadonation.core.listener.ResultListener
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.di.initKoin
import care.data4life.datadonation.internal.domain.usecases.*
import care.data4life.hl7.fhir.stu3.model.FhirResource
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication

class Client internal constructor(
    private val configuration: Contract.Configuration,
    koinApplication: KoinApplication
) : Contract.DataDonation {
    private val createUserContent: CreateUserConsent by koinApplication.koin.inject()
    private val registerNewDonor: RegisterNewDonor by koinApplication.koin.inject()
    private val fetchConsentDocuments: FetchConsentDocuments by koinApplication.koin.inject()
    private val fetchUserConsents: UsecaseContract.FetchUserConsents by koinApplication.koin.inject()
    private val revokeUserContent: RevokeUserConsent by koinApplication.koin.inject()
    private val donateResources: DonateResources by koinApplication.koin.inject()

    private fun <ReturnType : Any> runUsecase(
        listener: ResultListener<ReturnType>,
        usecase: UsecaseContract.Usecase<ReturnType>
    ) {
        configuration.getCoroutineContext().launch {
            try {
                listener.onSuccess(usecase.execute())
            } catch (ex: Exception) {
                listener.onError(ex)
            }
        }
    }

    override fun fetchConsentDocuments(
        consentDocumentVersion: Int?,
        language: String?,
        consentKey: String,
        listener: ResultListener<List<ConsentDocument>>
    ) {
        TODO()
        /*fetchConsentDocuments.withParams(
            FetchConsentDocuments.Parameters(
                consentDocumentVersion,
                language,
                consentKey
            )
        ).runForListener(listener)*/
    }

    override fun createUserConsent(
        consentDocumentVersion: Int,
        language: String?,
        listener: ResultListener<UserConsent>
    ) {
        createUserContent.withParams(
            CreateUserConsent.Parameters(
                configuration.getDonorKeyPair(),
                consentDocumentVersion,
                language
            )
        ).runForListener(listener)
    }

    override fun registerDonor(
        listener: ResultListener<KeyPair>
    ) {
        registerNewDonor.withParams(RegisterNewDonor.Parameters(configuration.getDonorKeyPair()))
            .runForListener(listener)
    }

    override fun fetchUserConsents(
        listener: ResultListener<List<UserConsent>>,
        consentKey: String?
    ) {
        val parameter = FetchUserConsentsFactory.Parameters(consentKey)

        runUsecase(
            listener,
            fetchUserConsents.withParams(parameter)
        )
    }

    override fun revokeUserConsent(language: String?, callback: Callback) {
        revokeUserContent.withParams(RevokeUserConsent.Parameters(language))
            .runForListener(callback)
    }

    override fun <T : FhirResource> donateResources(resources: List<T>, callback: Callback) {
        donateResources.withParams(
            DonateResources.Parameters(
                configuration.getDonorKeyPair(),
                resources
            )
        ).runForListener(callback)
    }

    // TODO: Remove -> Wrong level of abstraction
    private fun <ReturnType : Any> Usecase<ReturnType>.runForListener(
        listener: ResultListener<ReturnType>
    ) {
        configuration.getCoroutineContext().launch {
            try {
                listener.onSuccess(this@runForListener.execute())
            } catch (ex: Exception) {
                listener.onError(ex)
            }
        }
    }

    // TODO: Remove -> Wrong level of abstraction
    private fun <ReturnType : Any> Usecase<ReturnType>.runForListener(
        listener: Callback
    ) {
        configuration.getCoroutineContext().launch {
            try {
                execute()
                listener.onSuccess()
            } catch (ex: Exception) {
                listener.onError(ex)
            }
        }
    }

    companion object Factory : Contract.DataDonationFactory {
        override fun getInstance(
            configuration: Contract.Configuration
        ): Contract.DataDonation {
            return Client(configuration, initKoin(configuration))
        }
    }
}
