/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2021, D4L data4life gGmbH
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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.encryption.hybrid.HybridEncryption
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.model.*
import care.data4life.datadonation.internal.data.service.ConsentService
import care.data4life.datadonation.internal.domain.repositories.ServiceTokenRepository
import care.data4life.datadonation.internal.domain.repositories.UserConsentRepository
import care.data4life.datadonation.internal.utils.Base64Encoder
import care.data4life.datadonation.internal.utils.toJsonString
import io.ktor.utils.io.core.*

internal class CreateRequestConsentPayload(
    private val serviceTokenRepository: ServiceTokenRepository,
    private val consentRepository: UserConsentRepository,
    private val encryptionDD: HybridEncryption,
    private val base64encoder: Base64Encoder,
):
    ParameterizedUsecase<CreateRequestConsentPayload.Parameters, ByteArray>() {

    override suspend fun execute(): ByteArray {
        val token = serviceTokenRepository.requestDonationToken()
        val request = ConsentRequest(parameter.SignatureKeyPrivate.pkcs8Public, token)
        val encryptedMessage =
            base64encoder.encode(encryptionDD.encrypt(request.toJsonString().toByteArray()))

        val signature = when (parameter.signatureType) {
            ConsentSignatureType.ConsentOnce -> {
                consentRepository.signUserConsentRegistration(encryptedMessage)
            }
            ConsentSignatureType.NormalUse -> {
                consentRepository.signUserConsentDonation(encryptedMessage)
            }
            else -> throw UnsupportedOperationException()
        }

        val consentMessage = ConsentMessage(
            ConsentService.defaultDonationConsentKey,
            parameter.signatureType.apiValue,
            encryptedMessage
        )
        val signedMessage = SignedConsentMessage(consentMessage.toJsonString(), signature)
        return encryptionDD.encrypt(signedMessage.toJsonString().toByteArray())
    }

    data class Parameters(
        val signatureType: ConsentSignatureType,
        val SignatureKeyPrivate: SignatureKeyPrivate
    )
}
