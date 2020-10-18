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

import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_KEY_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.CIPHERTEXT_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.VERSION_LENGTH
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import java.nio.ByteBuffer

internal actual val hybridEncryptionSerializer: HybridEncryptionPayload.Serializer
    get() = HybridEncryptionAndroidSerializer

internal object HybridEncryptionAndroidSerializer : HybridEncryptionPayload.Serializer {

    private const val FIXED_OUTPUT_LENGTH =
        VERSION_LENGTH + AES_KEY_SIZE_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH

    @DangerousInternalIoApi
    override fun serialize(payload: HybridEncryptionPayload): ByteArray {
        val outputLength =
            FIXED_OUTPUT_LENGTH + payload.encryptedSymmetricPrivateKey.size + payload.ciphertext.size

        val output = ByteArray(outputLength)
        val outputByteBuffer = ByteBuffer.wrap(output)
        val wBuffer = Buffer(Memory(outputByteBuffer))

        payload.apply {
            //version
            wBuffer.writeUByte(version.toUByte())
            //encKeyLen
            wBuffer.writeUShort(
                encryptedSymmetricPrivateKey.size.toUShort().reverseByteOrder()
            ) // little endian
            //encKey
            wBuffer.writeFully(encryptedSymmetricPrivateKey)
            //iv
            wBuffer.writeFully(iv)
            //ciphertextLen
            wBuffer.writeULong(ciphertext.size.toULong().reverseByteOrder()) // little endian
            //ciphertext
            wBuffer.writeFully(ciphertext)
        }

        if (wBuffer.writePosition != outputLength) {
            throw IllegalStateException("Output size different than expected")
        }

        return output
    }

    @DangerousInternalIoApi
    override fun deserialize(data: ByteArray): HybridEncryptionPayload {
        if (data.size <= FIXED_OUTPUT_LENGTH) {
            throw IllegalArgumentException("Input data too short")
        }
        //version
        val versionBytes = ByteArray(VERSION_LENGTH)
        data.copyInto(versionBytes, 0, 0, VERSION_LENGTH)
        val versionBuffer = Buffer(Memory(ByteBuffer.wrap(versionBytes)))
        versionBuffer.resetForRead()
        val version = versionBuffer.readUByte().toInt()

        //encKeyLen
        val keySizeBytes = ByteArray(AES_KEY_SIZE_LENGTH)
        data.copyInto(keySizeBytes, 0, VERSION_LENGTH, VERSION_LENGTH + AES_KEY_SIZE_LENGTH)
        val keySizeBuffer = Buffer(Memory(ByteBuffer.wrap(keySizeBytes)))
        keySizeBuffer.resetForRead()

        val keySizeBytesReverse = ByteArray(AES_KEY_SIZE_LENGTH)
        keySizeBuffer.readAvailable(keySizeBytesReverse)
        keySizeBytesReverse.reverse()
        val keySizeBufferReverse = Buffer(Memory(ByteBuffer.wrap(keySizeBytesReverse)))
        keySizeBufferReverse.resetForRead()

        val keySize = keySizeBufferReverse.readUShort().toInt()

        //encKey
        val keyPos = VERSION_LENGTH + AES_KEY_SIZE_LENGTH
        val aesEncryptedKey = ByteArray(keySize)
        data.copyInto(aesEncryptedKey, 0, keyPos, keyPos + keySize)

        //IV
        val ivPos = keyPos + keySize
        val iv = ByteArray(AES_IV_LENGTH)
        data.copyInto(iv, 0, ivPos, ivPos + AES_IV_LENGTH)

        //ciphertextLen
        val cipherSizeBytes = ByteArray(CIPHERTEXT_SIZE_LENGTH)
        val ciphertextPos = FIXED_OUTPUT_LENGTH + keySize
        data.copyInto(cipherSizeBytes, 0, ciphertextPos - CIPHERTEXT_SIZE_LENGTH, ciphertextPos)
        val cipherSizeBuffer = Buffer(Memory(ByteBuffer.wrap(cipherSizeBytes)))
        cipherSizeBuffer.resetForRead()

        val cipherSizeBytesReverse = ByteArray(CIPHERTEXT_SIZE_LENGTH)
        cipherSizeBuffer.readAvailable(cipherSizeBytesReverse)
        cipherSizeBytesReverse.reverse()
        val cipherSizeBufferReverse = Buffer(Memory(ByteBuffer.wrap(cipherSizeBytesReverse)))
        cipherSizeBufferReverse.resetForRead()

        val cipherSize = cipherSizeBufferReverse.readULong().toInt()

        //ciphertext
        val ciphertext = ByteArray(cipherSize)
        data.copyInto(ciphertext, 0, ciphertextPos, ciphertextPos + cipherSize)

        return HybridEncryptionPayload(aesEncryptedKey, iv, ciphertext, version)
    }

}
