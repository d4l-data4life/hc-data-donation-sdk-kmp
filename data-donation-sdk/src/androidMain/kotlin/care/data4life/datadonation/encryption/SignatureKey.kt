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

package care.data4life.datadonation.encryption


import com.google.crypto.tink.*
import com.google.crypto.tink.proto.HashType
import com.google.crypto.tink.proto.RsaSsaPssKeyFormat
import com.google.crypto.tink.proto.RsaSsaPssParams
import care.data4life.datadonation.encryption.protos.RsaSsaPrivateKey
import care.data4life.datadonation.encryption.protos.RsaSsaPssPublicKey
import com.google.crypto.tink.shaded.protobuf.ByteString
import java.security.spec.RSAKeyGenParameterSpec


actual fun SignatureKeyPrivate(size: Int,algorithm: Algorithm.Signature): SignatureKeyPrivate {
    return when(algorithm) {
        is Algorithm.Signature.RsaPSS -> SignatureKeyPrivateHandle(KeysetHandle(size, algorithm),RsaSsaPrivateKey.serializer())
    }
}

private fun KeysetHandle(size: Int,algorithm: Algorithm.Signature): KeysetHandle {
    return when(algorithm) {
        is Algorithm.Signature.RsaPSS -> {
            val hash = when(algorithm.hashSize) {
                HashSize.Hash256 -> HashType.SHA256
            }
            RsaSsaPssParams.newBuilder()
                .setSigHash(hash)
                .setMgf1Hash(HashType.SHA256)
                .setSaltLength(32)
                .build()
                .let { params ->
                    RsaSsaPssKeyFormat.newBuilder()
                        .setParams(params)
                        .setModulusSizeInBits(size)
                        .setPublicExponent(ByteString.copyFrom(RSAKeyGenParameterSpec.F4.toByteArray()))
                        .build()
                }
                .let { format ->
                    KeyTemplate.create(
                        "type.googleapis.com/google.crypto.tink.RsaSsaPssPrivateKey",
                        format.toByteArray(),
                        KeyTemplate.OutputPrefixType.RAW
                    )
                }

        }
    }.let { template ->
        KeysetHandle.generateNew(template)
    }
}

actual fun SignatureKeyPrivate(
    serializedPrivate: ByteArray,
    //this one isn't actually used because public is calculated from private
    serializedPublic: ByteArray,
    size: Int,
    algorithm: Algorithm.Signature
): SignatureKeyPrivate {
    CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(serializedPrivate))
    val serializer = when(algorithm) {
        is Algorithm.Signature.RsaPSS -> RsaSsaPrivateKey.serializer()
    }
    return SignatureKeyPrivateHandle(serializedPrivate,serializer)
}

actual fun SignatureKeyPublic(
    serialized: ByteArray,
    size: Int,
    algorithm: Algorithm.Signature
): SignatureKeyPublic {
    CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(serialized))
    val deserializer = when(algorithm) {
        is Algorithm.Signature.RsaPSS -> RsaSsaPssPublicKey.serializer()
    }
    return SignatureKeyPublicHandle(serialized,deserializer)
}
