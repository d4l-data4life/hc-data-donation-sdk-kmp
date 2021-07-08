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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.encryption.hybrid.HybridEncryption
import care.data4life.datadonation.encryption.signature.SignatureKeyPrivate
import care.data4life.datadonation.internal.data.exception.MissingCredentialsException
import care.data4life.datadonation.internal.data.model.*
import care.data4life.datadonation.internal.data.service.ConsentService
import care.data4life.datadonation.internal.domain.mock.*
import care.data4life.datadonation.internal.domain.repository.DonationRepository
import care.data4life.datadonation.internal.domain.repository.ServiceTokenRepository
import care.data4life.datadonation.internal.utils.Base64Encoder
import care.data4life.datadonation.internal.utils.toJsonString
import care.data4life.datadonation.mock.spy.CapturingResultListener
import care.data4life.datadonation.mock.stub.ConsentDataStoreStub
import care.data4life.datadonation.mock.stub.UserConsentRepositoryStub
import care.data4life.hl7.fhir.stu3.FhirStu3Parser
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.Reference
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.json.Json
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

abstract class DonateResourcesTest {

    private val dummyNonce = "random_nonce"
    private val dummyPublicKey64Encoded = "publicKey64Encoded"
    private val dummySignature = "signature"
    private val dummyEncryptedRequest = byteArrayOf(1, 2, 3)
    private val dummyEncryptedRequest64Encoded = "encryptedRequest64Encoded"
    private val dummyEncryptedSignedMessage = byteArrayOf(4, 5)

    private val dummyResponse = QuestionnaireResponse(
        status = QuestionnaireResponseStatus.COMPLETED,
        id = "id_1",
        language = "en",
        questionnaire = Reference(id = "questionnaire_id"),
        item = emptyList()
    )
    private val dummyResourceList =
        listOf(dummyResponse, dummyResponse.copy(id = "id_2"), dummyResponse.copy(id = "id_3"))
    private val dummyEncryptedResourceList =
        listOf(DummyData.rawData, DummyData.rawData, DummyData.rawData)
    private val dummyEncryptedResourceSignatureList =
        listOf(DummyData.rawData, DummyData.rawData, DummyData.rawData)

    private val jsonParser = Json {}

    private val mockUserConsentDataStore = ConsentDataStoreStub()
    private val mockDonationDataStore = MockDonationDataStore()
    private val mockServiceTokenDataStore = MockServiceTokenDataStore()
    private val mockFilterSensitiveInformation = MockFilterSensitiveInformation()
    private val mockRemoveInternalInformation = MockRemoveInternalInformation(jsonParser)
    private val mockUserConsentRepository = UserConsentRepositoryStub()
    private val serviceTokenRepository = ServiceTokenRepository(mockServiceTokenDataStore)
    private val donationRepository = DonationRepository(mockDonationDataStore)

    private val fhirParser = FhirStu3Parser.defaultJsonParser()

    private val signatureKey = object : SignatureKeyPrivate {
        override fun sign(data: ByteArray) = when (data) {
            dummyEncryptedResourceList[0] -> dummyEncryptedResourceSignatureList[0]
            dummyEncryptedResourceList[1] -> dummyEncryptedResourceSignatureList[1]
            dummyEncryptedResourceList[2] -> dummyEncryptedResourceSignatureList[2]
            else -> byteArrayOf()
        }

        override fun serializedPrivate() = DummyData.keyPair.private
        override val pkcs8Private = ""
        override fun verify(data: ByteArray, signature: ByteArray) = true
        override fun serializedPublic() = DummyData.keyPair.public
        override val pkcs8Public = ""
    }

    private val requestJsonString =
        ConsentRequest(signatureKey.pkcs8Public, dummyNonce).toJsonString()

    private val consentMessage = ConsentMessage(
        ConsentService.defaultDonationConsentKey,
        ConsentSignatureType.NormalUse.apiValue,
        dummyEncryptedRequest64Encoded
    )

    private val signedConsentJsonString =
        SignedConsentMessage(consentMessage.toJsonString(), dummySignature).toJsonString()

    private val encryptorDD = object : HybridEncryption {
        override fun encrypt(plaintext: ByteArray) = when (plaintext.decodeToString()) {
            requestJsonString -> dummyEncryptedRequest
            signedConsentJsonString -> dummyEncryptedSignedMessage
            else -> byteArrayOf()
        }

        override fun decrypt(ciphertext: ByteArray) = Result.success(byteArrayOf())
    }

    private val encryptorALP = object : HybridEncryption {
        override fun encrypt(plaintext: ByteArray) =
            when (fhirParser.fromJson(QuestionnaireResponse::class, plaintext.decodeToString())) {
                dummyResourceList[0] -> dummyEncryptedResourceList[0]
                dummyResourceList[1] -> dummyEncryptedResourceList[1]
                dummyResourceList[2] -> dummyEncryptedResourceList[2]
                else -> byteArrayOf()
            }

        override fun decrypt(ciphertext: ByteArray) = Result.success(byteArrayOf())
    }

    private val base64Encoder = object : Base64Encoder {
        override fun encode(src: ByteArray) = dummyEncryptedRequest64Encoded
        override fun decode(src: ByteArray, charset: Charset) = ""
    }

    private val createRequestConsentPayload = CreateRequestConsentPayload(
        serviceTokenRepository,
        mockUserConsentRepository,
        encryptorDD,
        base64Encoder
    )

    private val donateResources =
        DonateResources(
            mockFilterSensitiveInformation,
            mockRemoveInternalInformation,
            createRequestConsentPayload,
            donationRepository,
            encryptorALP
        ) { signatureKey }

    private val capturingListener = DonateResourcesListener()

    @Test
    fun donateResourcesTest() = runBlockingTest {
        // Given
        var result: DonationPayload? = null
        mockServiceTokenDataStore.whenRequestDonationToken = { dummyNonce }
        mockUserConsentRepository.whenSignUserConsent = { _ -> dummySignature }
        mockDonationDataStore.whenDonateResources = { payload -> result = payload }

        // When
        donateResources.runWithParams(
            DonateResources.Parameters(DummyData.keyPair, dummyResourceList),
            capturingListener
        )

        // Then
        assertEquals(capturingListener.captured, Unit)
        assertTrue(result!!.request.contentEquals(dummyEncryptedSignedMessage))
        assertEquals(result!!.documents.size, dummyResourceList.size)
        assertTrue(result!!.documents[0].document.contentEquals(dummyEncryptedResourceList[0]))
        assertTrue(result!!.documents[0].signature.contentEquals(dummyEncryptedResourceSignatureList[0]))
        assertNull(capturingListener.error)
    }

    @Test
    fun donateResourcesTestWithoutKeyFails() = runBlockingTest {
        // Given
        mockServiceTokenDataStore.whenRequestDonationToken = { dummyNonce }
        mockUserConsentRepository.whenSignUserConsent = { _ -> dummySignature }

        // When
        donateResources.runWithParams(
            DonateResources.Parameters(null, dummyResourceList),
            capturingListener
        )

        // Then
        assertNull(capturingListener.captured)
        assertTrue(capturingListener.error is MissingCredentialsException)
    }

    class DonateResourcesListener : CapturingResultListener<Unit>()
}
