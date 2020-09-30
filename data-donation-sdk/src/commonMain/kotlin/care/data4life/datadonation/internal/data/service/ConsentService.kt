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

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.*
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.datetime.Clock


internal class ConsentService(
    private val client: HttpClient,
    environment: Environment
) {

    private val baseUrl = "${environment.url}/consent/api/v1"

    suspend fun fetchConsentDocuments(
        accessToken: String,
        version: String?,
        language: String?
    ): List<ConsentDocument> {
        return client.getWithQuery(accessToken, baseUrl, Endpoints.consentDocuments) {
            parameter(Parameters.consentDocumentKey, defaultDonationConsentKey)
            parameter(Parameters.version, version)
            parameter(Parameters.language, language)
        }
    }

    suspend fun fetchUserConsents(accessToken: String, latest: Boolean?): List<UserConsent> {
        return client.getWithQuery(accessToken, baseUrl, Endpoints.userConsents) {
            parameter(Parameters.consentDocumentKey, defaultDonationConsentKey)
            parameter(Parameters.latest, latest)
        }
    }

    suspend fun createUserConsent(accessToken: String, version: String, language: String?): TokenVerificationResult {
        return client.postWithBody(
            accessToken,
            baseUrl,
            Endpoints.userConsents,
            ConsentCreationPayload(
                defaultDonationConsentKey,
                version,
                Clock.System.now().toString(),
                language ?: ""
            )
        )
    }

    suspend fun requestSignature(accessToken: String, message: String): ConsentSignature {
        return client.postWithBody(
            accessToken,
            baseUrl,
            "${Endpoints.userConsents}/$defaultDonationConsentKey/signatures",
            ConsentSigningRequest(
                defaultDonationConsentKey,
                message,
                ConsentSignatureType.ConsentOnce.apiValue
            )
        )
    }

    suspend fun revokeUserConsent(accessToken: String, language: String?) {
        return client.deleteWithBody(
            accessToken,
            baseUrl,
            Endpoints.userConsents,
            ConsentRevocationPayload(defaultDonationConsentKey, language ?: "")
        )
    }

    companion object {
        const val defaultDonationConsentKey = "data donation"

        object Endpoints {
            const val userConsents = "userConsents"
            const val consentDocuments = "consentDocuments"
        }

        object Parameters {
            const val consentDocumentKey = "consentDocumentKey"
            const val latest = "latest"
            const val language = "language"
            const val version = "version"
        }
    }

}
