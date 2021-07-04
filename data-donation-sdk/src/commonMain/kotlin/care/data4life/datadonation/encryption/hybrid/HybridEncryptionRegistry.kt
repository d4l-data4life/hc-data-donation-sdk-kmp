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

package care.data4life.datadonation.encryption.hybrid

import care.data4life.datadonation.Contract
import care.data4life.datadonation.encryption.Algorithm
import care.data4life.datadonation.encryption.EncryptionContract
import care.data4life.datadonation.encryption.EncryptionContract.AsymmetricKeyProvider
import care.data4life.datadonation.encryption.EncryptionContract.HybridEncryption
import care.data4life.datadonation.encryption.EncryptionContract.SymmetricKeyProvider
import care.data4life.datadonation.encryption.HashSize
import care.data4life.datadonation.encryption.asymetric.EncryptionPrivateKey
import care.data4life.datadonation.encryption.asymetric.EncryptionPublicKey
import care.data4life.datadonation.encryption.symmetric.EncryptionSymmetricKey
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.internal.utils.decodeBase64Bytes

internal class HybridEncryptionRegistry(
    private val repository: RepositoryContract.CredentialsRepository
) : EncryptionContract.HybridEncryptionRegistry {

    companion object {
        fun createEncryption(repository: RepositoryContract.CredentialsRepository, service: Contract.Service): HybridEncryption {
            val publicKey = when (service) {
                Contract.Service.DD -> repository.getDataDonationPublicKey()
                Contract.Service.ALP -> repository.getAnalyticsPlatformPublicKey()
            }
            return HybridEncryptionHandle(
                HybridEncryptionSymmetricKeyProvider,
                HybridAsymmetricSymmetricKeyProvider(publicKey),
                HybridEncryptionSerializer
            )
        }
    }

    override val hybridEncryptionDD: HybridEncryption by lazy { createEncryption(repository, Contract.Service.DD) }
    override val hybridEncryptionALP: HybridEncryption by lazy { createEncryption(repository, Contract.Service.ALP) }
}

internal object HybridEncryptionSymmetricKeyProvider : SymmetricKeyProvider {
    override fun getNewKey(): EncryptionSymmetricKey {
        return EncryptionSymmetricKey(
            EncryptionContract.AES_KEY_LENGTH,
            Algorithm.Symmetric.AES(HashSize.Hash256)
        )
    }

    override fun getKey(keyData: ByteArray): EncryptionSymmetricKey {
        return EncryptionSymmetricKey(
            keyData,
            EncryptionContract.AES_KEY_LENGTH,
            Algorithm.Symmetric.AES(HashSize.Hash256)
        )
    }

    override fun getAuthenticationData() = byteArrayOf()
}

internal class HybridAsymmetricSymmetricKeyProvider(
    dataDonationPublicKey: String
) : AsymmetricKeyProvider {

    private val publicKey = EncryptionPublicKey(
        dataDonationPublicKey.decodeBase64Bytes(),
        EncryptionContract.RSA_KEY_SIZE_BITS,
        Algorithm.Asymmetric.RsaOAEP(HashSize.Hash256)
    )

    override fun getPublicKey(): EncryptionPublicKey = publicKey

    override fun getPrivateKey(): EncryptionPrivateKey {
        throw NotImplementedError("Interface method only available for testing purposes")
    }
}
