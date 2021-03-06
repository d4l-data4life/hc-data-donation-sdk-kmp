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

package care.data4life.datadonation.mock.stub.donation.donationservice

import care.data4life.datadonation.donation.donationservice.DonationServiceContract
import care.data4life.datadonation.donation.donationservice.EncryptedJSON
import care.data4life.datadonation.donation.donationservice.Token
import care.data4life.datadonation.donation.donationservice.model.DeletionProof
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import io.ktor.client.request.forms.MultiPartFormDataContent

internal class DonationServiceApiServiceStub : DonationServiceContract.ApiService, MockContract.Stub {
    var whenFetchToken: (() -> Token)? = null
    var whenRegister: ((EncryptedJSON) -> Unit)? = null
    var whenDonate: ((MultiPartFormDataContent) -> Unit)? = null
    var whenRevoke: ((MultiPartFormDataContent) -> DeletionProof)? = null

    override suspend fun fetchToken(): Token {
        return whenFetchToken?.invoke() ?: throw MockException()
    }

    override suspend fun register(encryptedJSON: EncryptedJSON) {
        return whenRegister?.invoke(encryptedJSON) ?: throw MockException()
    }

    override suspend fun donate(donations: MultiPartFormDataContent) {
        return whenDonate?.invoke(donations) ?: throw MockException()
    }

    override suspend fun revoke(donation: MultiPartFormDataContent): DeletionProof {
        return whenRevoke?.invoke(donation) ?: throw MockException()
    }

    override fun clear() {
        whenFetchToken = null
        whenRegister = null
        whenDonate = null
        whenRevoke = null
    }
}
