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

import care.data4life.datadonation.toByteArray
import care.data4life.datadonation.toNSData
import crypto.dd.CryptoAES
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Security.*

class EncryptionSymmetricKeyNative : EncryptionSymmetricKey {

    private val key: ByteArray
    private val cryptoWrapper: CryptoAES

    // TODO: Support for different padding
    constructor(size: Int) {
        val randomByteArray = secureRandomByteArray(size / 8)
        this.key = randomByteArray
        this.cryptoWrapper = CryptoAES(randomByteArray.toNSData())
    }

    constructor(key: ByteArray) {
        this.key = key
        this.cryptoWrapper = CryptoAES(key.toNSData())
    }

    override fun decrypt(encrypted: ByteArray, associatedData: ByteArray): Result<ByteArray> = runCatching {
        val iv = encrypted.sliceArray(0..15)
        val encrypted = encrypted.sliceArray(16..encrypted.lastIndex)
        cryptoWrapper.decryptWithEncrypted(encrypted.toNSData(), iv.toNSData(), associatedData.toNSData())!!.toByteArray()
    }

    override fun serialized(): ByteArray = key

    override val pkcs8: String
        get() = TODO("Not yet implemented")

    override fun encrypt(plainText: ByteArray, associatedData: ByteArray): ByteArray = memScoped {
        val iv = secureRandomByteArray(16)
        val encrypted = cryptoWrapper.encryptWithPlainText(plainText.toNSData(), iv.toNSData(), associatedData.toNSData())!!.toByteArray()
        if (encrypted.isEmpty()) {
            throw Throwable("Unknown encryption error")
        }
        return iv + encrypted
    }
}

internal fun secureRandomByteArray(size: Int) = ByteArray(size).usePinned {
    val r = SecRandomCopyBytes(kSecRandomDefault, size.toULong(), it.addressOf(0))
    if (r != errSecSuccess)
        throw Throwable("Could not generate secure ByteArray")
    it.get()
}
