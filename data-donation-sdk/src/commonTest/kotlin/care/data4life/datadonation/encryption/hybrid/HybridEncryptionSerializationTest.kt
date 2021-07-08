/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.datadonation.encryption.hybrid/*
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

import care.data4life.datadonation.encryption.EncryptionContract.Companion.AES_IV_LENGTH
import care.data4life.datadonation.internal.utils.*
import io.ktor.utils.io.core.internal.*
import kotlin.random.Random
import kotlin.test.*

class HybridEncryptionSerializationTest {

    @DangerousInternalIoApi
    @Test
    fun `serialize and deserialize`() {
        val encryptedSymmetricPrivateKey = ByteArray(256)
        Random.nextBytes(encryptedSymmetricPrivateKey)
        val iv = ByteArray(AES_IV_LENGTH)
        Random.nextBytes(iv)
        val ciphertext = ByteArray(Random.nextInt(5, 25))
        val version = Random.nextInt(0, 255)
        val payload = HybridEncryptionPayload(encryptedSymmetricPrivateKey, iv, ciphertext, version)

        val serializedPayload = HybridEncryptionSerializer.serialize(payload)
        val result = HybridEncryptionSerializer.deserialize(serializedPayload)

        val encoder = CommonBase64Encoder
        assertEquals(encoder.encode(result.encryptedSymmetricPrivateKey), encoder.encode(encryptedSymmetricPrivateKey))
        assertEquals(encoder.encode(result.iv), encoder.encode(iv))
        assertEquals(encoder.encode(result.ciphertext), encoder.encode(ciphertext))
        assertEquals(result.version, version)
    }
}
