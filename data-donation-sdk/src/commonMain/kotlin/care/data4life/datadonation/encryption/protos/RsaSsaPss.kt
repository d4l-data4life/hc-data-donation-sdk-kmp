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

package care.data4life.datadonation.encryption.protos

import care.data4life.datadonation.encryption.Asn1Exportable
import care.data4life.datadonation.encryption.sequence
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class RsaSsaPssParams(
    // Hash function used in computing hash of the signing message
    // (see https://tools.ietf.org/html/rfc8017#section-9.1.1).
    // Required.
    @ProtoId(1)
    val sigHash: HashType,
    // Hash function used in MGF1 (a mask generation function based on a
    // hash function) (see https://tools.ietf.org/html/rfc8017#appendix-B.2.1).
    // Required.
    @ProtoId(2)
    val mgf1Hash: HashType,
    // Salt length (see https://tools.ietf.org/html/rfc8017#section-9.1.1)
    // Required.
    @ProtoId(3)
    val saltLength: Int
)

@Serializable
class RsaSsaPssPublicKey(
    // Required.
    @ProtoId(1)
    val version: Int = 0,
    // Required.
    @ProtoId(2)
    val params: RsaSsaPssParams,
    // Modulus.
    // Unsigned big integer in bigendian representation.
    @ProtoId(3)
    val n: ByteArray,
    // Public exponent.
    // Unsigned big integer in bigendian representation.
    @ProtoId(4)
    val e: ByteArray
):Asn1Exportable {
    override fun toAsn1() = "RSAPublicKey" sequence {
        "modulus" integer n
        "publicExponent" integer e
    }
}

@Serializable
class RsaSsaPrivateKey(
    // Required.
    @ProtoId(1)
    val version: Int = 0,
    // Required.
    @ProtoId(2)
    val publicKey: RsaSsaPssPublicKey,
    // Private exponent.
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(3)
    val d: ByteArray,
    // The following parameters are used to optimize RSA signature computation.
    // The prime factor p of n.
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(4)
    val p: ByteArray,
    // The prime factor q of n.
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(5)
    val q: ByteArray,
    // d mod (p - 1).
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(6)
    val dp: ByteArray,
    // d mod (q - 1).
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(7)
    val dq: ByteArray,
    // Chinese Remainder Theorem coefficient q^(-1) mod p.
    // Unsigned big integer in bigendian representation.
    // Required.
    @ProtoId(8)
    val crt: ByteArray
):Asn1Exportable {
    override fun toAsn1() =
        "RSAPrivateKey" sequence {
            "version" integer byteArrayOf(0)
            "modulus" integer publicKey.n
            "publicExponent" integer publicKey.e
            "privateExponent" integer d
            "prime1" integer p
            "prime2" integer q
            "exponent1" integer dp
            "exponent2" integer dq
            "crtCoefficient" integer crt
        }
}

@Serializable
enum class HashType() {
    @ProtoId(0)
    UNKNOWN_HASH,

    @ProtoId(1)
    SHA1,  // Using SHA1 for digital signature is deprecated but HMAC-SHA1 is

    // fine.
    @ProtoId(2)
    SHA384,

    @ProtoId(3)
    SHA256,

    @ProtoId(4)
    SHA512
}
