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

import care.data4life.datadonation.donation.DonationContract
import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.donation.model.ConsentSigningRequest
import care.data4life.datadonation.networking.AccessToken

internal class ConsentSignatureRepository(
    val apiService: ConsentSignatureContract.ApiService
) : ConsentSignatureContract.Repository {
    override suspend fun enableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        request: SignatureRequest
    ): Signature {
        return apiService.enableSigning(
            accessToken,
            consentDocumentKey,
            request
        ).signature
    }

    override suspend fun sign(
        accessToken: AccessToken,
        consentDocumentKey: String,
        message: String
    ): Signature {
        val signingRequest = ConsentSigningRequest(
            consentDocumentKey = consentDocumentKey,
            payload = message,
            signatureType = DonationContract.ConsentSignatureType.NORMAL_USE
        )

        return apiService.sign(
            accessToken,
            consentDocumentKey,
            signingRequest
        ).signature
    }

    override suspend fun disableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        message: SignedDeletionMessage
    ) {
        return apiService.disableSigning(accessToken, consentDocumentKey, message)
    }
}
