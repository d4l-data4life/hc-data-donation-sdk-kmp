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
