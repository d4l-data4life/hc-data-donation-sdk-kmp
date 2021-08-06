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

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.DataDonationSDKPublicAPI
import care.data4life.datadonation.DataDonationSDKPublicAPI.Environment
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.datadonation.mock.stub.service.networking.RequestBuilderSpy
import care.data4life.sdk.util.coroutine.CoroutineHelper
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createHelloWorldMockClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ServiceKoinTest {
    private val testScope = CoroutineHelper.createCoroutineScope("test2Scope")

    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a ConsentErrorHandler`() {
        // When
        val koin = koinApplication {
            modules(resolveServiceModule())
        }
        // Then
        val builder: ServiceContract.ConsentService.ConsentErrorHandler = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a ConsentService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
                module {
                    single<Clock> { ClockStub() }
                    single<Networking.RequestBuilderFactory> {
                        RequestBuilderSpy.Factory()
                    }
                    single { createHelloWorldMockClient() }
                    single<Environment> { Environment.DEV }
                }
            )
        }
        // Then
        val builder: ServiceContract.ConsentService = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a UserSessionTokenService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
                module {
                    single<Clock> { ClockStub() }
                    single<DataDonationSDKPublicAPI.UserSessionTokenProvider> {
                        UserSessionTokenProviderStub()
                    }
                    single<CoroutineScope> { testScope }
                }
            )
        }
        // Then
        val builder: ServiceContract.UserSessionTokenService = koin.koin.get()
        assertNotNull(builder)
    }
}
