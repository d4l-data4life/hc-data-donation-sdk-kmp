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

package care.data4life.datadonation.encryption.assymetric

import io.ktor.util.*
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import javax.crypto.Cipher


class EncryptionKeyPrivateHandleBouncy(
    private val cipher: Cipher, private val privateKey: PrivateKey, private val publicKey: PublicKey
) : EncryptionPrivateKey,
    EncryptionPublicKey by EncryptionKeyPublicHandleBouncy(cipher, publicKey) {

    override fun serializedPrivate(): ByteArray = privateKey.encoded

    @OptIn(InternalAPI::class)
    override val pkcs8Private: String
        get() = privateKey.encoded.encodeBase64()

    override fun decrypt(data: ByteArray): Result<ByteArray> {
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return runCatching { cipher.doFinal(data) }
    }

}

class EncryptionKeyPublicHandleBouncy(
    private val cipher: Cipher, private val publicKey: PublicKey
) : EncryptionPublicKey {
    private val random = SecureRandom()
    override fun encrypt(data: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, random)
        return cipher.doFinal(data)
    }

    override fun serializedPublic(): ByteArray = publicKey.encoded

    @OptIn(InternalAPI::class)
    override val pkcs8Public: String
        get() = publicKey.encoded.encodeBase64()

}
