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
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceApiServiceStub
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceKeyMapperStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.Networking
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class PublicKeyServiceKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolvePublicKeyServiceKoinModule is called it creates a Module, which contains a PublicKeyServiceErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(
                resolvePublicKeyServiceKoinModule()
            )
        }

        // Then
        val repo: PublicKeyServiceContract.ApiService.ErrorHandler = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolvePublicKeyServiceKoinModule is called it creates a Module, which contains a PublicKeyServiceApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolvePublicKeyServiceKoinModule(),
                module {
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                }
            )
        }

        // Then
        val repo: PublicKeyServiceContract.ApiService = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolvePublicKeyServiceKoinModule is called it creates a Module, which contains a PublicKeyServiceKeyMapper`() {
        // When
        val koin = koinApplication {
            modules(
                resolvePublicKeyServiceKoinModule(),
                module {
                    single<DataDonationSDK.Environment> {
                        DataDonationSDK.Environment.DEVELOPMENT
                    }
                }
            )
        }

        // Then
        val repo: PublicKeyServiceContract.Repository.KeyMapper = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolvePublicKeyServiceKoinModule is called it creates a Module, which contains a PublicKeyServiceRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolvePublicKeyServiceKoinModule(),
                module {
                    single<PublicKeyServiceContract.ApiService>(override = true) {
                        PublicKeyServiceApiServiceStub()
                    }

                    single<PublicKeyServiceContract.Repository.KeyMapper>(override = true) {
                        PublicKeyServiceKeyMapperStub()
                    }
                }
            )
        }

        // Then
        val repo: PublicKeyServiceContract.Repository = koin.koin.get()
        assertNotNull(repo)
    }
}
