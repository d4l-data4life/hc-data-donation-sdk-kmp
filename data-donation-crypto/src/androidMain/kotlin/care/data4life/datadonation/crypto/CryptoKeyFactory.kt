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

import care.data4life.sdk.crypto.ExchangeKey
import care.data4life.sdk.crypto.GCAsymmetricKey
import care.data4life.sdk.crypto.GCKey
import care.data4life.sdk.crypto.GCKeyPair
import care.data4life.sdk.crypto.GCRSAKeyAlgorithm
import care.data4life.sdk.crypto.KeyOptions
import care.data4life.sdk.crypto.KeyVersion
import care.data4life.sdk.util.Base64
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec

// TODO Merge with CORE and move into the Crypto SDK
internal object CryptoKeyFactory : CryptoKeyFactoryContract {
    private val templateGCKey = D4LCryptoProtocol.generateAsymKeyPair(
        algorithm = GCRSAKeyAlgorithm(),
        options = KeyOptions(
            keySize = KeyVersion.VERSION_1.asymmetricKeySize
        )
    )

    override fun generateSymmetricKey(): GCKey {
        TODO("Not yet implemented")
    }

    override fun generateAsymmetricKeyPair(): GCKeyPair {
        TODO("Not yet implemented")
    }

    override fun createPublicKey(exchangeKey: ExchangeKey): GCKeyPair {
        val algorithm = GCRSAKeyAlgorithm()

        val keyFactory = KeyFactory.getInstance(algorithm.cipher)
        val x509EncodedKeySpec = X509EncodedKeySpec(Base64.decode(exchangeKey.publicKey!!))
        val publicKey = keyFactory.generatePublic(x509EncodedKeySpec)
        val gcPublicKey = GCAsymmetricKey(publicKey, GCAsymmetricKey.Type.Public)

        return GCKeyPair(
            algorithm,
            templateGCKey.privateKey!!,
            gcPublicKey,
            exchangeKey.getVersion().value
        )
    }
}
