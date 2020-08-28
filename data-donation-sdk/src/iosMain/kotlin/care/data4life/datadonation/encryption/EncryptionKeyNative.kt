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

import platform.Security.SecKeyAlgorithm
import platform.Security.SecKeyRef

class EncryptionKeyNative : KeyNative, EncryptionKeyPrivate {

    constructor(keyType: SecKeyAlgorithm, algoType: SecKeyAlgorithm, size: Int)
            : super(keyType, algoType, size)

    constructor(private: SecKeyRef, public: SecKeyRef, algoType: SecKeyAlgorithm)
            : super(private, public, algoType)

    override fun decrypt(data: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun serializedPrivate(): ByteArray {
        TODO("Not yet implemented")
    }

    override val pkcs8Private: String
        get() = TODO("Not yet implemented")

    override fun encrypt(data: ByteArray, signature: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    override fun serializedPublic(): ByteArray {
        TODO("Not yet implemented")
    }

    override val pkcs8Public: String
        get() = TODO("Not yet implemented")

}
