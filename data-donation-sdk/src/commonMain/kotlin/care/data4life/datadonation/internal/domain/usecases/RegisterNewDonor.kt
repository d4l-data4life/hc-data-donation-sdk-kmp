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
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.model.*
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.internal.utils.DefaultKeyGenerator
import care.data4life.datadonation.internal.utils.KeyGenerator

internal class RegisterNewDonor(
    private val createRequestConsentPayload: CreateRequestConsentPayload,
    private val registrationRepository: RepositoryContract.RegistrationRepository,
    private val keyGenerator: KeyGenerator = DefaultKeyGenerator
) :
    ParameterizedUsecase<RegisterNewDonor.Parameters, KeyPair>() {

    override suspend fun execute(): KeyPair {
        return if (parameter.keyPair == null) {
            val newKeyPair = keyGenerator.newSignatureKeyPrivate(
                2048,
                Algorithm.Signature.RsaPSS(HashSize.Hash256)
            )
            registerNewDonor(newKeyPair)

            KeyPair(newKeyPair.serializedPublic(), newKeyPair.serializedPrivate())
        } else {
            parameter.keyPair!!
        }
    }

    private suspend fun registerNewDonor(newKeyPair: SignatureKeyPrivate) {
        val payload = createRequestConsentPayload.withParams(
            CreateRequestConsentPayload.Parameters(
                ConsentSignatureType.ConsentOnce,
                newKeyPair
            )
        ).execute()
        registrationRepository.registerNewDonor(payload)
    }

    data class Parameters(val keyPair: KeyPair?)
}
