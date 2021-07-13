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
import care.data4life.datadonation.encryption.EncryptionContract
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.model.ConsentSignatureType
import care.data4life.datadonation.internal.data.model.DocumentWithSignature
import care.data4life.datadonation.internal.data.model.DonationPayload
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.lang.CoreRuntimeError
import care.data4life.hl7.fhir.stu3.model.FhirResource
import io.ktor.utils.io.core.*

internal class DonateResources(
    private val redactSensitiveInformation: UsecaseContract.RedactSensitiveInformation,
    private val removeInternalInformation: RemoveInternalInformation,
    private val createRequestConsentPayload: CreateRequestConsentPayload,
    private val donationRepository: RepositoryContract.DonationRepository,
    private val encryptionALP: EncryptionContract.HybridEncryption,
    private val signatureProvider: (KeyPair) -> SignatureKeyPrivate = defaultSignatureProvider
) : ParameterizedUsecase<DonateResources.Parameters, Unit>() {

    companion object {
        private val defaultSignatureProvider = { keyPair: KeyPair ->
            SignatureKeyPrivate(
                keyPair.private,
                keyPair.public,
                2048,
                Algorithm.Signature.RsaPSS(HashSize.Hash256)
            )
        }
    }

    override suspend fun execute() {
        parameter.keyPair?.let {
            donateResources(
                signatureProvider.invoke(it),
                removeInternalInformation.withParams(
                    redactSensitiveInformation.execute(parameter.resources)
                ).execute()
            )
        } ?: throw CoreRuntimeError.MissingCredentialsError()
    }

    private suspend fun donateResources(
        keyPair: SignatureKeyPrivate,
        jsonResources: List<String>
    ) {
        val encryptedSignedMessage = createRequestConsentPayload.withParams(
            CreateRequestConsentPayload.Parameters(
                ConsentSignatureType.NORMAL_USE,
                keyPair
            )
        ).execute()
        val signedEncryptedDocuments = jsonResources.map {
            val encryptedDocument = encryptionALP.encrypt(it.toByteArray())
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

    data class Parameters(val keyPair: KeyPair?, val resources: List<FhirResource>)
}
