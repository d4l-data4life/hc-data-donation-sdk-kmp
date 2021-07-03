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

import care.data4life.datadonation.encryption.KeyNative
import care.data4life.datadonation.encryption.asymetric.rsaPkcsIDENTIFIER
import care.data4life.datadonation.encryption.sequence
import care.data4life.datadonation.toNSData
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Security.*
import platform.darwin.noErr
import platform.posix.*

class SignatureKeyNative : KeyNative, SignatureKeyPrivate {

    constructor(keyType: SecKeyAlgorithm, algoType: SecKeyAlgorithm, size: Int) :
        super(keyType, algoType, size)

    constructor(private: SecKeyRef, public: SecKeyRef, algoType: SecKeyAlgorithm) :
        super(private, public, algoType)

    override fun sign(data: ByteArray): ByteArray {
        val inputCfDataRef = CFBridgingRetain(data.toNSData()) as CFDataRef
        val k = SecKeyCreateSignature(privateKey, algoType, inputCfDataRef, null)!!
        CFBridgingRelease(inputCfDataRef)
        return (CFBridgingRelease(k) as Payload).toByteArray()
    }

    override val pkcs8Private: String
        get() = (CFBridgingRelease(SecKeyCopyExternalRepresentation(privateKey, null)) as Payload)
            .toByteArray()
            .let(::toPkcs8Private)
            .toNSData()
            .base64EncodedStringWithOptions(NSDataBase64EncodingEndLineWithLineFeed)

    override val pkcs8Public: String
        get() = (CFBridgingRelease(SecKeyCopyExternalRepresentation(publicKey, null)) as Payload)
            .toByteArray()
            .let(::toPkcs8Public)
            .toNSData()
            .base64EncodedStringWithOptions(NSDataBase64EncodingEndLineWithLineFeed)

    override fun verify(data: ByteArray, signature: ByteArray): Boolean =
        memScoped {
            val error = alloc<CFErrorRefVar>()
            val inputCfDataRef = CFBridgingRetain(data.toNSData()) as CFDataRef
            val signatureCfDataRef = CFBridgingRetain(signature.toNSData()) as CFDataRef
            val k = SecKeyVerifySignature(publicKey, algoType, inputCfDataRef, signatureCfDataRef, error.ptr)
            if (error.value != null) {
                val err = CFBridgingRelease(error.value) as NSError
                throw Throwable(err.localizedDescription)
            }
            return@memScoped k
        }

    override fun serializedPublic(): ByteArray =
        (CFBridgingRelease(SecKeyCopyExternalRepresentation(publicKey, null)) as Payload)
            .toByteArray()

    override fun serializedPrivate(): ByteArray =
        (CFBridgingRelease(SecKeyCopyExternalRepresentation(privateKey, null)) as Payload)
            .toByteArray()

    fun toPkcs8Private(privateKey: ByteArray) =
        (
            "PrivateKeyInfo" sequence {
                "version" integer byteArrayOf(0)
                "algorithm" sequence {
                    "algorithm" object_identifier rsaPkcsIDENTIFIER
                }
                "PrivateKey" octet_string { raw(privateKey) }
            }
            ).encoded

    fun toPkcs8Public(publicKey: ByteArray) = (
        "PublicKeyInfo" sequence {
            "algorithm" sequence {
                "algorithm" object_identifier rsaPkcsIDENTIFIER
            }
            "PublicKey" bit_string { raw(publicKey) }
        }
        ).encoded
}

fun generateKey(type: SecKeyAlgorithm, size: Int): Pair<SecKeyRef, SecKeyRef> {
    memScoped {
        val publicKeyAttr = CFDictionaryCreateMutable(kCFAllocatorSystemDefault, 3, null, null)!!
        publicKeyAttr += kSecAttrIsPermanent to CFBridgingRetain(NSNumber(bool = false))
        publicKeyAttr += kSecAttrApplicationTag to CFBridgingRetain(
            NSData.dataWithBytes(
                bytes = "com.foo.public".cstr.ptr,
                length = 15u
            )
        )
        publicKeyAttr += kSecAttrAccessible to CFBridgingRetain(NSNumber(bool = true))

        val privateKeyAttr = CFDictionaryCreateMutable(kCFAllocatorSystemDefault, 3, null, null)!!
        privateKeyAttr += kSecAttrIsPermanent to CFBridgingRetain(NSNumber(bool = false))
        privateKeyAttr += kSecAttrApplicationTag to CFBridgingRetain(
            NSData.dataWithBytes(
                bytes = "com.foo.private".cstr.ptr,
                length = 16u
            )
        )
        privateKeyAttr += kSecAttrAccessible to CFBridgingRetain(NSNumber(bool = true))

        val keyPairAttr = CFDictionaryCreateMutable(kCFAllocatorSystemDefault, 5, null, null)!!
        keyPairAttr += kSecAttrKeyType to type
        keyPairAttr += kSecAttrKeySizeInBits to CFBridgingRetain(NSNumber(int = size))
        keyPairAttr += kSecPublicKeyAttrs to publicKeyAttr
        keyPairAttr += kSecPrivateKeyAttrs to privateKeyAttr
        keyPairAttr += kSecAttrAccessible to CFBridgingRetain(NSNumber(bool = true))

        val publicKey = alloc<SecKeyRefVar>()
        val privateKey = alloc<SecKeyRefVar>()
        val statusCode = SecKeyGeneratePair(keyPairAttr, publicKey.ptr, privateKey.ptr)

        CFBridgingRelease(publicKeyAttr)
        CFBridgingRelease(privateKeyAttr)
        CFBridgingRelease(keyPairAttr)

        if (statusCode == noErr.toInt() && publicKey.value != null && privateKey.value != null) {
            return privateKey.value!! to publicKey.value!!
        } else {
            throw GeneralEncryptionException("Cannot generate keypair: $statusCode")
        }
    }
}

inline operator fun CFMutableDictionaryRef.plusAssign(pair: Pair<CValuesRef<*>?, CValuesRef<*>?>) {
    CFDictionaryAddValue(this, pair.first, pair.second)
}

class GeneralEncryptionException(override val message: String?) : Throwable()
