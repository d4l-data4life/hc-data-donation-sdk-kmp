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
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.PSSParameterSpec

internal class GCSignatureAlgorithm private constructor(
    schema: Schema = Schema.PSS,
    hash: Hash = Hash.SHA256,
    mask: Mask = Mask.MGF1,
    generatorFunction: GeneratorFunction = GeneratorFunction.SHA256_MGF1,
    val salt: Salt,
) : SignatureAlgorithm(
    schema,
    hash,
    mask,
    generatorFunction
) {
    init {
        cipher = Algorithm.Cipher.RSA.name
    }

    val spec: AlgorithmParameterSpec
        get() {
            return PSSParameterSpec(
                this.hash.value,
                this.mask.name,
                this.generator.spec,
                this.salt.length,
                1
            )
        }

    override val transformation: String
        get() = "${Hash.SHA256.name}with$cipher/${schema.name}"

    companion object {
        fun createUnsaltedKey() = GCSignatureAlgorithm(salt = Salt.SALT_0)
        fun createSaltedKey() = GCSignatureAlgorithm(salt = Salt.SALT_32)
    }
}
