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
import care.data4life.datadonation.donation.consentsignature.ConsentSignatureContract
import care.data4life.datadonation.donation.donationservice.DonationServiceContract
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract
import care.data4life.datadonation.donation.program.ProgramContract
import care.data4life.datadonation.donation.publickeyservice.PublicKeyServiceContract
import care.data4life.datadonation.mock.stub.crypto.KeyFactoryStub
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureControllerStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceControllerStub
import care.data4life.datadonation.mock.stub.donation.donorkeystorage.DonorKeyStorageRepositoryStub
import care.data4life.datadonation.mock.stub.donation.program.ProgramControllerStub
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceRepositoryStub
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class DonationKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveDonationKoinModule is called it creates a Module, which contains a DonationController`() {
        // When
        val koin = koinApplication {
            modules(
                resolveDonationKoinModule(),
                module {
                    single<ProgramContract.Controller> {
                        ProgramControllerStub()
                    }
                    single<DonationServiceContract.Controller> {
                        DonationServiceControllerStub()
                    }
                    single<ConsentSignatureContract.Controller> {
                        ConsentSignatureControllerStub()
                    }
                    single<PublicKeyServiceContract.Repository> {
                        PublicKeyServiceRepositoryStub()
                    }
                    single<CryptoContract.KeyFactory> {
                        KeyFactoryStub()
                    }
                    single<DonorKeyStorageRepositoryContract> {
                        DonorKeyStorageRepositoryStub()
                    }
                }
            )
        }

        // Then
        val repo: DonationContract.Controller = koin.koin.get()
        assertNotNull(repo)
    }
}
