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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.encryption.EncryptionContract
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.mock.stub.ConsentDocumentRepositoryStub
import care.data4life.datadonation.mock.stub.DonationRepositoryStub
import care.data4life.datadonation.mock.stub.HybridEncryptionRegistryStub
import care.data4life.datadonation.mock.stub.HybridEncryptionStub
import care.data4life.datadonation.mock.stub.RegistrationRepositoryStub
import care.data4life.datadonation.mock.stub.ServiceTokenRepositoryStub
import care.data4life.datadonation.mock.stub.UserConsentRepositoryStub
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertNotNull

class UsecaseKoinTest {
    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains CreateRequestConsentPayload`() {
        // Given
        val hybridEncryptionRegistry = HybridEncryptionRegistryStub()
        hybridEncryptionRegistry.givenHybridEncryptionDD = HybridEncryptionStub()

        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<RepositoryContract.ServiceTokenRepository> {
                        ServiceTokenRepositoryStub()
                    } bind RepositoryContract.ServiceTokenRepository::class
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                    single<EncryptionContract.HybridEncryptionRegistry> {
                        hybridEncryptionRegistry
                    } bind EncryptionContract.HybridEncryptionRegistry::class
                }
            )
        }
        // Then
        val instance: CreateRequestConsentPayload = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains DonateResources`() {
        // Given
        val hybridEncryptionRegistry = HybridEncryptionRegistryStub()
        hybridEncryptionRegistry.givenHybridEncryptionALP = HybridEncryptionStub()
        hybridEncryptionRegistry.givenHybridEncryptionDD = HybridEncryptionStub()

        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<EncryptionContract.HybridEncryptionRegistry> {
                        hybridEncryptionRegistry
                    } bind EncryptionContract.HybridEncryptionRegistry::class
                    single<RepositoryContract.DonationRepository> {
                        DonationRepositoryStub()
                    } bind RepositoryContract::class
                    single<RepositoryContract.ServiceTokenRepository> {
                        ServiceTokenRepositoryStub()
                    } bind RepositoryContract.ServiceTokenRepository::class
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                }
            )
        }
        // Then
        val instance: DonateResources = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains FilterSensitiveInformation`() {
        // When
        val koin = koinApplication {
            modules(usecaseModule())
        }
        // Then
        val instance: FilterSensitiveInformation = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains RegisterNewDonor`() {
        // Given
        val hybridEncryptionRegistry = HybridEncryptionRegistryStub()
        hybridEncryptionRegistry.givenHybridEncryptionDD = HybridEncryptionStub()

        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<EncryptionContract.HybridEncryptionRegistry> {
                        hybridEncryptionRegistry
                    } bind EncryptionContract.HybridEncryptionRegistry::class
                    single<RepositoryContract.DonationRepository> {
                        DonationRepositoryStub()
                    } bind RepositoryContract::class
                    single<RepositoryContract.ServiceTokenRepository> {
                        ServiceTokenRepositoryStub()
                    } bind RepositoryContract.ServiceTokenRepository::class
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                    single<RepositoryContract.RegistrationRepository> {
                        RegistrationRepositoryStub()
                    } bind RepositoryContract.RegistrationRepository::class
                }
            )
        }
        // Then
        val instance: RegisterNewDonor = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains FetchConsentDocuments`() {
        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<RepositoryContract.ConsentDocumentRepository> {
                        ConsentDocumentRepositoryStub()
                    } bind RepositoryContract.ConsentDocumentRepository::class
                }
            )
        }
        // Then
        val instance: UsecaseContract.FetchConsentDocuments = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains CreateUserConsent`() {
        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                }
            )
        }
        // Then
        val instance: CreateUserConsent = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains FetchUserConsents`() {
        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                }
            )
        }
        // Then
        val instance: UsecaseContract.FetchUserConsents = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains RemoveInternalInformation`() {
        // When
        val koin = koinApplication {
            modules(usecaseModule())
        }
        // Then
        val instance: RemoveInternalInformation = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given usecaseModule is called with it creates a Module, which contains RevokeUserConsent`() {
        // When
        val koin = koinApplication {
            modules(
                usecaseModule(),
                module {
                    single<RepositoryContract.UserConsentRepository> {
                        UserConsentRepositoryStub()
                    } bind RepositoryContract.UserConsentRepository::class
                }
            )
        }
        // Then
        val instance: RevokeUserConsent = koin.koin.get()
        assertNotNull(instance)
    }
}
