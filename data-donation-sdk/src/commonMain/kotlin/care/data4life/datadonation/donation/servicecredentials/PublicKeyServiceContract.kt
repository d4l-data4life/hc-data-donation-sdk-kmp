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

package care.data4life.datadonation.donation.servicecredentials

import care.data4life.datadonation.donation.servicecredentials.model.PublicKeys
import care.data4life.datadonation.donation.servicecredentials.model.RawKeys
import care.data4life.datadonation.networking.HttpRuntimeError

internal interface PublicKeyServiceContract {
    enum class KeyDomain(val domain: String) {
        DonationService("donation_public_key"),
        ALP("alp_public_key")
    }

    interface ApiService {
        fun latestUpdate(): String
        fun fetchPublicKeys(): RawKeys

        companion object {
            val ROUTE = listOf("mobile", "credentials.json")
        }

        interface ErrorHandler {
            fun handlePublicKeys(error: HttpRuntimeError): ServiceCredentialsError
        }
    }

    interface Repository {
        fun fetchPublicKeys(): PublicKeys

        fun interface KeyMapper {
            fun mapKeys()
        }
    }
}
