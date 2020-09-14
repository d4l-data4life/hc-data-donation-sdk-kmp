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

import care.data4life.datadonation.encryption.signature.generateKey
import care.data4life.datadonation.encryption.signature.plusAssign
import care.data4life.datadonation.toNSData
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.kCFAllocatorSystemDefault
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSNumber
import platform.Security.*

abstract class KeyNative {
    protected val privateKey: SecKeyRef
    protected val publicKey: SecKeyRef
    protected val algoType: SecKeyAlgorithm

    constructor(keyType: SecKeyAlgorithm, algoType: SecKeyAlgorithm, size: Int) {
        val (private, public) = generateKey(keyType, size)
        privateKey = private
        publicKey = public
        this.algoType = algoType
    }

    constructor(private: SecKeyRef, public: SecKeyRef, algoType: SecKeyAlgorithm) {
        privateKey = private
        publicKey = public
        this.algoType = algoType
    }

    companion object {
        fun buildSecKeyRef(serialized: ByteArray, algorithm: Algorithm, type: KeyType): SecKeyRef {
            val data = CFBridgingRetain(serialized.toNSData()) as CFDataRef
            val pubAttr = CFDictionaryCreateMutable(kCFAllocatorSystemDefault, 3, null, null)!!
            val exhaustive = when (algorithm) {
                is Algorithm.Signature.RsaPSS -> {
                    pubAttr += kSecAttrKeyType to kSecAttrKeyTypeRSA
                    pubAttr += kSecAttrKeyClass to type.nativeType
                    pubAttr += kSecAttrKeySizeInBits to CFBridgingRetain(NSNumber(int = algorithm.hashSize.bits))
                }
                is Algorithm.Asymmetric.RsaOAEP -> {
                    pubAttr += kSecAttrKeyType to kSecAttrKeyTypeRSA!!
                    pubAttr += kSecAttrKeyClass to type.nativeType
                    pubAttr += kSecAttrKeySizeInBits to CFBridgingRetain(NSNumber(int = algorithm.hashSize.bits))
                }
                is Algorithm.Symmetric.AES -> throw NotImplementedError("No native AES support is available")
            }
            return SecKeyCreateWithData(data, pubAttr, null)!!
        }
    }

    enum class KeyType(val nativeType: CFStringRef) {
        Private(kSecAttrKeyClassPrivate!!),Public(kSecAttrKeyClassPublic!!),Symmetric(kSecAttrKeyClassSymmetric!!)
    }

}
