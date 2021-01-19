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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.hybrid.HybridEncryption
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.exception.MissingCredentialsException
import care.data4life.datadonation.internal.data.model.*
import care.data4life.datadonation.internal.data.service.ConsentService.Companion.defaultDonationConsentKey
import care.data4life.datadonation.internal.domain.repositories.DonationRepository
import care.data4life.datadonation.internal.domain.repositories.UserConsentRepository
import care.data4life.datadonation.internal.utils.Base64Encoder
import care.data4life.datadonation.internal.utils.toJsonString
import io.ktor.utils.io.core.*

internal class DonateResources(
    private val donationRepository: DonationRepository,
    private val consentRepository: UserConsentRepository,
    private val encryptionDD: HybridEncryption,
    private val encryptionALP: HybridEncryption,
    private val base64encoder: Base64Encoder,
    private val signatureProvider: (KeyPair) -> SignatureKeyPrivate = defaultSignatureProvider
) :
    ParameterizedUsecase<DonateResources.Parameters, Unit>() {

    companion object {
        private val defaultSignatureProvider = { keyPair: KeyPair -> SignatureKeyPrivate(
            keyPair.private,
            keyPair.public,
            2048,
            Algorithm.Signature.RsaPSS(HashSize.Hash256)) }
    }

    override suspend fun execute() {
        parameter.keyPair?.let {
            donateResources(signatureProvider.invoke(it), parameter.resources)
        } ?: throw MissingCredentialsException()
    }

    private suspend fun donateResources(keyPair: SignatureKeyPrivate, resources: List<String>) {
        val token = donationRepository.requestDonationToken()
        val request = DonationRequest(keyPair.pkcs8Public, token)
        val encryptedMessage =
            base64encoder.encode(encryptionDD.encrypt(request.toJsonString().toByteArray()))
        val signature = consentRepository.signUserConsent(encryptedMessage)

        val consentMessage = ConsentMessage(
            defaultDonationConsentKey,
            ConsentSignatureType.NormalUse.apiValue,
            encryptedMessage
        )
        val signedMessage = SignedConsentMessage(consentMessage.toJsonString(), signature)
        val encryptedSignedMessage = encryptionDD.encrypt(signedMessage.toJsonString().toByteArray())

        val signedEncryptedDocuments = resources.map {
            val encryptedDocument = encryptionALP.encrypt(it.encodeToByteArray())
            DocumentWithSignature(
                document = encryptedDocument,
                signature = keyPair.sign(encryptedDocument)
            )
        }
        val payload = DonationPayload(
            request = encryptedSignedMessage,
            documents = signedEncryptedDocuments
        )
        donationRepository.donateResources(payload)
    }

    data class Parameters(val keyPair: KeyPair?, val resources: List<String>)


}
