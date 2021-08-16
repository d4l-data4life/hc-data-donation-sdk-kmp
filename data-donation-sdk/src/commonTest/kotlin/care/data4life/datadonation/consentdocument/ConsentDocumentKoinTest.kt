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

package care.data4life.datadonation.consentdocument

import care.data4life.datadonation.DataDonationSDKPublicAPI
import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.consentdocument.ConsentDocumentApiServiceStub
import care.data4life.datadonation.mock.stub.consentdocument.ConsentDocumentRepositoryStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.mock.stub.service.UserSessionTokenServiceStub
import care.data4life.datadonation.networking.Networking
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ConsentDocumentKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveConsentDocumentKoinModule is called it creates a Module, which contains a ConsentDocumentRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentDocumentKoinModule(),
                module {
                    single<ServiceContract.UserSessionTokenService> {
                        UserSessionTokenServiceStub()
                    }

                    single<ConsentDocumentContract.ApiService>(override = true) {
                        ConsentDocumentApiServiceStub()
                    }
                }
            )
        }

        // Then
        val repo: ConsentDocumentContract.Repository = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveConsentDocumentKoinModule is called it creates a Module, which contains a ConsentDocumentErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(resolveConsentDocumentKoinModule())
        }
        // Then
        val builder: ConsentDocumentContract.ApiService.ErrorHandler = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveConsentDocumentKoinModule is called it creates a Module, which contains a ConsentDocumentApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentDocumentKoinModule(),
                module {
                    single<Clock> { ClockStub() }
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                    single { HttpMockClientFactory.createHelloWorldMockClient() }
                    single<DataDonationSDKPublicAPI.Environment> { DataDonationSDKPublicAPI.Environment.DEV }
                }
            )
        }
        // Then
        val builder: ConsentDocumentContract.ApiService = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveConsentDocumentKoinModule is called it creates a Module, which contains a ConsentDocumentInteractor`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentDocumentKoinModule(),
                module {
                    single<ConsentDocumentContract.Repository>(override = true) {
                        ConsentDocumentRepositoryStub()
                    }
                }
            )
        }
        // Then
        val builder: ConsentDocumentContract.Interactor = koin.koin.get()
        assertNotNull(builder)
    }
}
