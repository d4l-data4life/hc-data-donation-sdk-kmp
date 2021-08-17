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

package care.data4life.datadonation.consent.userconsent

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentApiServiceStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentRepositoryStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.session.SessionTokenRepositoryContract
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class UserConsentKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveConsentKoinModule is called it creates a Module, which contains a UserConsentRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentKoinModule(),
                module {
                    single<UserConsentContract.ApiService>(override = true) {
                        UserConsentApiServiceStub()
                    }
                }
            )
        }

        // Then
        val repo: UserConsentContract.Repository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveConsentKoinModule is called it creates a Module, which contains a ConsentErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(resolveConsentKoinModule())
        }
        // Then
        val builder: UserConsentContract.ApiService.ErrorHandler = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveConsentKoinModule is called it creates a Module, which contains a ConsentApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentKoinModule(),
                module {
                    single<Clock> { ClockStub() }
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                    single { HttpMockClientFactory.createHelloWorldMockClient() }
                    single<DataDonationSDK.Environment> { DataDonationSDK.Environment.DEV }
                }
            )
        }
        // Then
        val builder: UserConsentContract.ApiService = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveConsentKoinModule is called it creates a Module, which contains a ConsentInteractor`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentKoinModule(),
                module {
                    single<UserConsentContract.Repository>(override = true) {
                        UserConsentRepositoryStub()
                    }

                    single<SessionTokenRepositoryContract> {
                        UserSessionTokenRepositoryStub()
                    }
                }
            )
        }
        // Then
        val builder: UserConsentContract.Interactor = koin.koin.get()
        assertNotNull(builder)
    }
}
