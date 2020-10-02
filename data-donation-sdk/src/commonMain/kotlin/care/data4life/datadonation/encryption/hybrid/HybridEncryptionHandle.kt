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

package care.data4life.datadonation.encryption.hybrid

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.assymetric.EncryptionPublicKey
import care.data4life.datadonation.encryption.symmetric.EncryptionSymmetricKey
import care.data4life.datadonation.internal.utils.decodeBase64Bytes


internal class HybridEncryptionHandle(
    private val dataDonationPublicKey: String,
    private val serializer: HybridEncryptionPayload.Serializer
) : HybridEncryption {

    override fun encrypt(plaintext: ByteArray): ByteArray {
        // generate new symmetric key (AES GCM)
        val aesPrivateKey = EncryptionSymmetricKey(
            HybridEncryption.AES_KEY_LENGTH,
            Algorithm.Symmetric.AES(HashSize.Hash256)
        )

        // encrypt symmetric key with asymmetric algorithm (RSA OAEP) using dataDonationPublicKey
        val rsaPublicKey = EncryptionPublicKey(
            dataDonationPublicKey.decodeBase64Bytes(),
            HybridEncryption.RSA_KEY_SIZE_BITS,
            Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256)
        )
        val encryptedAesPrivateKey = rsaPublicKey.encrypt(aesPrivateKey.serialized())
        if (encryptedAesPrivateKey.size != (HybridEncryption.AES_KEY_LENGTH)) {
            throw IllegalStateException("Encrypted key size different than expected")
        }

        // encrypt plaintext with symmetric key
        // TODO double check if it is fine to have empty 'associatedData'
        val ivAndCiphertext = aesPrivateKey.encrypt(plaintext, ByteArray(0))

        // AES encryption returns: iv + ciphertext (ciphertext includes authentication tag)
        val iv = ByteArray(HybridEncryption.AES_IV_LENGTH)
        ivAndCiphertext.copyInto(iv, 0, 0, HybridEncryption.AES_IV_LENGTH)
        val ciphertext = ByteArray(ivAndCiphertext.size - HybridEncryption.AES_IV_LENGTH)
        ivAndCiphertext.copyInto(
            ciphertext,
            0,
            HybridEncryption.AES_IV_LENGTH,
            ivAndCiphertext.size - HybridEncryption.AES_IV_LENGTH
        )

        // Build output
        val payload = HybridEncryptionPayload(encryptedAesPrivateKey, iv, ciphertext)
        return serializer.serialize(payload)
    }
}
