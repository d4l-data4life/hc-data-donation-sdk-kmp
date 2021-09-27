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

package care.data4life.datadonation.donation.publickeyservice

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.donation.publickeyservice.model.RawKeys
import care.data4life.datadonation.donation.publickeyservice.model.RawServiceCredentialKey
import care.data4life.datadonation.donation.publickeyservice.model.ServiceCredentialKeys
import care.data4life.datadonation.error.CoreRuntimeError

internal class PublicKeyServiceKeyMapper(
    private val environment: DataDonationSDK.Environment
) : PublicKeyServiceContract.Repository.KeyMapper {
    private fun filterRawKeys(rawKeys: RawKeys): Pair<RawServiceCredentialKey, RawServiceCredentialKey> {
        val keys = rawKeys.credentials.filter { rawKey -> environment == rawKey.environment }

        return if (keys.size != 2) {
            throw CoreRuntimeError.MissingCredentials("Malformed credential source.")
        } else {
            Pair(keys.first(), keys.last())
        }
    }

    private fun retrieveDomainKey(
        serviceCredentialKeyPair: List<RawServiceCredentialKey>,
        domain: PublicKeyServiceContract.KeyDomain
    ): String {
        val key = serviceCredentialKeyPair.find { rawKey ->
            rawKey.domain == domain
        }

        return if (key is RawServiceCredentialKey) {
            key.key
        } else {
            throw CoreRuntimeError.MissingCredentials(
                "Malformed credential source - No ${domain.name}Key found."
            )
        }
    }

    private fun mapToPublicKey(serviceCredentialKeyPair: Pair<RawServiceCredentialKey, RawServiceCredentialKey>): ServiceCredentialKeys {
        val keys = serviceCredentialKeyPair.toList()

        return ServiceCredentialKeys(
            donationService = retrieveDomainKey(keys, PublicKeyServiceContract.KeyDomain.DonationService),
            alp = retrieveDomainKey(keys, PublicKeyServiceContract.KeyDomain.ALP)
        )
    }

    override fun mapKeys(rawKeys: RawKeys): ServiceCredentialKeys {
        val potentialKeys = filterRawKeys(rawKeys)
        return mapToPublicKey(potentialKeys)
    }
}
