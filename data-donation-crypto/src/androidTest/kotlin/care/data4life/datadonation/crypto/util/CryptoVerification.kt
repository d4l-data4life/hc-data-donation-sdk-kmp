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

package care.data4life.datadonation.crypto.util

import care.data4life.datadonation.crypto.CryptoServiceContract.Companion.PROTOCOL_VERSION
import care.data4life.datadonation.crypto.D4LCryptoProtocol
import care.data4life.datadonation.crypto.signature.GCSignatureKeyPair
import care.data4life.sdk.crypto.ExchangeKey
import care.data4life.sdk.crypto.GCKey
import care.data4life.sdk.crypto.GCKeyPair
import care.data4life.sdk.crypto.KeyType
import care.data4life.sdk.crypto.KeyVersion
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.PublicKey
import java.security.Signature
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PSSParameterSpec

private data class CryptoMaterial(
    val encryptedSymmetricKey: ByteArray,
    val iv: ByteArray,
    val encryptedText: ByteArray
)

actual object CryptoVerification {
    private fun validateVersion(version: Int) {
        if (version != PROTOCOL_VERSION) {
            throw RuntimeException("Unsupported Protocol Version $version.")
        }
    }

    private fun resolveCrypotMaterial(payload: ByteArray): CryptoMaterial {
        val buffer = ByteBuffer.wrap(payload)

        validateVersion(buffer.get().toInt())

        val encryptedKeyLength = buffer.order(ByteOrder.LITTLE_ENDIAN).short.toInt()

        val encryptedSymKey = ByteArray(encryptedKeyLength)
        buffer.order(ByteOrder.LITTLE_ENDIAN).get(encryptedSymKey)

        val iv = ByteArray(16)
        buffer.order(ByteOrder.LITTLE_ENDIAN).get(iv)

        val cipherTextLength = buffer.order(ByteOrder.LITTLE_ENDIAN).long.toInt()
        val cipherText = ByteArray(cipherTextLength)
        buffer.order(ByteOrder.LITTLE_ENDIAN).get(cipherText)

        return CryptoMaterial(
            encryptedSymmetricKey = encryptedSymKey,
            iv = iv,
            encryptedText = cipherText
        )
    }

    private fun resolveAsymmetricKey(key: String): GCKeyPair {
        return CryptoVerificationKeyFactory.createPrivateKey(
            ExchangeKey(
                type = KeyType.APP_PRIVATE_KEY,
                privateKey = key,
                publicKey = null,
                symmetricKey = null,
                version = KeyVersion.VERSION_1
            )
        )
    }

    private fun resolveSymmetricKey(
        encryptedKey: ByteArray,
        keyPair: GCKeyPair
    ): GCKey {
        val symKey = D4LCryptoProtocol.asymDecrypt(
            keyPair,
            encryptedKey
        )

        return CryptoVerificationKeyFactory.createGCKey(
            symKey,
            KeyVersion.VERSION_1
        )
    }

    actual fun decrypt(
        payload: ByteArray,
        key: String
    ): ByteArray {

        val (encryptedSymKey, iv, encryptedText) = resolveCrypotMaterial(payload)
        val symmetricKey = resolveSymmetricKey(
            encryptedSymKey,
            resolveAsymmetricKey(key)
        )

        return D4LCryptoProtocol.symDecrypt(
            symmetricKey,
            encryptedText,
            iv
        )
    }

    private fun resolvePublicKey(key: String): GCSignatureKeyPair {
        val asymExchangeKey = ExchangeKey(
            type = KeyType.APP_PUBLIC_KEY,
            privateKey = null,
            publicKey = key,
            symmetricKey = null,
            version = KeyVersion.VERSION_1
        )

        return CryptoVerificationKeyFactory.createPublicSignatureKey(
            asymExchangeKey,
            32
        )
    }

    actual fun verify(
        payload: ByteArray,
        signature: ByteArray,
        key: String,
        saltLength: Int,
    ): Boolean {
        val asymKeyPair = resolvePublicKey(key)
        val signer = Signature.getInstance("SHA256withRSA/PSS")
        signer.setParameter(
            PSSParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                0,
                1
            )
        )
        signer.initVerify(asymKeyPair.publicKey!!.value as PublicKey)
        signer.update(payload)
        return signer.verify(signature)
    }
}
