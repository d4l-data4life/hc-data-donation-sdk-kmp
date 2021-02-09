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

package care.data4life.datadonation.encryption.assymetric

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.KeyNative
import care.data4life.datadonation.encryption.signature.GeneralEncryptionException
import platform.CoreFoundation.CFStringRef
import platform.Security.SecKeyAlgorithm
import platform.Security.kSecAttrKeyTypeRSA
import platform.Security.kSecKeyAlgorithmRSAEncryptionOAEPSHA256



actual fun EncryptionPublicKey(
    serializedKey: ByteArray,
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPublicKey {
    val key = try {
        KeyNative.buildSecKeyRef(serializedKey, algorithm, KeyNative.KeyType.Public, size)
    } catch (t: GeneralEncryptionException) {
        KeyNative.buildSecKeyRef(serializedKey.let { it.sliceArray(24..it.lastIndex) }, algorithm, KeyNative.KeyType.Public, size)
    }

    return EncryptionAsymmetricKeyNative(
        key,
        key,
        algorithm.toAttributes().second
    )
}


actual fun EncryptionPrivateKey(
    serializedPrivate: ByteArray,
    serializedPublic: ByteArray,
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPrivateKey {

    val keys = try {
        KeyNative.buildSecKeyRef(serializedPrivate, algorithm, KeyNative.KeyType.Private, size) to
                KeyNative.buildSecKeyRef(serializedPublic, algorithm, KeyNative.KeyType.Public, size)
    } catch (t: GeneralEncryptionException) {
        KeyNative.buildSecKeyRef(serializedPrivate.let { it.sliceArray(26..it.lastIndex) }, algorithm, KeyNative.KeyType.Private, size) to
                KeyNative.buildSecKeyRef(serializedPublic.let { it.sliceArray(24..it.lastIndex) }, algorithm, KeyNative.KeyType.Public, size)
    }
    return EncryptionAsymmetricKeyNative(
        keys.first,keys.second,
        algorithm.toAttributes().second
    )
}

private fun Algorithm.Asymmetric.toAttributes(): Pair<CFStringRef, SecKeyAlgorithm> {
    return when (this) {
        is Algorithm.Asymmetric.RsaOAEP -> {
            kSecAttrKeyTypeRSA!! to when (hashSize) {
                HashSize.Hash256 -> kSecKeyAlgorithmRSAEncryptionOAEPSHA256!!
            }
        }
    }
}


actual fun EncryptionPrivateKey(
    size: Int,
    algorithm: Algorithm.Asymmetric
): EncryptionPrivateKey {
    val params: Pair<CFStringRef, SecKeyAlgorithm> = algorithm.toAttributes()
    return EncryptionAsymmetricKeyNative(params.first, params.second, size)
}
