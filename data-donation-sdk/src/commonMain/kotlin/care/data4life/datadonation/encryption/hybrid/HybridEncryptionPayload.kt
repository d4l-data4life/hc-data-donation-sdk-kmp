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

class HybridEncryptionPayload(
    val encryptedAesPrivateKey: ByteArray,
    val iv: ByteArray,
    val ciphertext: ByteArray,
    val version: Int = HYBRID_ENCRYPTION_VERSION_AES_WITH_GCM
) {

    companion object {
        const val HYBRID_ENCRYPTION_VERSION_AES_WITH_GCM = HybridEncryption.HYBRID_ENCRYPTION_VERSION_AES_WITH_GCM
        const val VERSION_LENGTH = UByte.SIZE_BYTES
        const val AES_IV_SIZE_LENGTH = UShort.SIZE_BYTES // The IV size must be encoded in the payload even if its value is always 12 bytes for AES GCM
        const val CIPHERTEXT_SIZE_LENGTH = ULong.SIZE_BYTES
        const val AES_IV_LENGTH = HybridEncryption.AES_IV_LENGTH
        const val AES_AUTH_TAG_LENGTH = HybridEncryption.AES_AUTH_TAG_LENGTH
        const val AES_KEY_LENGTH = HybridEncryption.AES_KEY_LENGTH
        const val RSA_KEY_SIZE_BITS = HybridEncryption.RSA_KEY_SIZE_BITS
    }

    interface Serializer {
        fun serialize(payload: HybridEncryptionPayload): ByteArray
        fun deserialize(data: ByteArray): HybridEncryptionPayload
    }
}
