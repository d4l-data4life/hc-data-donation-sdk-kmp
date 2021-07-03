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

package care.data4life.datadonation.encryption.asymetric

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
    return EncryptionKeyPublicHandleBouncy(cipher, key)
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
    return EncryptionKeyPrivateHandleBouncy(cipher, privateKey, publicKey)
}

actual fun EncryptionPrivateKey(
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPrivateKey {

    return when (algorithm) {
        is Algorithm.Asymmetric.RsaOAEP -> {
            val (cipher, keyPair) = cipherGen(size, algorithm)
            EncryptionKeyPrivateHandleBouncy(cipher, keyPair.private, keyPair.public)
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
