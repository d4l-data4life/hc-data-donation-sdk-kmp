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

import CapturingResultListener
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.hybrid.HybridEncryption
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.model.ConsentMessage
import care.data4life.datadonation.internal.data.model.ConsentRequest
import care.data4life.datadonation.internal.data.model.ConsentSignatureType
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.data.model.SignedConsentMessage
import care.data4life.datadonation.internal.data.service.ConsentService
import care.data4life.datadonation.internal.domain.mock.MockRegistrationDataStore
import care.data4life.datadonation.internal.domain.mock.MockServiceTokenDataStore
import care.data4life.datadonation.internal.domain.mock.MockUserConsentRepository
import care.data4life.datadonation.internal.domain.repositories.RegistrationRepository
import care.data4life.datadonation.internal.domain.repositories.ServiceTokenRepository
import care.data4life.datadonation.internal.utils.Base64Encoder
import care.data4life.datadonation.internal.utils.KeyGenerator
import care.data4life.datadonation.internal.utils.toJsonString
import io.ktor.utils.io.charsets.Charset
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class RegisterNewDonorTest {

    private val dummyNonce = "random_nonce"
    private val dummyPublicKey64Encoded = "publicKey64Encoded"
    private val dummySignature = "signature"
    private val dummyEncryptedRequest = byteArrayOf(1, 2, 3)
    private val dummyEncryptedRequest64Encoded = "encryptedRequest64Encoded"
    private val dummyEncryptedSignedMessage = byteArrayOf(4, 5)

    private val mockServiceTokenDataStore = MockServiceTokenDataStore()
    private val mockRegistrationDataStore = MockRegistrationDataStore()
    private val serviceTokenRepository = ServiceTokenRepository(mockServiceTokenDataStore)
    private val mockUserConsentRepository = MockUserConsentRepository()
    private val registrationRepository = RegistrationRepository(mockRegistrationDataStore)

    private val signatureKey = object: SignatureKeyPrivate {
        override fun sign(data: ByteArray) = byteArrayOf()
        override fun serializedPrivate() = DummyData.keyPair.private
        override val pkcs8Private = ""
        override fun verify(data: ByteArray, signature: ByteArray) = true
        override fun serializedPublic() = DummyData.keyPair.public
        override val pkcs8Public = ""
    }

    private val requestJsonString =
        ConsentRequest(signatureKey.pkcs8Public, dummyNonce).toJsonString()

    private val consentMessage = ConsentMessage(
        ConsentService.defaultDonationConsentKey,
        ConsentSignatureType.ConsentOnce.apiValue,
        dummyEncryptedRequest64Encoded
    )

    private val signedConsentJsonString =
        SignedConsentMessage(consentMessage.toJsonString(), dummySignature).toJsonString()

    private val encryptorDD = object : HybridEncryption {
        override fun encrypt(plaintext: ByteArray) = when (plaintext.decodeToString()) {
            requestJsonString -> dummyEncryptedRequest
            signedConsentJsonString -> dummyEncryptedSignedMessage
            else -> byteArrayOf()
        }
        override fun decrypt(ciphertext: ByteArray) = Result.success(byteArrayOf())

    }

    private val base64Encoder = object : Base64Encoder {
        override fun encode(src: ByteArray) = dummyEncryptedRequest64Encoded
        override fun decode(src: ByteArray, charset: Charset) = ""

    }

    private val mockKeyGenerator = object : KeyGenerator {
        override fun newSignatureKeyPrivate(
            size: Int,
            algorithm: Algorithm.Signature
        ): SignatureKeyPrivate = signatureKey
    }

    private val createRequestConsentPayload = CreateRequestConsentPayload(
        serviceTokenRepository,
        mockUserConsentRepository,
        encryptorDD,
        base64Encoder
    )

    private val registerNewDonor =
        RegisterNewDonor(
            createRequestConsentPayload,
            registrationRepository,
            mockKeyGenerator
        )

    private val capturingListener = RegisterNewDonorListener()

    @Test
    fun registerNewDonorTestWithoutKey() = runTest {
        //Given
        mockServiceTokenDataStore.whenRequestDonationToken = { dummyNonce }
        mockUserConsentRepository.whenSignUserConsent = { _ -> dummySignature }

        //When
        registerNewDonor.runWithParams(RegisterNewDonor.Parameters(null), capturingListener)

        //Then
        assertEquals(
            capturingListener.captured,
            KeyPair(signatureKey.serializedPublic(), signatureKey.serializedPrivate())
        )
        assertNull(capturingListener.error)
    }

    @Test
    fun registerNewDonorTestWithKey() = runTest {
        //Given

        //When
        registerNewDonor.runWithParams(RegisterNewDonor.Parameters(DummyData.keyPair), capturingListener)

        //Then
        assertEquals(capturingListener.captured, DummyData.keyPair)
        assertNull(capturingListener.error)
    }

    class RegisterNewDonorListener: CapturingResultListener<KeyPair>()

}

