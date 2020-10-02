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

package care.data4life.datadonation.encryption.symmetric

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.protos.Aes
import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.proto.AesGcmKey
import com.google.crypto.tink.proto.AesGcmKeyFormat


actual fun EncryptionSymmetricKey(size: Int, algorithm: Algorithm.Symmetric): EncryptionSymmetricKey {
    val format = AesGcmKeyFormat.newBuilder().setKeySize(size/8).build()
    val tmpl = KeyTemplate.create(
        "type.googleapis.com/google.crypto.tink.AesGcmKey",
        format.toByteArray(),
        KeyTemplate.OutputPrefixType.RAW
    )
    return when (algorithm) {
        is Algorithm.Symmetric.AES -> EncryptionKeySymmetricHandle(tmpl,Aes.serializer())
    }
}


actual fun EncryptionSymmetricKey(
    serializedKey: ByteArray,
    size: Int,
    algorithm: Algorithm.Symmetric
): EncryptionSymmetricKey {
    CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(serializedKey))
    val serializer = when (algorithm) {
        is Algorithm.Symmetric.AES -> Aes.serializer()
    }
    return EncryptionKeySymmetricHandle(serializedKey, serializer)
}
