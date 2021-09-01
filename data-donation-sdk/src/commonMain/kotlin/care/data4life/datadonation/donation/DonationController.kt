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

package care.data4life.datadonation.donation

import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.donation.DonationContract.Companion.DONOR_IDENTITY_KEY_TYPE
import care.data4life.datadonation.donation.DonationContract.Companion.DONOR_IDENTITY_KEY_VERSION
import care.data4life.datadonation.donation.consentsignature.ConsentSignatureContract
import care.data4life.datadonation.donation.donationservice.DonationServiceContract
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import care.data4life.datadonation.donation.model.DonorIdentity
import care.data4life.datadonation.donation.program.ProgramContract
import care.data4life.datadonation.donation.publickeyservice.PublicKeyServiceContract

internal class DonationController(
    private val programService: ProgramContract.Controller,
    private val publicKeysService: PublicKeyServiceContract.Repository,
    private val keyStorage: DonorKeyStorageRepositoryContract,
    private val consentSignatureService: ConsentSignatureContract.Controller,
    private val donationService: DonationServiceContract.Controller,
    private val keyGenerator: CryptoContract.KeyFactory
) : DonationContract.Controller {
    private suspend fun createAndStoreNewDonorIdentity(
        documentConsentKey: String,
        programName: String
    ): DonorIdentity {
        val donorKeys = keyGenerator.createKeyPair()

        val donorIdentity = DonorIdentity(
            keyType = DONOR_IDENTITY_KEY_TYPE,
            version = DONOR_IDENTITY_KEY_VERSION,
            privateKey = donorKeys.privateKey,
            publicKey = donorKeys.publicKey,
            scope = documentConsentKey
        )

        keyStorage.save(
            NewDonor(
                donorIdentity = donorIdentity,
                programName = programName
            )
        )

        return donorIdentity
    }

    private suspend fun resolveDonorIdentityOnRegister(
        documentConsentKey: String,
        programName: String
    ): DonorIdentity {
        val donor = keyStorage.load(programName)
        return if (donor is Donor) {
            donor.donorIdentity
        } else {
            createAndStoreNewDonorIdentity(
                documentConsentKey,
                programName
            )
        }
    }

    override suspend fun register(programName: String) {
        val program = programService.fetchProgram(programName)
        val donorIdentity = resolveDonorIdentityOnRegister(
            program.configuration!!.consentDocumentKey,
            programName
        )

        val publicKeys = publicKeysService.fetchPublicKeys()

        val token = donationService.fetchToken()
        val signedConsent = consentSignatureService.enableSigning(
            token = token,
            consentDocumentKey = program.configuration.consentDocumentKey,
            donorPublicKey = donorIdentity.publicKey,
            donationServicePublicKey = publicKeys.donationService
        )

        return donationService.register(
            signedConsent,
            publicKeys.donationService
        )
    }
}
