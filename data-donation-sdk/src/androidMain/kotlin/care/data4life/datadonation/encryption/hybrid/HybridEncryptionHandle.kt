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
import care.data4life.datadonation.internal.domain.repositories.CredentialsRepository
import care.data4life.datadonation.internal.utils.decodeBase64Bytes
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import java.nio.ByteBuffer

internal actual class HybridEncryptionHandle(repository: CredentialsRepository) :
    HybridEncryptor {

    private val dataDonationPublicKey = repository.getDataDonationPublicKey()

    @DangerousInternalIoApi
    actual override fun encrypt(plaintext: ByteArray): ByteArray {
        // generate new symmetric key (AES GCM)
        val aesPrivateKey = EncryptionSymmetricKey(
            HybridEncryptor.AES_KEY_LENGTH,
            Algorithm.Symmetric.AES(HashSize.Hash256)
        )

        // encrypt symmetric key with asymmetric algorithm (RSA OAEP) using dataDonationPublicKey
        val rsaPublicKey = EncryptionPublicKey(
            dataDonationPublicKey.decodeBase64Bytes(),
            HybridEncryptor.RSA_KEY_SIZE_BITS,
            Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256)
        )
        val encryptedAesPrivateKey = rsaPublicKey.encrypt(aesPrivateKey.serialized())
        if (encryptedAesPrivateKey.size != HybridEncryptor.AES_KEY_LENGTH) {
            throw IllegalStateException("Encrypted key size different than expected")
        }

        // encrypt plaintext with symmetric key
        // TODO double check if it is fine to have empty 'associatedData'
        val ivAndCiphertext = aesPrivateKey.encrypt(plaintext, ByteArray(0))

        // AES encryption returns: iv + ciphertext (ciphertext includes authentication tag)
        val iv = ByteArray(HybridEncryptor.AES_IV_LENGTH)
        ivAndCiphertext.copyInto(iv, 0, 0)
        val ciphertext = ByteArray(ivAndCiphertext.size - HybridEncryptor.AES_IV_LENGTH)
        ivAndCiphertext.copyInto(ciphertext, 0, HybridEncryptor.AES_IV_LENGTH)

        // extract initialisation vector
        /*val iv = GCMParameterSpec(
            8 * HybridEncryptor.AES_TAG_LENGTH,
            ciphertext,
            0,
            HybridEncryptor.AES_IV_LENGTH
        ).iv*/

        // Build output
        val version = HybridEncryptor.HYBRID_ENCRYPTION_VERSION_AES_WITH_GCM.toUByte()
        val keyLength = HybridEncryptor.AES_KEY_LENGTH.toUShort()
        val ciphertextLength = ciphertext.size.toULong()

        // Output ByteArray order: version, keyLength, encryptedAesPrivateKey, iv, ciphertextLength, ciphertext
        // Output ByteArray size: 1 + 2 + encryptedKey.size (16) + iv.size (12) + 8 + ciphertext.size
        val outputLength =
            UByte.SIZE_BYTES + UShort.SIZE_BYTES + HybridEncryptor.AES_KEY_LENGTH + HybridEncryptor.AES_IV_LENGTH + ULong.SIZE_BYTES + ciphertext.size

        val output = ByteArray(outputLength)
        val outputByteBuffer = ByteBuffer.wrap(output)
        val wBuffer = Buffer(Memory(outputByteBuffer))

        wBuffer.writeUByte(version)
        wBuffer.writeUShort(keyLength)
        wBuffer.writeFully(encryptedAesPrivateKey)
        wBuffer.writeULong(ciphertextLength)
        wBuffer.writeFully(iv)
        wBuffer.writeULong(ciphertextLength) // TODO check if it needs to be written as little endian
        wBuffer.writeFully(ciphertext)

        if (wBuffer.writePosition != outputLength) {
            throw IllegalStateException("Output size different than expected")
        }

        return output
    }

}
