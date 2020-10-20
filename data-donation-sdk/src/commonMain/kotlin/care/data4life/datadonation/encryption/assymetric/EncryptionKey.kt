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

expect fun EncryptionPrivateKey(serializedPrivate: ByteArray, serializedPublic: ByteArray, size: Int, algorithm: Algorithm.Asymmetric): EncryptionPrivateKey

expect fun EncryptionPublicKey(serializedKey: ByteArray, size: Int, algorithm: Algorithm.Asymmetric): EncryptionPublicKey

expect fun EncryptionPrivateKey(size: Int, algorithm: Algorithm.Asymmetric): EncryptionPrivateKey

//TODO: expect fun EncryptionKeyPublic(pkcs1: String):EncryptionKeyPublic


interface EncryptionPrivateKey: EncryptionPublicKey {
    fun decrypt(data:ByteArray):Result<ByteArray>
    fun serializedPrivate():ByteArray
    val pkcs8Private:String
}

interface EncryptionPublicKey {
    fun encrypt(data:ByteArray):ByteArray
    fun serializedPublic():ByteArray
    val pkcs8Public:String
}
