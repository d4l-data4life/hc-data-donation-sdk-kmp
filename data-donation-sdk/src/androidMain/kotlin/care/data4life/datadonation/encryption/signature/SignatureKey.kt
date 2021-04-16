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
            PSSParameterSpec(sha, "MGF1", MGF1ParameterSpec(sha), 32, 1)
        }
    }
    setParameter(params)
}
