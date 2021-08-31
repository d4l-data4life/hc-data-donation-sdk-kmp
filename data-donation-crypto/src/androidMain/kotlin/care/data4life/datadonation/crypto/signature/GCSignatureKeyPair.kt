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

package care.data4life.datadonation.crypto.signature

import care.data4life.sdk.crypto.GCAsymmetricKey
import care.data4life.sdk.crypto.GCKeyPair
import care.data4life.sdk.crypto.GCRSAKeyAlgorithm
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature

internal class GCSignatureKeyPair constructor(
    val algorithm: GCSignatureAlgorithm,
    val privateKey: GCAsymmetricKey,
    val publicKey: GCAsymmetricKey,
    val keyVersion: Int
) {
    private val random = SecureRandom()

    fun toGCKeyPair(algorithm: GCRSAKeyAlgorithm): GCKeyPair {
        return GCKeyPair(
            algorithm,
            privateKey,
            publicKey,
            keyVersion
        )
    }

    private fun prepareSignature(): Signature {
        return Signature.getInstance(algorithm.transformation).also {
            it.setParameter(algorithm.spec)
        }
    }

    fun toSigningSignature(): Signature {
        return prepareSignature().also {
            it.initSign(
                privateKey.value as PrivateKey,
                random
            )
        }
    }

    fun toVerificationSignature(): Signature {
        return prepareSignature().also {
            it.initVerify(publicKey.value as PublicKey)
        }
    }

    companion object {
        fun fromGCKeyPair(
            keyPair: GCKeyPair,
            algorithm: GCSignatureAlgorithm
        ): GCSignatureKeyPair {
            return GCSignatureKeyPair(
                algorithm = algorithm,
                privateKey = keyPair.privateKey!!,
                publicKey = keyPair.publicKey!!,
                keyVersion = keyPair.keyVersion
            )
        }
    }
}
