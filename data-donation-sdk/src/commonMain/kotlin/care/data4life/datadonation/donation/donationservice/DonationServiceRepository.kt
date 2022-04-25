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

package care.data4life.datadonation.donation.donationservice

import care.data4life.datadonation.donation.donationservice.model.DeletionProof
import io.ktor.client.request.forms.MultiPartFormDataContent

internal class DonationServiceRepository(
    private val apiService: DonationServiceContract.ApiService
) : DonationServiceContract.Repository {
    override suspend fun fetchToken(): Token {
        return apiService.fetchToken()
    }

    override suspend fun register(encryptedJSON: EncryptedJSON) {
        return apiService.register(encryptedJSON)
    }

    override suspend fun donate(donations: MultiPartFormDataContent) {
        return apiService.donate(donations)
    }

    override suspend fun revoke(donation: MultiPartFormDataContent): DeletionProof {
        return apiService.revoke(donation)
    }
}
