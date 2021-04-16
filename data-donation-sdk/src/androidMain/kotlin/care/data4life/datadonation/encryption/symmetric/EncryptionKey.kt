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

package care.data4life.datadonation.encryption.symmetric

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.assymetric.bouncyCastleProvider
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
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
