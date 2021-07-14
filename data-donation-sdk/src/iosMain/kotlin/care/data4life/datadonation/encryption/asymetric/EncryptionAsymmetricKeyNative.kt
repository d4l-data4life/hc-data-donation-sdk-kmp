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

package care.data4life.datadonation.encryption.asymetric

import care.data4life.datadonation.encryption.KeyNative
import care.data4life.datadonation.encryption.sequence
import care.data4life.sdk.util.NSDataMapper
import platform.CoreFoundation.CFDataRef
import platform.Foundation.*
import platform.Security.*
import platform.posix.*

internal val rsaPkcsIDENTIFIER = listOf(42, 34376, 8845069, 1, 1, 1)

class EncryptionAsymmetricKeyNative :
    KeyNative,
    EncryptionPrivateKey,
    EncryptionPublicKey {

    constructor(keyType: SecKeyAlgorithm, algoType: SecKeyAlgorithm, size: Int) :
        super(keyType, algoType, size)

    constructor(private: SecKeyRef, public: SecKeyRef, algoType: SecKeyAlgorithm) :
        super(private, public, algoType)

    override fun encrypt(data: ByteArray): ByteArray {
        val input = CFBridgingRetain(NSDataMapper.toNSData(data)) as CFDataRef
        val encrypted = SecKeyCreateEncryptedData(publicKey, algoType, input, null)!!
        CFBridgingRelease(input)
        return NSDataMapper.toByteArray(CFBridgingRelease(encrypted) as NSData)
    }

    // TODO: DRY that with SignatureKey
    override fun decrypt(data: ByteArray): Result<ByteArray> = runCatching {
        val input = CFBridgingRetain(NSDataMapper.toNSData(data)) as CFDataRef
        val decrypted = SecKeyCreateDecryptedData(privateKey, algoType, input, null)!!
        CFBridgingRelease(input)
        return@runCatching NSDataMapper.toByteArray(CFBridgingRelease(decrypted) as NSData)
    }

    override fun serializedPublic(): ByteArray {
        return NSDataMapper.toByteArray(
            CFBridgingRelease(
                SecKeyCopyExternalRepresentation(publicKey, null)
            ) as NSData
        )
    }

    override fun serializedPrivate(): ByteArray {
        return NSDataMapper.toByteArray(
            CFBridgingRelease(
                SecKeyCopyExternalRepresentation(privateKey, null)
            ) as NSData
        )
    }

    fun toPkcs8Private(privateKey: ByteArray) =
        (
            "PrivateKeyInfo" sequence {
                "version" integer byteArrayOf(0)
                "algorithm" sequence {
                    "algorithm" object_identifier rsaPkcsIDENTIFIER
                }
                "PrivateKey" octet_string { raw(privateKey) }
            }
            ).encoded.asByteArray()

    fun toPkcs8Public(publicKey: ByteArray) = (
        "PublicKeyInfo" sequence {
            "algorithm" sequence {
                "algorithm" object_identifier rsaPkcsIDENTIFIER
            }
            "PublicKey" bit_string { raw(publicKey) }
        }
        ).encoded.asByteArray()

    private fun encodeWithLineFeed(data: ByteArray): String {
        return NSDataMapper.toNSData(data)
            .base64EncodedStringWithOptions(NSDataBase64EncodingEndLineWithLineFeed)
    }

    override val pkcs8Private: String
        get() {
            return NSDataMapper.toByteArray(
                CFBridgingRelease(
                    SecKeyCopyExternalRepresentation(privateKey, null)
                ) as NSData
            )
                .let(::toPkcs8Private)
                .let(::encodeWithLineFeed)
        }

    override val pkcs8Public: String
        get() {
            return NSDataMapper.toByteArray(
                CFBridgingRelease(
                    SecKeyCopyExternalRepresentation(publicKey, null)
                ) as NSData
            )
                .let(::toPkcs8Public)
                .let(::encodeWithLineFeed)
        }
    // regionend
}
