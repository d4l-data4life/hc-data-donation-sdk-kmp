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

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.io.IOContract
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import care.data4life.datadonation.mock.stub.ClockStub
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ServiceKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a CallBuilderFactory`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
            )
        }
        // Then
        val builder: ServiceContract.CallBuilderFactory = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a ConsentService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
                module {
                    single { ClockStub() } bind Clock::class
                    single { HttpClient() }
                    single { Environment.LOCAL } bind Environment::class
                }
            )
        }
        // Then
        val builder: ServiceContract.ConsentService = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a CredentialService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
                module {
                    single { ClockStub() } bind Clock::class
                    single {
                        ClientConfigurationStub()
                    } binds arrayOf(
                        Contract.Configuration::class,
                        IOContract.ScopeProvider::class,
                        IOContract.CredentialProvider::class,
                        IOContract.UserSessionTokenProvider::class
                    )
                }
            )
        }
        // Then
        val builder: ServiceContract.CredentialService = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveServiceModule is called it creates a Module, which contains a UserSessionTokenService`() {
        // When
        val koin = koinApplication {
            modules(
                resolveServiceModule(),
                module {
                    single { ClockStub() } bind Clock::class
                    single {
                        ClientConfigurationStub()
                    } binds arrayOf(
                        Contract.Configuration::class,
                        IOContract.ScopeProvider::class,
                        IOContract.CredentialProvider::class,
                        IOContract.UserSessionTokenProvider::class
                    )
                }
            )
        }
        // Then
        val builder: ServiceContract.UserSessionTokenService = koin.koin.get()
        assertNotNull(builder)
    }
}
