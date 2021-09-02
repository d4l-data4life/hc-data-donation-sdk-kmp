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

import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.mock.stub.crypto.CryptoServiceStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceApiServiceStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceErrorHandlerStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceRepositoryStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.Networking
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class DonationServiceKoinTest {
    @Test
    fun `Given resolveDonationServiceKoinModule is called with its appropriate parameter it creates a Module, which contains a DonationServiceErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(
                resolveDonationServiceKoinModule()
            )
        }

        // Then
        val handler: DonationServiceContract.ApiService.ErrorHandler = koin.koin.get()
        assertNotNull(handler)
    }

    @Test
    fun `Given resolveDonationServiceKoinModule is called with its appropriate parameter it creates a Module, which contains a DonationServiceApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveDonationServiceKoinModule(),
                module {
                    single<DonationServiceContract.ApiService.ErrorHandler>(override = true) {
                        DonationServiceErrorHandlerStub()
                    }
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                }
            )
        }

        // Then
        val apiService: DonationServiceContract.ApiService = koin.koin.get()
        assertNotNull(apiService)
    }

    @Test
    fun `Given resolveDonationServiceKoinModule is called with its appropriate parameter it creates a Module, which contains a DonationServiceRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveDonationServiceKoinModule(),
                module {
                    single<DonationServiceContract.ApiService>(override = true) {
                        DonationServiceApiServiceStub()
                    }
                }
            )
        }

        // Then
        val repo: DonationServiceContract.Repository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveDonationServiceKoinModule is called with its appropriate parameter it creates a Module, which contains a DonationServiceController`() {
        // When
        val koin = koinApplication {
            modules(
                resolveDonationServiceKoinModule(),
                module {
                    single<DonationServiceContract.Repository>(override = true) {
                        DonationServiceRepositoryStub()
                    }
                    single<CryptoContract.Service> {
                        CryptoServiceStub()
                    }
                    single<Json>(named("DataDonationSerializer")) { Json }
                }
            )
        }

        // Then
        val controller: DonationServiceContract.Controller = koin.koin.get()
        assertNotNull(controller)
    }
}
