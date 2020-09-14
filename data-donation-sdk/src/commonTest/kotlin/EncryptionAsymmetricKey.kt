import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.assymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.initEncryption
import care.data4life.datadonation.encryption.symmetric.EncryptionSymmetricKey
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

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

class EncryptionKeyCommonTest {

    @BeforeTest
    fun setup() {
        initEncryption()
    }

    @Test
    fun `Generate, encrypt and decrypt`() {
        val testData = byteArrayOf(1, 2, 3, 4, 5)

        val key = EncryptionPrivateKey(2048, Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))
        val encrypted = key.encrypt(testData)
        val decrypted = key.decrypt(encrypted)
        assertTrue(decrypted.isSuccess)
        assertTrue(decrypted.getOrThrow().contentEquals(testData))
    }

    @Test
    fun `Generate, serialize and deserialize`() {
        val key = EncryptionPrivateKey(2048, Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))

        val private = key.serializedPrivate()
        val public = key.serializedPublic()

        with(EncryptionPrivateKey(private,public,2048,Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))) {
            assertTrue(private.contentEquals(serializedPrivate()))
            assertTrue(public.contentEquals(serializedPublic()))
        }

    }

}
