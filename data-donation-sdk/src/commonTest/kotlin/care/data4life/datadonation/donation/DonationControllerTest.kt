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

import care.data4life.datadonation.crypto.model.KeyPair
import care.data4life.datadonation.donation.donationservice.Token
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import care.data4life.datadonation.donation.model.SignedConsentMessage
import care.data4life.datadonation.donation.publickeyservice.model.ServicePublicKeyring
import care.data4life.datadonation.mock.fixture.DonorIdentityFixture
import care.data4life.datadonation.mock.fixture.ProgramFixture
import care.data4life.datadonation.mock.stub.crypto.KeyFactoryStub
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureControllerStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceControllerStub
import care.data4life.datadonation.mock.stub.donation.donorkeystorage.DonorKeyStorageRepositoryStub
import care.data4life.datadonation.mock.stub.donation.program.ProgramControllerStub
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonationControllerTest {
    @Test
    fun `It fulfils DonationController`() {
        val controller: Any = DonationController(
            ProgramControllerStub(),
            PublicKeyServiceRepositoryStub(),
            DonorKeyStorageRepositoryStub(),
            ConsentSignatureControllerStub(),
            DonationServiceControllerStub(),
            KeyFactoryStub()
        )

        assertTrue(controller is DonationContract.Controller)
    }

    @Test
    fun `Given register is called with a ProgramName, it registers a user for the given program, while using a exisitng DonorKeys`() =
        runBlockingTest {
            // Given
            val programName = "Potato"
            val program = ProgramFixture.sampleProgram
            val donor = Donor(
                recordId = "23",
                donorIdentity = DonorIdentityFixture.sampleIdentity,
                programName = programName
            )
            val token = "Tomato"
            val publicKeys = ServicePublicKeyring(
                donationKeyBase64Encoded = "Broccoli",
                alpKeyBase64Encoded = "Onion"
            )
            val signedConsentMessage = SignedConsentMessage(
                consentMessageJSON = "Salt",
                signature = "Pepper"
            )

            val programController = ProgramControllerStub()

            var capturedProgramNameFetch: String? = null
            programController.whenFetchProgram = { delegatedProgramName ->
                capturedProgramNameFetch = delegatedProgramName

                program
            }

            val donorKeyStorageRepository = DonorKeyStorageRepositoryStub()

            var capturedProgramNameLoad: String? = null
            donorKeyStorageRepository.whenLoad = { delegatedProgramName ->
                capturedProgramNameLoad = delegatedProgramName

                donor
            }

            val publicKeyService = PublicKeyServiceRepositoryStub()
            publicKeyService.whenFetchPublicKeys = { publicKeys }

            val donationService = DonationServiceControllerStub()
            donationService.whenFetchToken = { token }

            var capturedConsentMessage: SignedConsentMessage? = null
            var capturedDonationServicePublicKeyRegister: String? = null
            donationService.whenRegister = { delegatedConsentMessage, delegatedDonationServicePublicKey ->
                capturedConsentMessage = delegatedConsentMessage
                capturedDonationServicePublicKeyRegister = delegatedDonationServicePublicKey
            }

            val signatureService = ConsentSignatureControllerStub()

            var capturedToken: Token? = null
            var capturedConsentDocumentKey: String? = null
            var capturedDonorPublicKey: String? = null
            var capturedDonationServicePublicKeyEnableSigning: String? = null
            signatureService.whenEnableSigning =
                { delegatedToken, delegatedConsentDocumentKey, delegatedDonorPublicKey, delegatedDonationServiceKey ->
                    capturedToken = delegatedToken
                    capturedConsentDocumentKey = delegatedConsentDocumentKey
                    capturedDonorPublicKey = delegatedDonorPublicKey
                    capturedDonationServicePublicKeyEnableSigning = delegatedDonationServiceKey

                    signedConsentMessage
                }

            // When
            val result = DonationController(
                programController,
                publicKeyService,
                donorKeyStorageRepository,
                signatureService,
                donationService,
                KeyFactoryStub()
            ).register(programName)

            // Then
            assertSame(
                actual = result,
                expected = Unit
            )

            assertEquals(
                actual = capturedProgramNameFetch,
                expected = programName
            )

            assertEquals(
                actual = capturedProgramNameLoad,
                expected = programName
            )

            assertEquals(
                actual = capturedToken,
                expected = token
            )
            assertEquals(
                actual = capturedConsentDocumentKey,
                expected = program.configuration?.consentDocumentKey
            )
            assertEquals(
                actual = capturedDonorPublicKey,
                expected = DonorIdentityFixture.sampleIdentity.publicKey
            )
            assertEquals(
                actual = capturedDonationServicePublicKeyEnableSigning,
                expected = publicKeys.donationKeyBase64Encoded
            )

            assertEquals(
                actual = capturedConsentMessage,
                expected = signedConsentMessage
            )
            assertEquals(
                actual = capturedDonationServicePublicKeyRegister,
                expected = publicKeys.donationKeyBase64Encoded
            )
        }

    @Test
    fun `Given register is called with a ProgramName, it registers a user for the given program, while creating and storing DonorKeys`() =
        runBlockingTest {
            // Given
            val programName = "Potato"
            val program = ProgramFixture.sampleProgram
            val keyPair = KeyPair(
                publicKey = "PublicKey123",
                privateKey = "PrivateKey123"
            )
            val token = "Tomato"
            val publicKeys = ServicePublicKeyring(
                donationKeyBase64Encoded = "Broccoli",
                alpKeyBase64Encoded = "Onion"
            )
            val signedConsentMessage = SignedConsentMessage(
                consentMessageJSON = "Salt",
                signature = "Pepper"
            )

            val programController = ProgramControllerStub()

            var capturedProgramNameFetch: String? = null
            programController.whenFetchProgram = { delegatedProgramName ->
                capturedProgramNameFetch = delegatedProgramName

                program
            }

            val keyGenerator = KeyFactoryStub()
            keyGenerator.whenCreateKeyPair = { keyPair }

            val donorKeyStorageRepository = DonorKeyStorageRepositoryStub()

            var capturedProgramNameLoad: String? = null
            donorKeyStorageRepository.whenLoad = { delegatedProgramName ->
                capturedProgramNameLoad = delegatedProgramName

                null
            }

            var capturedNewDonor: NewDonor? = null
            donorKeyStorageRepository.whenSave = { delegatedNewDonor ->
                capturedNewDonor = delegatedNewDonor
            }

            val publicKeyService = PublicKeyServiceRepositoryStub()
            publicKeyService.whenFetchPublicKeys = { publicKeys }

            val donationService = DonationServiceControllerStub()
            donationService.whenFetchToken = { token }

            var capturedConsentMessage: SignedConsentMessage? = null
            var capturedDonationServicePublicKeyRegister: String? = null
            donationService.whenRegister = { delegatedConsentMessage, delegatedDonationServicePublicKey ->
                capturedConsentMessage = delegatedConsentMessage
                capturedDonationServicePublicKeyRegister = delegatedDonationServicePublicKey
            }

            val signatureService = ConsentSignatureControllerStub()

            var capturedToken: Token? = null
            var capturedConsentDocumentKey: String? = null
            var capturedDonorPublicKey: String? = null
            var capturedDonationServicePublicKeyEnableSigning: String? = null
            signatureService.whenEnableSigning =
                { delegatedToken, delegatedConsentDocumentKey, delegatedDonorPublicKey, delegatedDonationServiceKey ->
                    capturedToken = delegatedToken
                    capturedConsentDocumentKey = delegatedConsentDocumentKey
                    capturedDonorPublicKey = delegatedDonorPublicKey
                    capturedDonationServicePublicKeyEnableSigning = delegatedDonationServiceKey

                    signedConsentMessage
                }

            // When
            val result = DonationController(
                programController,
                publicKeyService,
                donorKeyStorageRepository,
                signatureService,
                donationService,
                keyGenerator
            ).register(programName)

            // Then
            assertSame(
                actual = result,
                expected = Unit
            )

            assertEquals(
                actual = capturedProgramNameFetch,
                expected = programName
            )

            assertEquals(
                actual = capturedProgramNameLoad,
                expected = programName
            )

            assertEquals(
                actual = capturedNewDonor,
                expected = NewDonor(
                    donorIdentity = DonorIdentityFixture.sampleIdentity.copy(
                        publicKey = keyPair.publicKey,
                        privateKey = keyPair.privateKey
                    ),
                    programName = programName
                )
            )

            assertEquals(
                actual = capturedToken,
                expected = token
            )
            assertEquals(
                actual = capturedConsentDocumentKey,
                expected = program.configuration?.consentDocumentKey
            )
            assertEquals(
                actual = capturedDonorPublicKey,
                expected = keyPair.publicKey
            )
            assertEquals(
                actual = capturedDonationServicePublicKeyEnableSigning,
                expected = publicKeys.donationKeyBase64Encoded
            )

            assertEquals(
                actual = capturedConsentMessage,
                expected = signedConsentMessage
            )
            assertEquals(
                actual = capturedDonationServicePublicKeyRegister,
                expected = publicKeys.donationKeyBase64Encoded
            )
        }
}
