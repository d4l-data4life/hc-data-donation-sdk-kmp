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
import care.data4life.datadonation.donation.donationservice.model.RevocationTypeSerializer
import care.data4life.datadonation.networking.HttpRuntimeError
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.serialization.Serializable

internal typealias SerializedJson = String
internal typealias EncryptedJSON = ByteArray
internal typealias Signature = String
internal typealias DonorId = String
internal typealias UUID = String
internal typealias Token = String

internal interface DonationServiceContract {
    @Serializable(with = RevocationTypeSerializer::class)
    enum class RevocationType(val value: String) {
        DELETE("delete"),
        UNMAP("unmap")
    }

    interface ApiService {
        fun fetchToken(): Token
        fun register(signedConsentMessage: ByteArray): Unit
        fun donate(donation: MultiPartFormDataContent): Unit
        fun revoke(donation: MultiPartFormDataContent): DeletionProof

        companion object {
            val ROUTE = listOf(
                "donation", "api", "v1"
            )
        }

        interface ErrorHandler {
            fun handleFetchToken(error: HttpRuntimeError): DonationServiceError
            fun handleRegister(error: HttpRuntimeError): DonationServiceError
            fun handleDonate(error: HttpRuntimeError): DonationServiceError
            fun handleRevoke(error: HttpRuntimeError): DonationServiceError
        }
    }

    interface Repository
}
