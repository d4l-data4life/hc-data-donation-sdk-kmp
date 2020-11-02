/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package care.data4life.datadonation.encryption.signature

import care.data4life.datadonation.internal.utils.encodeBase64
import io.ktor.util.*
import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import javax.crypto.Cipher

class SignatureKeyPrivateHandleBouncy(
    private val signature: Signature, private val privateKey: PrivateKey, private val publicKey: PublicKey
) : SignatureKeyPrivate, SignatureKeyPublic by SignatureKeyPublicHandleBouncy(signature,publicKey) {

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
    private val verifier: Signature, private val publicKey: PublicKey
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
