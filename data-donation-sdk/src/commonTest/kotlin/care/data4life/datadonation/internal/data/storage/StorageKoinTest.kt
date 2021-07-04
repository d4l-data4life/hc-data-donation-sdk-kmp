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

package care.data4life.datadonation.internal.data.storage

import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.mock.stub.ConsentServiceStub
import care.data4life.datadonation.mock.stub.DonationServiceStub
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class StorageKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given storageModule is called with it creates a Module, which contains a UserConsentRemoteStorage`() {
        // When
        val koin = koinApplication {
            modules(
                storageModule(),
                module {
                    single<ServiceContract.ConsentService> {
                        ConsentServiceStub()
                    } bind ServiceContract.ConsentService::class
                }
            )
        }
        // Then
        val store: StorageContract.UserConsentRemoteStorage = koin.koin.get()
        assertNotNull(store)
    }

    @Test
    fun `Given storageModule is called with it creates a Module, which contains a RegistrationRepositoryStorage`() {
        // When
        val koin = koinApplication {
            modules(
                storageModule(),
                module {
                    single<ServiceContract.DonationService> {
                        DonationServiceStub()
                    } bind ServiceContract.DonationService::class
                }
            )
        }
        // Then
        val store: StorageContract.RegistrationRemoteStorage = koin.koin.get()
        assertNotNull(store)
    }

    @Test
    fun `Given storageModule is called with it creates a Module, which contains a ConsentDocumentRemoteStorage`() {
        // When
        val koin = koinApplication {
            modules(
                storageModule(),
                module {
                    single<ServiceContract.ConsentService> {
                        ConsentServiceStub()
                    } bind ServiceContract.ConsentService::class
                }
            )
        }
        // Then
        val store: StorageContract.ConsentDocumentRemoteStorage = koin.koin.get()
        assertNotNull(store)
    }

    @Test
    fun `Given storageModule is called with it creates a Module, which contains a DonationRepositoryRemoteStorage`() {
        // When
        val koin = koinApplication {
            modules(
                storageModule(),
                module {
                    single<ServiceContract.DonationService> {
                        DonationServiceStub()
                    } bind ServiceContract.DonationService::class
                }
            )
        }
        // Then
        val store: StorageContract.DonationRemoteStorage = koin.koin.get()
        assertNotNull(store)
    }

    @Test
    fun `Given storageModule is called with it creates a Module, which contains a ServiceTokenRepositoryRemoteStorage`() {
        // When
        val koin = koinApplication {
            modules(
                storageModule(),
                module {
                    single<ServiceContract.DonationService> {
                        DonationServiceStub()
                    } bind ServiceContract.DonationService::class
                }
            )
        }
        // Then
        val store: StorageContract.ServiceTokenRemoteStorage = koin.koin.get()
        assertNotNull(store)
    }
}
