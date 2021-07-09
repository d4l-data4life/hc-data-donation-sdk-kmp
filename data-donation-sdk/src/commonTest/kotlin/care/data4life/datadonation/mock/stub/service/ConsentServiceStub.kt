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

package care.data4life.datadonation.mock.stub.service

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import care.data4life.datadonation.internal.data.service.ServiceContract

class ConsentServiceStub : ServiceContract.ConsentService {
    override suspend fun fetchConsentDocuments(
        accessToken: String,
        version: Int?,
        language: String?,
        consentKey: String
    ): List<ConsentDocument> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserConsents(
        accessToken: String,
        latestConsent: Boolean?,
        consentKey: String?
    ): List<UserConsent> {
        TODO("Not yet implemented")
    }

    override suspend fun createUserConsent(accessToken: String, consentKey: String, version: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun requestSignatureRegistration(
        accessToken: String,
        message: String
    ): ConsentSignature {
        TODO("Not yet implemented")
    }

    override suspend fun requestSignatureDonation(
        accessToken: String,
        message: String
    ): ConsentSignature {
        TODO("Not yet implemented")
    }

    override suspend fun revokeUserConsent(accessToken: String, consentKey: String) {
        TODO("Not yet implemented")
    }
}
