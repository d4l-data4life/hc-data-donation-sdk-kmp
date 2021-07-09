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

package care.data4life.datadonation.internal.domain.repository

import care.data4life.datadonation.internal.data.storage.StorageContract
import care.data4life.datadonation.mock.stub.storage.ConsentDocumentRemoteStorageStub
import care.data4life.datadonation.mock.stub.storage.CredentialsDataStorageStub
import care.data4life.datadonation.mock.stub.storage.DonationDataStorageStub
import care.data4life.datadonation.mock.stub.storage.RegistrationDataStorageStub
import care.data4life.datadonation.mock.stub.storage.ServiceTokenDataStorageStub
import care.data4life.datadonation.mock.stub.storage.UserConsentRemoteStorageStub
import care.data4life.datadonation.mock.stub.storage.UserSessionTokenDataStorageStub
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class RepositoryKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a UserConsentRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.UserConsentRemoteStorage> {
                        UserConsentRemoteStorageStub()
                    } bind StorageContract.UserConsentRemoteStorage::class

                    single<StorageContract.UserSessionTokenDataStorage> {
                        UserSessionTokenDataStorageStub()
                    } bind StorageContract.UserSessionTokenDataStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.UserConsentRepository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a RegistrationRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.RegistrationRemoteStorage> {
                        RegistrationDataStorageStub()
                    } bind StorageContract.RegistrationRemoteStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.RegistrationRepository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a ConsentDocumentRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.ConsentDocumentRemoteStorage> {
                        ConsentDocumentRemoteStorageStub()
                    } bind StorageContract.ConsentDocumentRemoteStorage::class

                    single<StorageContract.UserSessionTokenDataStorage> {
                        UserSessionTokenDataStorageStub()
                    } bind StorageContract.UserSessionTokenDataStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.ConsentDocumentRepository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a CredentialsRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.CredentialsDataStorage> {
                        CredentialsDataStorageStub()
                    } bind StorageContract.CredentialsDataStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.CredentialsRepository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a DonationRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.DonationRemoteStorage> {
                        DonationDataStorageStub()
                    } bind StorageContract.DonationRemoteStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.DonationRepository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveRepositoryModule is called it creates a Module, which contains a ServiceTokenRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveRepositoryModule(),
                module {
                    single<StorageContract.ServiceTokenRemoteStorage> {
                        ServiceTokenDataStorageStub()
                    } bind StorageContract.ServiceTokenRemoteStorage::class
                }
            )
        }

        // Then
        val repo: RepositoryContract.ServiceTokenRepository = koin.koin.get()
        assertNotNull(repo)
    }
}
