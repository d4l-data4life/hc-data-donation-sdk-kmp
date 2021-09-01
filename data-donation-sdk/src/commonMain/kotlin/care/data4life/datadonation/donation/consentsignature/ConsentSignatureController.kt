/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.donation.DonationContract
import care.data4life.datadonation.donation.donationservice.Token
import care.data4life.datadonation.donation.model.ConsentRequestMessage
import care.data4life.datadonation.donation.model.ConsentSigningRequest
import care.data4life.datadonation.donation.model.SignedConsentMessage
import care.data4life.datadonation.session.SessionTokenRepositoryContract
import care.data4life.sdk.util.Base64
import kotlinx.serialization.json.Json

internal class ConsentSignatureController(
    private val repository: ConsentSignatureContract.Repository,
    private val userSessionTokenRepository: SessionTokenRepositoryContract,
    private val cryptor: CryptoContract.Service,
    private val serializer: Json
) : ConsentSignatureContract.Controller {
    override suspend fun enableSigning(
        token: Token,
        consentDocumentKey: String,
        donorPublicKey: String,
        donationServicePublicKey: String
    ): SignedConsentMessage {
        val accessToken = userSessionTokenRepository.getUserSessionToken()

        /*
         * TODO: Rethink once every Case is present how to structure it properly since this
         * technically the mapping to the DataTransferObject belongs in the repository.
         */
        val message = ConsentRequestMessage(
            token = token,
            donorId = donorPublicKey
        )

        val encryptedSerializedMessage = serializer.encodeToString(
            ConsentRequestMessage.serializer(),
            message
        ).let { serializedMessage ->
            cryptor.encrypt(
                serializedMessage.encodeToByteArray(),
                donationServicePublicKey
            )
        }

        val request = ConsentSigningRequest(
            consentDocumentKey = consentDocumentKey,
            payload = Base64.encodeToString(encryptedSerializedMessage),
            signatureType = DonationContract.ConsentSignatureType.CONSENT_ONCE
        ).let { plainRequest ->
            serializer.encodeToString(
                ConsentSigningRequest.serializer(),
                plainRequest
            )
        }

        val signature = repository.enableSigning(
            accessToken = accessToken,
            consentDocumentKey = consentDocumentKey,
            request = request
        )

        return SignedConsentMessage(
            consentMessageJSON = request,
            signature = signature
        )
    }
}
