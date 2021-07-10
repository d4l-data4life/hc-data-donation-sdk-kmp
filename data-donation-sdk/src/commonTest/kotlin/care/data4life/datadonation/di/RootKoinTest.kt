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

package care.data4life.datadonation.di

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.data.storage.StorageContract
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.io.IOContract
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class RootKoinTest {
    private val config = ClientConfigurationStub()

    @BeforeTest
    fun setUp() {
        stopKoin()
        config.clear()
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module, which contains a Configuration`() {
        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: Contract.Configuration = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module, which contains a ScopeResolver`() {
        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: IOContract.ScopeProvider = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module, which contains a CredentialProvider`() {
        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: StorageContract.CredentialProvider = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module, which contains a UserSessionTokenProvider`() {
        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: StorageContract.UserSessionTokenProvider = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module which contains a Environment`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }

        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: StorageContract.UserSessionTokenProvider = koin.koin.get()
        assertNotNull(instance)
    }

    @Test
    fun `Given resolveRootModule is called with a Configuration it creates a Module which contains a Clock`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }

        // When
        val koin = koinApplication {
            modules(resolveRootModule(config))
        }
        // Then
        val instance: Clock = koin.koin.get()
        assertNotNull(instance)
    }
}
