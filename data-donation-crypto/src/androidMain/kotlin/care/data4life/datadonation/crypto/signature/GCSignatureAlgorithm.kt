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

import care.data4life.sdk.crypto.Algorithm
import care.data4life.sdk.crypto.GCRSAKeyAlgorithm

internal class GCSignatureAlgorithm private constructor(
    val salt: Salt
) : SignatureAlgorithm() {
    val hash: String

    enum class Hash {
        SHA256
    }

    init {
        cipher = Algorithm.Cipher.RSA.name
        blockMode = SignatureAlgorithm.BlockMode.PSS.name
        this.hash = GCRSAKeyAlgorithm.Hash.SHA256.name
    }

    companion object {
        fun createUnsaltedKey() = GCSignatureAlgorithm(Salt.SALT_0)
        fun createSaltedKey() = GCSignatureAlgorithm(Salt.SALT_32)
    }
}
