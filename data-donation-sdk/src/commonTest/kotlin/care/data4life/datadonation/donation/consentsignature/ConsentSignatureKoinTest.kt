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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureApiServiceStub
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureErrorHandlerStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.Networking
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ConsentSignatureKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveConsentSignatureKoinModule is called it creates a Module, which contains a ConsentSignature ErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentSignatureKoinModule(),
            )
        }

        // Then
        val repo: ConsentSignatureContract.ApiService.ErrorHandler = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveConsentSignatureKoinModule is called it creates a Module, which contains a ConsentSignature ApiService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentSignatureKoinModule(),
                module {
                    single<ConsentSignatureContract.ApiService.ErrorHandler>(override = true) {
                        ConsentSignatureErrorHandlerStub()
                    }
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                }
            )
        }

        // Then
        val repo: ConsentSignatureContract.ApiService = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveConsentSignatureKoinModule is called it creates a Module, which contains a ConsentSignature Repository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveConsentSignatureKoinModule(),
                module {
                    single<ConsentSignatureContract.ApiService>(override = true) {
                        ConsentSignatureApiServiceStub()
                    }
                }
            )
        }

        // Then
        val repo: ConsentSignatureContract.Repository = koin.koin.get()
        assertNotNull(repo)
    }
}
