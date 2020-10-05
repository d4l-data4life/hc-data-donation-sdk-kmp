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

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.assymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.hybrid.HybridEncryption
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_AUTH_TAG_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_KEY_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.RSA_KEY_SIZE_BITS
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionHandle
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_IV_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.CIPHERTEXT_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.VERSION_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionSymmetricKeyProvider
import care.data4life.datadonation.encryption.hybrid.hybridEncryptionSerializer
import care.data4life.datadonation.encryption.initEncryption
import care.data4life.datadonation.internal.utils.CommonBase64Encoder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HybridEncryptionTest {

    private val rsaKey =
        EncryptionPrivateKey(RSA_KEY_SIZE_BITS, Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))
    private val handle = HybridEncryptionHandle(HybridEncryptionSymmetricKeyProvider,
        object: HybridEncryption.AsymmetricKeyProvider {
            override fun getPublicKey() = rsaKey
            override fun getPrivateKey() = rsaKey
        },
        hybridEncryptionSerializer)

    @BeforeTest
    fun setup() {
        initEncryption()
    }

    @Test
    fun `Generate, encrypt and decrypt`() {
        val plaintext = byteArrayOf(1, 2, 3, 4, 5) // TODO make this data random one the test works properly

        val hybridEncryptedResult = handle.encrypt(plaintext)
        // ciphertext same length as plaintext
        // expected output size: version (1) + iv size value (2) + encryptedKey.size (16) + iv.size (12) + ciphertext size value (8) + ciphertext.size
        val expectedLength =
            VERSION_LENGTH + AES_IV_SIZE_LENGTH + AES_KEY_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH + plaintext.size + AES_AUTH_TAG_LENGTH
        assertEquals(hybridEncryptedResult.size, expectedLength)

        val result = handle.decrypt(hybridEncryptedResult)
        assertTrue(result.isSuccess)

        val encoder = CommonBase64Encoder
        assertEquals(encoder.encode(result.getOrThrow()), encoder.encode(plaintext))
    }

}
