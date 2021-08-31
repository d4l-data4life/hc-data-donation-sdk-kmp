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
import java.security.spec.MGF1ParameterSpec

internal open class SignatureAlgorithm(
    val schema: Schema,
    val hash: Hash,
    val mask: Mask,
    val generator: GeneratorFunction
) : Algorithm() {

    enum class Salt(val length: Int) {
        SALT_0(0),
        SALT_32(32)
    }

    enum class Hash(val value: String) {
        SHA256("SHA-256")
    }

    enum class Mask(val value: String) {
        MGF1("MGF1")
    }

    enum class GeneratorFunction(val spec: AlgorithmParameterSpec) {
        SHA256_MGF1(MGF1ParameterSpec.SHA256)
    }

    enum class Schema(name: String) {
        PSS("PSS")
    }
}
