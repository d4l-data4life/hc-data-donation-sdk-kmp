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
import care.data4life.datadonation.internal.data.model.RegistrationRequest
import care.data4life.datadonation.internal.data.model.SignedConsentMessage
import care.data4life.datadonation.internal.domain.repositories.RegistrationRepository
import care.data4life.datadonation.internal.domain.repositories.UserConsentRepository
import care.data4life.datadonation.internal.domain.usecases.RegisterNewDonor.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json.Default.stringify

internal class RegisterNewDonor(
    private val registrationRepository: RegistrationRepository,
    private val consentRepository: UserConsentRepository
) :
    ParameterizedUsecase<Parameters, Unit>() {

    @UnstableDefault
    override suspend fun execute() {
        val token = registrationRepository.requestRegistrationToken()
        val request = RegistrationRequest(parameter.keyPair.public.toBase64(), token)
        val message = request.encrypt(parameter.donationPublicKey).toBase64()
        val signature = consentRepository.signUserConsent(message)
        val signedMessage = SignedConsentMessage(message, signature)
        val payload = signedMessage.encrypt(parameter.donationPublicKey)
        registrationRepository.registerNewDonor(payload)
    }

    data class Parameters(val keyPair: KeyPair, val donationPublicKey: String)

}



@UnstableDefault
private fun RegistrationRequest.encrypt(dataDonationKey: String): ByteArray {
    return stringify(RegistrationRequest.serializer(), this).encrypt(dataDonationKey)
}

@UnstableDefault
private fun SignedConsentMessage.encrypt(dataDonationKey: String): ByteArray {
    return stringify(SignedConsentMessage.serializer(), this).encrypt(dataDonationKey)
}

// TODO implement these methods and move to utility classes
private fun String.encrypt(dataDonationKet: String): ByteArray {
    // String is an RSA public key in PEM / SPKI format
    // apply RSA_OAEP algorithm to data
    return ByteArray(0)
}

private fun ByteArray.toBase64(): String {
    return ""
}
