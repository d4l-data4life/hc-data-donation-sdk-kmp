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
import care.data4life.datadonation.encryption.protos.PublicHandle
import com.google.crypto.tink.BinaryKeysetReader
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.subtle.Base64
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.protobuf.ProtoBuf

abstract class KeyHandleTink<Proto> constructor(
    protected val handle: KeysetHandle, protected val deserializer: DeserializationStrategy<Proto>
) where Proto : Asn1Exportable, Proto : PublicHandle {

    constructor(keyTemplate: KeyTemplate, deserializer: DeserializationStrategy<Proto>) :
            this(KeysetHandle.generateNew(keyTemplate), deserializer)

    constructor(serializedKeyset: ByteArray, deserializer: DeserializationStrategy<Proto>) :
            this(CleartextKeysetHandle.read(BinaryKeysetReader.withBytes(serializedKeyset)), deserializer)

    private fun getProto() = ProtoBuf.load(
        Keyset.serializer(),
        CleartextKeysetHandle
            .getKeyset(handle)
            .toByteArray()
    ).key.first()
        .key_data.value
        .let { ProtoBuf.load(deserializer, it) }

    protected fun deserializePrivate(): String = getProto().toAsn1().encoded.asByteArray().let(Base64::encode)


    protected fun deserializePublic(): String = getProto().publicKey.toAsn1().encoded.asByteArray().let(Base64::encode)

    protected fun serializePublic(): ByteArray =
        CleartextKeysetHandle.getKeyset(handle.publicKeysetHandle).toByteArray()

    protected fun serializePrivate() =
        CleartextKeysetHandle.getKeyset(handle).toByteArray()
}
