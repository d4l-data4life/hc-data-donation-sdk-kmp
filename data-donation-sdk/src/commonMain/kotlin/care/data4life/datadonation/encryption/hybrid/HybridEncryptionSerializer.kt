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

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.VERSION_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_KEY_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.CIPHERTEXT_SIZE_LENGTH



object HybridEncryptionSerializer : HybridEncryptionPayload.Serializer {

    private const val FIXED_OUTPUT_LENGTH =
        VERSION_LENGTH + AES_KEY_SIZE_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH

    @DangerousInternalIoApi
    override fun serialize(payload: HybridEncryptionPayload): ByteArray {
        val outputLength =
            FIXED_OUTPUT_LENGTH + payload.encryptedSymmetricPrivateKey.size + payload.ciphertext.size

        val output = ByteArray(outputLength)

        val writtenLength = payload.let {
            buildPacket {
                //version
                writeUByte(it.version.toUByte())
                //encKeyLen
                writeUShort(
                    it.encryptedSymmetricPrivateKey.size.toUShort().reverseByteOrder()
                ) // little endian
                //encKey
                writeFully(it.encryptedSymmetricPrivateKey)
                //iv
                writeFully(it.iv)
                //ciphertextLen
                writeULong(it.ciphertext.size.toULong().reverseByteOrder()) // little endian
                //ciphertext
                writeFully(it.ciphertext)

            }.readAvailable(output)
        }

        if (writtenLength != outputLength) {
            throw IllegalStateException("Unexpected length of written data")
        }
        
        return output
    }

    @DangerousInternalIoApi
    override fun deserialize(data: ByteArray): HybridEncryptionPayload {
        if (data.size <= FIXED_OUTPUT_LENGTH) {
            throw IllegalArgumentException("Input data too short")
        }

        return ByteReadPacket(data, 0, data.size).let {
            //version
            val version = it.readUByte().toInt()
            //encKeyLen
            val keySize = it.readShortLittleEndian().toUShort().toInt()
            //encKey
            val aesEncryptedKey = ByteArray(keySize)
            it.readFully(aesEncryptedKey)
            //iv
            val iv = ByteArray(AES_IV_LENGTH)
            it.readFully(iv)
            //ciphertextLen
            val cipherSize = it.readLongLittleEndian().toULong().toInt()
            //ciphertext
            val ciphertext = ByteArray(cipherSize)
            it.readFully(ciphertext)

            HybridEncryptionPayload(aesEncryptedKey, iv, ciphertext, version)
        }
    }
}
