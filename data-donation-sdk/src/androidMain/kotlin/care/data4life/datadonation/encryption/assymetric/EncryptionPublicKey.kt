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

import care.data4life.datadonation.encryption.Algorithm
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


actual fun EncryptionPublicKey(
    serializedKey: ByteArray,
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPublicKey {
    val (_, key, cipher) = publicKey(algorithm, serializedKey)
    return EncryptionKeyPublicHandleBouncy(cipher,key)
}

private fun publicKey(
    algorithm: Algorithm.Asymmetric,
    serializedKey: ByteArray
): Triple<KeyFactory, PublicKey, Cipher> {
    val keyAttributes = attributes(algorithm)
    val spec = X509EncodedKeySpec(serializedKey)
    val factory = KeyFactory.getInstance(keyAttributes.second, bouncyCastleProvider)
    val key = factory.generatePublic(spec)
    val cipher = Cipher.getInstance(keyAttributes.first, bouncyCastleProvider)
    return Triple(factory, key, cipher)
}

actual fun EncryptionPrivateKey(
    serializedPrivate: ByteArray,
    serializedPublic: ByteArray,
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPrivateKey {
    val (factory, publicKey, cipher) = publicKey(algorithm, serializedPublic)
    val privateSpec = PKCS8EncodedKeySpec(serializedPrivate)
    val privateKey = factory.generatePrivate(privateSpec)
    return EncryptionKeyPrivateHandleBouncy(cipher,privateKey,publicKey)
}

actual fun EncryptionPrivateKey(
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPrivateKey {

    return when(algorithm) {
        is Algorithm.Asymmetric.RsaOAEP -> {
            val (cipher,keyPair) = cipherGen(size,algorithm)
            EncryptionKeyPrivateHandleBouncy(cipher,keyPair.private,keyPair.public)
        }
    }
}


private fun cipherGen(size: Int, algo: Algorithm.Asymmetric): Pair<Cipher, KeyPair> {
    val cipherKeyPair = attributes(algo)
    val cipher: Cipher = Cipher.getInstance(cipherKeyPair.first, bouncyCastleProvider)
    val random = SecureRandom()
    val generator: KeyPairGenerator = KeyPairGenerator.getInstance(cipherKeyPair.second, bouncyCastleProvider)

    generator.initialize(size, random)

    val keyPair: KeyPair = generator.generateKeyPair()
    return cipher to keyPair
}

private fun attributes(algo: Algorithm.Asymmetric): Pair<String, String> {
    return when (algo) {
        is Algorithm.Asymmetric.RsaOAEP -> {
            val hash = algo.hashSize
            "RSA/None/OAEPWithSHA${hash.bits}AndMGF1Padding" to "RSA"
        }
    }
}

internal val bouncyCastleProvider by lazy { BouncyCastleProvider() }
