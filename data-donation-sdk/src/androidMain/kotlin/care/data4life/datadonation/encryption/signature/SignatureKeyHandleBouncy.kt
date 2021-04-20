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

package care.data4life.datadonation.encryption.signature

import care.data4life.datadonation.internal.utils.encodeBase64
import io.ktor.util.*
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class SignatureKeyPrivateHandleBouncy(
    private val signature: Signature,
    private val privateKey: PrivateKey,
    private val publicKey: PublicKey
) : SignatureKeyPrivate, SignatureKeyPublic by SignatureKeyPublicHandleBouncy(signature, publicKey) {

    override fun sign(data: ByteArray): ByteArray = with(signature) {
        initSign(privateKey)
        update(data)
        sign()
    }

    override fun serializedPrivate(): ByteArray = privateKey.encoded

    override val pkcs8Private: String
        get() = serializedPrivate().encodeBase64()
}

class SignatureKeyPublicHandleBouncy(
    private val verifier: Signature,
    private val publicKey: PublicKey
) : SignatureKeyPublic {

    override fun verify(data: ByteArray, signature: ByteArray): Boolean = with(verifier) {
        initVerify(publicKey)
        update(data)
        verify(signature)
    }

    override fun serializedPublic(): ByteArray = publicKey.encoded

    override val pkcs8Public: String
        get() = serializedPublic().encodeBase64()
}
