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
import care.data4life.datadonation.internal.domain.usecases.CreateUserConsent
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsents
import care.data4life.datadonation.internal.domain.usecases.Usecase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Client(private val configuration: Contract.Configuration) : Contract.DataDonation {

    private val koinApplication = initKoin(configuration)
    private val createUserContent: CreateUserConsent by koinApplication.koin.inject()
    private val fetchUserConsents: FetchUserConsents by koinApplication.koin.inject()
    private val context = GlobalScope //TODO use proper CoroutineScope

    override fun fetchConsentDocument(
        consentDocumentVersion: String?,
        language: String?,
        listener: ResultListener<List<ConsentDocument>>
    ) {
        TODO("Not yet implemented")
    }

    override fun createUserConsent(
        consentDocumentVersion: String,
        language: String?,
        listener: ResultListener<Pair<UserConsent, KeyPair>>
    ) {
        createUserContent.withParams(
            CreateUserConsent.Parameters(
                configuration.getDonationKeyPair(),
                consentDocumentVersion,
                language
            )
        ).runForListener(listener)
    }

    override fun fetchUserConsents(listener: ResultListener<List<UserConsent>>) {
        fetchUserConsents.runForListener(listener)
    }

    override fun revokeUserConsent(language: String?, callback: Callback) {
        TODO("Not yet implemented")
    }

    private fun <ReturnType : Any> Usecase<ReturnType>.runForListener(
        listener: ResultListener<ReturnType>
    ) {
        context.launch {
            try {
                listener.onSuccess(this@runForListener.execute())
            }catch (ex: Exception){
                listener.onError(ex)
            }
        }
    }
}
