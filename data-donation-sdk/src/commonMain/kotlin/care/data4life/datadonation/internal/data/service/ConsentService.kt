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
import care.data4life.datadonation.internal.data.model.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class ConsentService(private val client:HttpClient) {

    suspend fun fetchConsentDocument(dataDonationKey : String, version: String, language: String): List<ConsentDocument> {
        return client.get {
            url("$baseUrl/admin/consentDocuments.json")
        }
    }

    suspend fun createUserConsent(version: String, language: String?): TokenVerificationResult {
        return client.postWithBody(
            "",//TODO
            "$baseUrl/$userConsentsEndpoint",
            ConsentCreationPayload(dataDonationKey, version, "", language ?: "")
        )
    }

    suspend fun requestSignature(message: String): ConsentSignature {
        return client.postWithBody(
            "",//TODO
            "$baseUrl/$userConsentsEndpoint/$dataDonationKey/signatures",
            ConsentSigningRequest(
                dataDonationKey,
                message,
                ConsentSignatureType.ConsentOnce.apiValue
            )
        )
    }

    companion object {
        private const val baseUrl = "https://api.data4life.local/consent/api/v1"
        private const val userConsentsEndpoint = "userConsents"
        const val dataDonationKey = "data donation"
    }

}
