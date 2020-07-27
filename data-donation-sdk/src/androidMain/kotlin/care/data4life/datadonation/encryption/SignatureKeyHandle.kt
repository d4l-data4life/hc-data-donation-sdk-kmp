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

import care.data4life.datadonation.encryption.protos.Keyset
import care.data4life.datadonation.encryption.protos.RsaSsaPrivateKey
import com.google.crypto.tink.*
import com.google.crypto.tink.subtle.Base64
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.protobuf.ProtoBuf

class SignatureKeyHandle<Proto : Asn1Exportable> : SignatureKeyPrivate {
    constructor(keyTemplate: KeyTemplate, deserializer: DeserializationStrategy<Proto>) {
        handle = KeysetHandle.generateNew(keyTemplate)
        this.deserializer = deserializer
    }

    constructor(handle: KeysetHandle, deserializer: DeserializationStrategy<Proto>) {
        this.handle = handle
        this.deserializer = deserializer
    }

    constructor(serializedKeyset: ByteArray, deserializer: DeserializationStrategy<Proto>) {
        handle = CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(serializedKeyset))
        this.deserializer = deserializer
    }

    private val handle: KeysetHandle
    private val deserializer: DeserializationStrategy<Proto>

    override fun sign(data: ByteArray): ByteArray {
        return handle.getPrimitive(PublicKeySign::class.java).sign(data)
    }



    private fun getProto() = ProtoBuf.load(
        Keyset.serializer(),
        CleartextKeysetHandle
            .getKeyset(handle)
            .toByteArray())
        .key.first()
        .key_data.value
        .let { ProtoBuf.load(deserializer, it) }

    override val pkcs1Private: String
        get() = getProto().toAsn1().encoded.asByteArray().let(Base64::encode)
    override val pkcs1Public: String
        get() = getProto().toAsn1().encoded.asByteArray().let(Base64::encode)

    override fun verify(data: ByteArray, signature: ByteArray): Boolean {
        val verifier = handle.publicKeysetHandle.getPrimitive(PublicKeyVerify::class.java)
        return runCatching { verifier.verify(signature, data) }.isSuccess
    }

    override fun serializedPublic():ByteArray =
        CleartextKeysetHandle.getKeyset(handle.publicKeysetHandle).toByteArray()

    override fun serializedPrivate() =
        CleartextKeysetHandle.getKeyset(handle).toByteArray()


}
