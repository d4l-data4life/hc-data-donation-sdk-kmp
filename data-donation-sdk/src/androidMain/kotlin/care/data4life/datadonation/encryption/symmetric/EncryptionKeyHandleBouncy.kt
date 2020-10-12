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

package care.data4life.datadonation.encryption.symmetric

import care.data4life.datadonation.encryption.Asn1Exportable
import care.data4life.datadonation.encryption.KeyHandleTink
import care.data4life.datadonation.encryption.assymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.assymetric.EncryptionPublicKey
import care.data4life.datadonation.encryption.protos.PublicHandle
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import io.ktor.util.*
import kotlinx.serialization.DeserializationStrategy
import org.bouncycastle.asn1.cms.GCMParameters
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.ParametersWithIV
import java.security.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class EncryptionSymmetricKeyHandleBouncy(
    private val cipher: Cipher, private val key: Key
) : EncryptionSymmetricKey {

    private val random = SecureRandom()

    override fun decrypt(encrypted: ByteArray, associatedData: ByteArray): Result<ByteArray> {
        val iv = encrypted.sliceArray(0..15)
        val encrypted = encrypted.sliceArray(16..encrypted.lastIndex)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128,iv))
        cipher.updateAAD(associatedData)
        return runCatching { cipher.doFinal(encrypted) }
    }

    override fun encrypt(plainText: ByteArray, associatedData: ByteArray): ByteArray {
        val iv = ByteArray(16)
        random.nextBytes(iv)
        val specParam = GCMParameterSpec(128,iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, specParam, random)
        cipher.updateAAD(associatedData)
        return iv + cipher.doFinal(plainText)
    }

    override fun serialized(): ByteArray = key.encoded

    @OptIn(InternalAPI::class)
    override val pkcs8: String
        get() = serialized().encodeBase64()

}
