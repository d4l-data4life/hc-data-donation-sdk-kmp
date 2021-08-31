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

import care.data4life.sdk.crypto.GCAESKeyAlgorithm
import care.data4life.sdk.crypto.GCKey
import care.data4life.sdk.crypto.GCSymmetricKey
import care.data4life.sdk.crypto.KeyVersion
import javax.crypto.spec.SecretKeySpec

// TODO Merge with CORE and move into the Crypto SDK
internal object CryptoVerificationKeyFactory {
    // see: https://github.com/d4l-data4life/hc-sdk-kmp/blob/c03e078247312eb0319658f4399e2e39de6882fd/sdk-core/src/main/java/care/data4life/sdk/crypto/KeyFactory.kt#L28
    fun createGCKey(key: ByteArray, version: KeyVersion): GCKey {
        val algorithm = GCAESKeyAlgorithm.createDataAlgorithm()

        val symmetricKey = GCSymmetricKey(
            SecretKeySpec(
                key,
                algorithm.transformation
            )
        )

        return GCKey(algorithm, symmetricKey, version.symmetricKeySize)
    }
}
