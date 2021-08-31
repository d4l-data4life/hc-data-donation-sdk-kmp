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
import care.data4life.datadonation.donation.donationservice.Token
import care.data4life.datadonation.donation.model.SignedConsentMessage
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

internal class DonationServiceControllerStub: DonationServiceContract.Controller, MockContract.Stub{
    var whenFetchToken: (() -> Token)? = null
    var whenRegister: ((SignedConsentMessage, String) -> Unit)? = null

    override suspend fun fetchToken(): Token {
        return whenFetchToken?.invoke() ?: throw MockException()
    }

    override suspend fun register(
        signedConsent: SignedConsentMessage,
        donationServicePublicKey: String
    ) {
        return whenRegister?.invoke(signedConsent, donationServicePublicKey) ?: throw MockException()
    }

    override fun clear() {
        whenFetchToken = null
        whenRegister = null
    }
}
