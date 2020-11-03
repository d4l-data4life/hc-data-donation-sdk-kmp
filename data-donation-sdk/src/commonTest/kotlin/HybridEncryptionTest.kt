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

import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.assymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.hybrid.*
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_AUTH_TAG_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_IV_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.AES_KEY_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryption.Companion.RSA_KEY_SIZE_BITS
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.AES_KEY_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.CIPHERTEXT_SIZE_LENGTH
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionPayload.Companion.VERSION_LENGTH
import care.data4life.datadonation.internal.utils.*
import kotlin.random.Random
import kotlin.test.*


open class HybridEncryptionTest {

    private val rsaKey =
        EncryptionPrivateKey(RSA_KEY_SIZE_BITS, Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256))
    private val handle = HybridEncryptionHandle(
        HybridEncryptionSymmetricKeyProvider,
        object : HybridEncryption.AsymmetricKeyProvider {
            override fun getPublicKey() = rsaKey
            override fun getPrivateKey() = rsaKey
        },
        HybridEncryptionSerializer
    )


    @Test
    fun `Generate, encrypt and decrypt`() {
        val randomSize = Random.nextInt(5, 25)
        val plaintext = ByteArray(randomSize)
        Random.nextBytes(plaintext)

        val hybridEncryptedResult = handle.encrypt(plaintext)
        // ciphertext same length as plaintext
        // expected output size: version (1) + key size value (2) + encryptedKey.size (256) + iv.size (12) + ciphertext size value (8) + ciphertext.size
        val expectedLength =
            VERSION_LENGTH + AES_KEY_SIZE_LENGTH + AES_KEY_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH + plaintext.size + AES_AUTH_TAG_LENGTH
        assertEquals(expectedLength, hybridEncryptedResult.size)

        val result = handle.decrypt(hybridEncryptedResult)
        if (result.isFailure) {
            result.exceptionOrNull()?.printStackTrace()
        }
        assertTrue(result.isSuccess)

        val encoder = CommonBase64Encoder
        assertEquals(encoder.encode(result.getOrThrow()), encoder.encode(plaintext))
    }

    @Test
    fun `Generate, encrypt and decrypt from text`() {
        val plaintext = "Az újszülött kiscicák szervezetébe antitestek jutnak az anyatejen keresztül"
        val hybridEncryptedResult = handle.encrypt(plaintext.encodeToByteArray())
        // ciphertext same length as plaintext
        // expected output size: version (1) + key size value (2) + encryptedKey.size (256) + iv.size (12) + ciphertext size value (8) + ciphertext.size
        val expectedLength =
            VERSION_LENGTH + AES_KEY_SIZE_LENGTH + AES_KEY_LENGTH + AES_IV_LENGTH + CIPHERTEXT_SIZE_LENGTH + plaintext.encodeToByteArray().size + AES_AUTH_TAG_LENGTH
        assertEquals(expectedLength, hybridEncryptedResult.size)
        val result = handle.decrypt(hybridEncryptedResult)
        if (result.isFailure) {
            result.exceptionOrNull()?.printStackTrace()
        }
        assertTrue(result.isSuccess)
        assertEquals(plaintext, result.getOrThrow().decodeToString())
    }

    @Test
    fun encryptFixtures() {
        //Given
        val privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC/6afBSmzhiMEE" +
                "NMSKeky8h7SV/sdQfSnb1PKGzycqUuUlOWlmsoc27WhSL6pgcQpvyRwU3pkhQlAO" +
                "vko3Y684dRQVDWA12ehHOAvRu6Edup/dvsN42nHFTbFqCAJ+zYHAVraPibXxNSzE" +
                "8OKBezRjiySu2zATmjmRGA0P7ALEH/kuMdUTD/cZGb9eKEWDQ9JACADFgwzbab7t" +
                "da8+eqTj85rxBVSRZgoTtakZyanYSesyS8jvcuugX/lDpfsRhe2AbnPI0wJVEBDe" +
                "jUobaOd3AuTowMXGhJU9gPYOrTb6YmEuyE9FZ9F57wah+pEEl3o6+4OXflqB2ZMT" +
                "SWXNYnQhAgMBAAECggEAVjufbXMLyauxTzqGtdKOeIhh1KRO2xPioyzkbT7X0mS9" +
                "IiTR/5totn2myocwf3VLwz8Spy3+kLtDTdyjbJAWQ8AX7f28pXXssVO1u+AbXUhm" +
                "XTVCkCNXy9hFR+ehd2jQTKSqE4VFg8TpAPVcUeISgEgdi5Rh3e0GwPOVqvnZpFYS" +
                "TXpsl8GOhStNdrMv+0pOBfYBcqTWxnzaPOmnbg7kRIRk3lA6C//gtBMczPynVV3J" +
                "Omh7DjRPfsEHhjhNipKC38HE+PH+KH5MuNMqMlLC5G5i3/2kyqD3ou5SYERK10tH" +
                "bFfNKb6s9SCEAkRkRdwpKdzvhqGrBDOtRx+fqP55gQKBgQDk3YbZ4SOEiQehB4Ti" +
                "jSug1lBAKyWD+GHJKtinIf3L4orcKduMTLB8g1JtNcTUGd74mS1brMMZRsckybks" +
                "CTPPPUcKovRpJ1TyoLonzjXsT7aCWqkKrODV47Y2UiHW30LNUOYD2HsB2cEE7DWr" +
                "zs1yA6ACdzRSNtXyan1GZ9LelQKBgQDWqozZhAEeJQXszdW/42+rvhyrUPB5R5BD" +
                "tM801QI3oN+EruqzXiV7TvsdkVDPvspgnmO3jCIbNTg+v+PfxKbZC0SUPjhAWKv7" +
                "MqD2/vxpUmfubUMn3kQ9nOwD+UUbSyBsXiJVbT1Jk67viMTodY80U2awdMcP9i3U" +
                "TtTTlfs4XQKBgQDWY+5/C7gJ35OV9UU2NKg58okak1CBX5u6prhtWBo3c/BAbbWM" +
                "qAprmVkNlODdD58fod4rkprwgqzqeU1NQxGVgQGbpSrvljitUIMR5sn8pG+DjQnt" +
                "RiUYOEfoeufYMSySyMWvtIsGIMX/poZge0lZFKw/owsQOO4SOE9CNOAEQQKBgGci" +
                "s99Bs8PG5+zZDAxQenOaOG36yj6Kqn5NHYx1lsYhTaKS44JgBkQTM0UGbzwQn20C" +
                "TEiAVFacIDTNvu7grYT0C/PpXN9VevOKZJmm8qCrjfGYnz5FZKXxgdd66L/vPVmz" +
                "dG10uZBjGxZMsMY6zR3HwDuhL460qVoqscgic/ulAoGBANwfwagDk7yUG3rOulRk" +
                "cy9494j/TV5hlKiPNWuIhNvCNjKqo5sUysxVRp5zc162B7+SplOmctbJ2CZtO5ug" +
                "djMbbsWWf7k8verVb+MgPqO1Grhm87uPwRvZGC2GEG6BknnqMdxOggsBW0hVz9og" +
                "+dFwONWN+SFj+uKbzizx1yuf"

        val publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv+mnwUps4YjBBDTEinpM" +
                "vIe0lf7HUH0p29Tyhs8nKlLlJTlpZrKHNu1oUi+qYHEKb8kcFN6ZIUJQDr5KN2Ov" +
                "OHUUFQ1gNdnoRzgL0buhHbqf3b7DeNpxxU2xaggCfs2BwFa2j4m18TUsxPDigXs0" +
                "Y4skrtswE5o5kRgND+wCxB/5LjHVEw/3GRm/XihFg0PSQAgAxYMM22m+7XWvPnqk" +
                "4/Oa8QVUkWYKE7WpGcmp2EnrMkvI73LroF/5Q6X7EYXtgG5zyNMCVRAQ3o1KG2jn" +
                "dwLk6MDFxoSVPYD2Dq02+mJhLshPRWfRee8GofqRBJd6OvuDl35agdmTE0llzWJ0" +
                "IQIDAQAB"
        val publicKeyLength = 2048

        val encodedMessageV2 = "02000178941374f21d384b111bd301c74b3d831d83d942b81b15edb4e7f6b1a9" +
                "b652f5380e55a0ad6597ee6a11d410f18555c2d79de2ff77068907ed57269064" +
                "a47305f86dae206065da5d155ec6a0bccc66b928d71694ffbb4b082103b47bd8" +
                "a65787e2c2f82b2afe5f1a871c3b504be17f55b19db2dbeb7275141ceb63389e" +
                "6da0b82cf29cfca5a6a2c1385ffee9af98483b6deae18d0aaf07e0a030d34820" +
                "8e9b58a62b177b9266bb10ef03bf0d292e256d92d697fe48de8cc6641b3465ac" +
                "aad11ad87a4d5ddc83e482734dd81e80ae6009cde60e083e0f96eeac8af74b1b" +
                "23c60f9d985d85eec37553c17371b258cc658da376fdac35d91203ed267d9d1f" +
                "da76f0b1504dba800b3deea0d21a92481b662d2400000000000000796435ab01" +
                "175a78f5efa9dfade1537c0552ec7be7e2453b8b9eaca461443784f624c4e6"

        val decodedMessageExpected = "{\"a\":\"Hello World!\"}"
        //When
        val key = EncryptionPrivateKey(
            privateKey.decodeBase64Bytes(),
            publicKey.decodeBase64Bytes(),
            publicKeyLength,
            Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256)
        )
        val hybrid = HybridEncryptionHandle(
            HybridEncryptionSymmetricKeyProvider,
            object : HybridEncryption.AsymmetricKeyProvider {
                override fun getPublicKey() = key
                override fun getPrivateKey() = key
            },
            HybridEncryptionSerializer
        )
        val decrypted = hybrid.decrypt(encodedMessageV2.decodeHexBytes())
        if (decrypted.isFailure) {
            decrypted.exceptionOrNull()?.printStackTrace()
        }
        //Then
        assertTrue(decrypted.isSuccess)
        assertEquals(decodedMessageExpected, decrypted.getOrNull()?.decodeToString())
    }
}
