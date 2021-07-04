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

package care.data4life.datadonation.encryption

import care.data4life.datadonation.encryption.asymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.asymetric.EncryptionPublicKey
import care.data4life.datadonation.encryption.symmetric.EncryptionSymmetricKey

interface EncryptionContract {
    interface HybridEncryptionRegistry {
        val hybridEncryptionDD: HybridEncryption
        val hybridEncryptionALP: HybridEncryption
    }

    interface HybridEncryption {
        fun encrypt(plaintext: ByteArray): ByteArray
        fun decrypt(ciphertext: ByteArray): Result<ByteArray>
    }

    interface SymmetricKeyProvider {
        fun getNewKey(): EncryptionSymmetricKey
        fun getKey(keyData: ByteArray): EncryptionSymmetricKey
        fun getAuthenticationData(): ByteArray
    }

    interface AsymmetricKeyProvider {
        fun getPublicKey(): EncryptionPublicKey
        fun getPrivateKey(): EncryptionPrivateKey
    }

    companion object {
        const val HYBRID_ENCRYPTION_VERSION_AES_WITH_GCM = 2
        const val AES_IV_LENGTH = 16
        const val AES_AUTH_TAG_LENGTH = 16
        const val AES_KEY_LENGTH = 256
        const val RSA_KEY_SIZE_BITS = 2048
    }
}
