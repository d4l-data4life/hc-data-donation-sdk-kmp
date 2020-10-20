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

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.assymetric.bouncyCastleProvider
import care.data4life.datadonation.encryption.protos.Aes
import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.proto.AesGcmKey
import com.google.crypto.tink.proto.AesGcmKeyFormat
import org.bouncycastle.jcajce.provider.symmetric.AES
import java.security.*
import java.security.spec.KeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


actual fun EncryptionSymmetricKey(size: Int, algorithm: Algorithm.Symmetric): EncryptionSymmetricKey {
    val (cipher,key) = cipherGen(size,algorithm)
    return when (algorithm) {
        is Algorithm.Symmetric.AES -> EncryptionSymmetricKeyHandleBouncy(cipher,key)
    }
}


actual fun EncryptionSymmetricKey(
    serializedKey: ByteArray,
    size: Int,
    algorithm: Algorithm.Symmetric
): EncryptionSymmetricKey {

    val keyAttributes = attributes(algorithm)
    val spec = SecretKeySpec(serializedKey,keyAttributes.second)

    val factory = SecretKeyFactory.getInstance(keyAttributes.second, bouncyCastleProvider)
    val key = factory.generateSecret(spec)
    val cipher = Cipher.getInstance(keyAttributes.first, bouncyCastleProvider)

    return EncryptionSymmetricKeyHandleBouncy(cipher, key)
}

private fun cipherGen(size: Int, algo: Algorithm.Symmetric): Pair<Cipher, Key> {
    val cipherKeyPair = attributes(algo)
    val cipher: Cipher = Cipher.getInstance(cipherKeyPair.first, bouncyCastleProvider)
    val random = SecureRandom()
    val generator = KeyGenerator.getInstance(cipherKeyPair.second, bouncyCastleProvider)

    generator.init(size, random)

    val key: Key = generator.generateKey()
    return cipher to key
}

private fun attributes(algo: Algorithm.Symmetric): Pair<String, String> {
    return when (algo) {
        is Algorithm.Symmetric.AES -> {
            val hash = algo.hashSize
            "AES/GCM/NoPadding" to "AES"
        }
    }
}
