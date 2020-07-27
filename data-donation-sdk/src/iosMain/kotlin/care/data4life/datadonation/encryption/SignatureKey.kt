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

import care.data4life.datadonation.toNSData
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.kCFAllocatorSystemDefault
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSNumber
import platform.Security.*

actual fun SignatureKeyPrivate(size: Int, algorithm: Algorithm): SignatureKeyPrivate {
    val params: Pair<CFStringRef, SecKeyAlgorithm> = algorithm.toAttributes()
    return SignatureKeyNative(params.first, params.second, size)
}

private fun Algorithm.toAttributes(): Pair<CFStringRef, SecKeyAlgorithm> {
    return when (this) {
        is Algorithm.RsaPSS -> {
            kSecAttrKeyTypeRSA!! to when (hashSize) {
                HashSize.Hash256 -> kSecKeyAlgorithmRSASignatureDigestPSSSHA256!!
            }
        }
    }
}

actual fun SignatureKeyPrivate(
    serializedPrivate: ByteArray,
    serializedPublic: ByteArray,
    size: Int,
    algorithm: Algorithm
): SignatureKeyPrivate {


    return SignatureKeyNative(
        SecKeyRef(serializedPrivate, algorithm, true),
        SecKeyRef(serializedPublic, algorithm, false),
        algorithm.toAttributes().first
    )
}

actual fun SignatureKeyPublic(
    serialized: ByteArray,
    size: Int,
    algorithm: Algorithm
): SignatureKeyPublic {
    TODO()
}

private fun SecKeyRef(serialized: ByteArray, algorithm: Algorithm, private: Boolean): SecKeyRef {
    val data = CFBridgingRetain(serialized.toNSData()) as CFDataRef
    val pubAttr = CFDictionaryCreateMutable(kCFAllocatorSystemDefault, 3, null, null)!!
    when (algorithm) {
        is Algorithm.RsaPSS -> {
            pubAttr += kSecAttrKeyType to kSecAttrKeyTypeRSA
            pubAttr += kSecAttrKeyClass to if (private) kSecAttrKeyClassPrivate else kSecAttrKeyClassPublic
            pubAttr += kSecAttrKeySizeInBits to CFBridgingRetain(NSNumber(int = algorithm.hashSize.bits))
        }
    }.let {}
    return SecKeyCreateWithData(data, pubAttr, null)!!
}
