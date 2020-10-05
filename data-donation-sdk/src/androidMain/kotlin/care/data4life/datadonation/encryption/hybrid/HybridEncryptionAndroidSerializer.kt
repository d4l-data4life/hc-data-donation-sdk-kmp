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
import java.nio.ByteBuffer
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_IV_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_KEY_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.CIPHERTEXT_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.VERSION_LENGTH

internal actual val hybridEncryptionSerializer: HybridEncryptionPayload.Serializer
    get() = HybridEncryptionAndroidSerializer

internal object HybridEncryptionAndroidSerializer : HybridEncryptionPayload.Serializer {

    private const val FIXED_OUTPUT_LENGTH =
        VERSION_LENGTH + AES_IV_SIZE_LENGTH + AES_KEY_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH

    @DangerousInternalIoApi
    override fun serialize(payload: HybridEncryptionPayload): ByteArray {
        val outputLength = FIXED_OUTPUT_LENGTH + payload.ciphertext.size

        val output = ByteArray(outputLength)
        val outputByteBuffer = ByteBuffer.wrap(output)
        val wBuffer = Buffer(Memory(outputByteBuffer))

        payload.apply {
            wBuffer.writeUByte(version.toUByte())
            wBuffer.writeUShort(encryptedSymmetricPrivateKey.size.toUShort())
            wBuffer.writeFully(encryptedSymmetricPrivateKey)
            wBuffer.writeFully(iv)
            wBuffer.writeULong(ciphertext.size.toULong()) // TODO check if it needs to be written as little endian
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
        val versionBytes =  ByteArray(VERSION_LENGTH)
        val versionBuffer = Buffer(Memory(ByteBuffer.wrap(versionBytes)))
        versionBuffer.resetForRead()
        val version = versionBuffer.readUByte().toInt()

        val ciphertextSizeBytes = ByteArray(CIPHERTEXT_SIZE_LENGTH)
        val cipherTextPos = FIXED_OUTPUT_LENGTH
        data.copyInto(ciphertextSizeBytes, 0, cipherTextPos - CIPHERTEXT_SIZE_LENGTH, cipherTextPos)
        val sizeBuffer = Buffer(Memory(ByteBuffer.wrap(ciphertextSizeBytes)))
        sizeBuffer.resetForRead()
        val ciphertextSize = sizeBuffer.readULong().toInt()

        val iv = ByteArray(AES_IV_LENGTH)
        val ciphertext = ByteArray(ciphertextSize)
        val keyPos = VERSION_LENGTH + AES_IV_SIZE_LENGTH
        val ivPos = keyPos + AES_KEY_LENGTH
        data.copyInto(iv, 0, ivPos, ivPos + AES_IV_LENGTH)
        data.copyInto(ciphertext, 0, cipherTextPos, cipherTextPos + ciphertextSize)
        val aesEncryptedKey = ByteArray(AES_KEY_LENGTH)
        data.copyInto(aesEncryptedKey, 0, keyPos, keyPos + AES_KEY_LENGTH)

        return HybridEncryptionPayload(aesEncryptedKey, iv, ciphertext, version)
    }

}
