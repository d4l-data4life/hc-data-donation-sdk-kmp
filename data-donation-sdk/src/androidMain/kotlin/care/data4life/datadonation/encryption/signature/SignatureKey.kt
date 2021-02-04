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

package care.data4life.datadonation.encryption.signature


import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.assymetric.bouncyCastleProvider
import java.security.*
import java.security.spec.*


actual fun SignatureKeyPrivate(size: Int,algorithm: Algorithm.Signature): SignatureKeyPrivate {
    return when(algorithm) {
        is Algorithm.Signature.RsaPSS -> {
            val (signature, key) = signatureGen(size,algorithm)
            SignatureKeyPrivateHandleBouncy(signature,key.private,key.public)
        }
    }
}


actual fun SignatureKeyPrivate(
    serializedPrivate: ByteArray,
    serializedPublic: ByteArray,
    size: Int,
    algorithm: Algorithm.Signature
): SignatureKeyPrivate {
    val (factory, publicKey, signature) = publicKey(algorithm, serializedPublic)
    val privateSpec = PKCS8EncodedKeySpec(serializedPrivate)
    val privateKey = factory.generatePrivate(privateSpec)

    return SignatureKeyPrivateHandleBouncy(signature,privateKey,publicKey)
}

actual fun SignatureKeyPublic(
    serialized: ByteArray,
    size: Int,
    algorithm: Algorithm.Signature
): SignatureKeyPublic {
    val (_, publicKey, signature) = publicKey(algorithm, serialized)
    return SignatureKeyPublicHandleBouncy(signature, publicKey)
}

private fun attributes(algo: Algorithm.Signature): Pair<String, String> {
    return when (algo) {
        is Algorithm.Signature.RsaPSS -> "SHA256withRSA/PSS" to "RSA"
    }
}

private fun publicKey(
    algorithm: Algorithm.Signature,
    serializedKey: ByteArray
): Triple<KeyFactory, PublicKey, Signature> {
    val keyAttributes = attributes(algorithm)
    val spec = X509EncodedKeySpec(serializedKey)
    val factory = KeyFactory.getInstance(keyAttributes.second, bouncyCastleProvider)
    val key = factory.generatePublic(spec)
    val signature = Signature.getInstance(keyAttributes.first, bouncyCastleProvider)
    signature.applyParams(algorithm)
    return Triple(factory, key, signature)
}

private fun signatureGen(size: Int, algo: Algorithm.Signature): Pair<Signature, KeyPair> {
    val signatureKeyPair = attributes(algo)
    val signature: Signature = Signature.getInstance(signatureKeyPair.first, bouncyCastleProvider)
    signature.applyParams(algo)
    val random = SecureRandom()
    val generator = KeyPairGenerator.getInstance(signatureKeyPair.second, bouncyCastleProvider)
    generator.initialize(size, random)
    val key = generator.genKeyPair()
    return signature to key
}

private fun Signature.applyParams(algo: Algorithm.Signature) {
    val params = when(algo) {
        is Algorithm.Signature.RsaPSS -> {
            val sha = "SHA-${algo.hashSize.bits}"
            PSSParameterSpec(sha, "MGF1", MGF1ParameterSpec(sha), algo.saltLength.bytes, 1)
        }
    }
    setParameter(params)
}
