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

package care.data4life.datadonation.encryption

import care.data4life.datadonation.encryption.assymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_AUTH_TAG_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_KEY_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.RSA_KEY_SIZE_BITS
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionHandle
import care.data4life.datadonation.encryption.hybrid.hybridEncryptionSerializer
import care.data4life.datadonation.encryption.symmetric.EncryptionSymmetricKey
import care.data4life.datadonation.internal.utils.CommonBase64Encoder
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import org.junit.Before
import org.junit.Test
import java.nio.ByteBuffer
import kotlin.test.assertEquals

class HybridEncryptionTest {

    private val rsaKey =
        EncryptionPrivateKey(RSA_KEY_SIZE_BITS, Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))
    private val publicKeyBase64 = CommonBase64Encoder.encode(rsaKey.serializedPublic())
    private val handle = HybridEncryptionHandle(publicKeyBase64, hybridEncryptionSerializer)

    @Before
    fun setup() {
        initEncryption()
    }

    @Test
    @DangerousInternalIoApi
    fun `Generate, encrypt and decrypt`() {
        val plaintext = byteArrayOf(1, 2, 3, 4, 5) // TODO make this data random one the test works properly

        val hybridEncryptedResult = handle.encrypt(plaintext)
        // ciphertext same length as plaintext
        // expected output size: 1 + 2 + encryptedKey.size (16) + iv.size (12) + 8 + ciphertext.size
        val expectedLength =
            1 + 2 + AES_KEY_LENGTH + AES_IV_LENGTH + 8 + plaintext.size + AES_AUTH_TAG_LENGTH
        assertEquals(hybridEncryptedResult.size, expectedLength)

        val result = hybridEncryptedResult.hybridDecrypt()
        assertEquals(result.toString(), plaintext.toString())
    }

    @DangerousInternalIoApi
    private fun ByteArray.hybridDecrypt(): ByteArray {
        val ciphertextSizeBytes = ByteArray(8)
        val cipherTextPos = 1 + 2 + AES_KEY_LENGTH + AES_IV_LENGTH + 8
        copyInto(ciphertextSizeBytes, 0, cipherTextPos - 8 , cipherTextPos)
        val buffer = Buffer(Memory(ByteBuffer.wrap(ciphertextSizeBytes)))
        buffer.resetForRead()
        val ciphertextSize = buffer.readULong().toInt()
        // ciphertext decryption requires iv + ciphertext as input (ciphertext includes authentication tag)
        val ivAndCiphertext = ByteArray(AES_IV_LENGTH + ciphertextSize)
        val keyPos = 1 + 2
        val ivPos = keyPos + AES_KEY_LENGTH
        copyInto(ivAndCiphertext, 0, ivPos, ivPos + AES_IV_LENGTH)
        copyInto(ivAndCiphertext, AES_IV_LENGTH, cipherTextPos, cipherTextPos + ciphertextSize)
        val aesEncryptedKey = ByteArray(AES_KEY_LENGTH)
        copyInto(aesEncryptedKey, 0, keyPos, keyPos + AES_KEY_LENGTH)
        val aesKeyResult = rsaKey.decrypt(aesEncryptedKey)
        val aesKey = EncryptionSymmetricKey(
            aesKeyResult.getOrThrow(),
            AES_KEY_LENGTH,
            Algorithm.Symmetric.AES(HashSize.Hash256)
        )
        val result = aesKey.decrypt(ivAndCiphertext, ByteArray(0))
        return result.getOrThrow()
    }
}
