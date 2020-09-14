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

import care.data4life.datadonation.encryption.hybrid.HybridEncryptor
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.data.model.RegistrationRequest
import care.data4life.datadonation.internal.data.model.SignedConsentMessage
import care.data4life.datadonation.internal.domain.repositories.RegistrationRepository
import care.data4life.datadonation.internal.domain.repositories.UserConsentRepository
import care.data4life.datadonation.internal.utils.Base64Encoder
import care.data4life.datadonation.internal.utils.toJsonString
import io.mockk.*
import runTest
import kotlin.test.Test

abstract class RegisterNewDonorTest {

    private val userConsentRepository = mockk<UserConsentRepository>()
    private val registrationRepository = mockk<RegistrationRepository>()
    private val encryptor = mockk<HybridEncryptor>()
    private val base64Encoder = mockk<Base64Encoder>()
    private val registerNewDonor =
        RegisterNewDonor(registrationRepository, userConsentRepository, encryptor, base64Encoder)

    private val dummyNonce = "random_nonce"
    private val dummyPublicKey64Encoded = "publicKey64Encoded"
    private val dummySignature = "signature"
    private val dummyEncryptedRequest = byteArrayOf(1, 2, 3)
    private val dummyEncryptedRequest64Encoded = "encryptedRequest64Encoded"
    private val dummyEncryptedSignedMessage = byteArrayOf(4, 5)

    @Test
    fun registerNewDonorTest() = runTest {
        //Given
        val requestJsonString =
            RegistrationRequest(dummyPublicKey64Encoded, dummyNonce).toJsonString()
        val signedConsentJsonString =
            SignedConsentMessage(dummyEncryptedRequest64Encoded, dummySignature).toJsonString()

        coEvery { registrationRepository.requestRegistrationToken() } returns dummyNonce
        coEvery { userConsentRepository.signUserConsent(any()) } returns dummySignature
        coEvery { base64Encoder.encode(any()) } returns dummyPublicKey64Encoded andThen dummyEncryptedRequest64Encoded
        coEvery { encryptor.encrypt(any()) } returns dummyEncryptedRequest andThen dummyEncryptedSignedMessage
        coEvery { registrationRepository.registerNewDonor(any()) } just runs

        //When
        registerNewDonor.withParams(DummyData.keyPair).execute()

        //Then
        coVerify(ordering = Ordering.SEQUENCE){
            registrationRepository.requestRegistrationToken()
            base64Encoder.encode(DummyData.keyPair.public)
            encryptor.encrypt(requestJsonString)
            base64Encoder.encode(dummyEncryptedRequest)
            userConsentRepository.signUserConsent(dummyEncryptedRequest64Encoded)
            encryptor.encrypt(signedConsentJsonString)
            registrationRepository.registerNewDonor(dummyEncryptedSignedMessage)
        }
    }

}
