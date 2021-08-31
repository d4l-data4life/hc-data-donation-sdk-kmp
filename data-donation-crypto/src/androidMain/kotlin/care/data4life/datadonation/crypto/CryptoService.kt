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

package care.data4life.datadonation.crypto

import care.data4life.datadonation.crypto.CryptoServiceContract.Companion.IV_SIZE
import care.data4life.datadonation.crypto.CryptoServiceContract.Companion.PROTOCOL_VERSION
import care.data4life.datadonation.crypto.model.KeyPair
import care.data4life.datadonation.crypto.signature.GCSignatureAlgorithm
import care.data4life.datadonation.crypto.signature.GCSignatureKeyPair
import care.data4life.datadonation.crypto.signature.SignatureAlgorithm
import care.data4life.sdk.crypto.ExchangeKey
import care.data4life.sdk.crypto.ExchangeKeyFactory
import care.data4life.sdk.crypto.GCKeyPair
import care.data4life.sdk.crypto.KeyType
import care.data4life.sdk.crypto.KeyVersion
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom

internal actual class CryptoService actual constructor() : CryptoServiceContract {
    private val secureRandom = SecureRandom()

    actual override fun createKeyPair(): KeyPair {
        val keyPair = try {
            CryptoKeyFactory.generateAsymmetricKeyPair()
        } catch (_: Throwable) {
            throw CryptoError.MalFormedKeyGeneration()
        }

        return KeyPair(
            publicKey = keyPair.publicKey!!.value.encoded,
            privateKey = keyPair.privateKey!!.value.encoded
        )
    }

    private fun concatenateCryptoMaterial(
        encryptedKey: ByteArray,
        iv: ByteArray,
        ciphertext: ByteArray
    ): ByteArray {
        val hybridLength = 1 + 2 + encryptedKey.size + IV_SIZE + 8 + ciphertext.size
        val buffer = ByteBuffer.wrap(ByteArray(hybridLength))

        buffer.put(PROTOCOL_VERSION.toByte())

        buffer.order(ByteOrder.LITTLE_ENDIAN).putShort(encryptedKey.size.toShort())

        buffer.order(ByteOrder.LITTLE_ENDIAN).put(encryptedKey)

        buffer.order(ByteOrder.LITTLE_ENDIAN).put(iv)

        buffer.order(ByteOrder.LITTLE_ENDIAN).putLong(ciphertext.size.toLong())
        buffer.order(ByteOrder.LITTLE_ENDIAN).put(ciphertext)

        return buffer.array()
    }

    private fun encryptInternal(
        payload: ByteArray,
        publicKey: String
    ): ByteArray {
        val symmetricKey = CryptoKeyFactory.generateSymmetricKey()
        val iv = ByteArray(IV_SIZE)
        secureRandom.nextBytes(iv)

        val cipherText = D4LCryptoProtocol.symEncrypt(
            symmetricKey,
            payload,
            iv
        )

        val exchangeKey = ExchangeKeyFactory.createKey(
            KeyVersion.VERSION_1,
            KeyType.APP_PUBLIC_KEY,
            publicKey
        )

        val asymKey = CryptoKeyFactory.createPublicKey(exchangeKey)

        val encryptedKey = D4LCryptoProtocol.asymEncrypt(
            asymKey,
            symmetricKey.getSymmetricKey().value.encoded
        )

        return concatenateCryptoMaterial(
            encryptedKey,
            iv,
            cipherText
        )
    }

    actual override fun encrypt(
        payload: ByteArray,
        publicKey: String
    ): ByteArray {
        return try {
            encryptInternal(payload, publicKey)
        } catch (_: Throwable) {
            throw CryptoError.IllEncryption()
        }
    }

    private fun resolveSignatureAlgorithm(saltLength: Int): GCSignatureAlgorithm {
        return when (saltLength) {
            SignatureAlgorithm.Salt.SALT_0.length -> GCSignatureAlgorithm.createUnsaltedKey()
            SignatureAlgorithm.Salt.SALT_32.length -> GCSignatureAlgorithm.createSaltedKey()
            else -> throw CryptoError.UnknownSalt(saltLength)
        }
    }

    private fun resolvePublicKey(key: String): GCKeyPair {
        val asymExchangeKey = ExchangeKey(
            type = KeyType.APP_PRIVATE_KEY,
            privateKey = key,
            publicKey = null,
            symmetricKey = null,
            version = KeyVersion.VERSION_1
        )

        return CryptoKeyFactory.createPrivateKey(asymExchangeKey)
    }

    private fun signInternal(
        payload: ByteArray,
        privateKey: String,
        saltLength: Int,
    ): ByteArray {
        val signer = GCSignatureKeyPair.fromGCKeyPair(
            algorithm = resolveSignatureAlgorithm(saltLength),
            keyPair = resolvePublicKey(privateKey)
        ).toSigningSignature()

        signer.update(payload)
        return signer.sign()
    }

    actual override fun sign(
        payload: ByteArray,
        privateKey: String,
        saltLength: Int,
    ): ByteArray {
        return try {
            signInternal(payload, privateKey, saltLength)
        } catch (_: Throwable) {
            throw CryptoError.IllSigning()
        }
    }
}
