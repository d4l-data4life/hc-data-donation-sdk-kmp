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

import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class EncryptionSymmetricKeyHandleBouncy(
    private val cipher: Cipher,
    private val key: Key
) : EncryptionSymmetricKey {

    private val random = SecureRandom()

    override fun decrypt(encrypted: ByteArray, associatedData: ByteArray): Result<ByteArray> {
        val iv = encrypted.sliceArray(0..15)
        val encrypted = encrypted.sliceArray(16..encrypted.lastIndex)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        cipher.updateAAD(associatedData)
        return runCatching { cipher.doFinal(encrypted) }
    }

    override fun encrypt(plainText: ByteArray, associatedData: ByteArray): ByteArray {
        val iv = ByteArray(16)
        random.nextBytes(iv)
        val specParam = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, specParam, random)
        cipher.updateAAD(associatedData)
        return iv + cipher.doFinal(plainText)
    }

    override fun serialized(): ByteArray = key.encoded

    @OptIn(InternalAPI::class)
    override val pkcs8: String
        get() = serialized().encodeBase64()
}
