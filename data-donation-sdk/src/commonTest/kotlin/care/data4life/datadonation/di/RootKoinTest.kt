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

import care.data4life.datadonation.DataDonationSDKPublicAPI
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class RootKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Environment`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val builder: DataDonationSDKPublicAPI.Environment = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Clock`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val builder: Clock = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a UserSessionTokenProvider, which is frozen`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val item: DataDonationSDKPublicAPI.UserSessionTokenProvider = koin.koin.get()
        assertNotNull(item)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a CoroutineContext`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val builder: CoroutineScope = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a DomainErrorMapperContract object`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val builder: DomainErrorMapperContract = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a D4LSDKFlowFactoryContract object`() {
        // Given
        val env = DataDonationSDKPublicAPI.Environment.DEV
        val provider = UserSessionTokenProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider
                )
            )
        }
        // Then
        val builder: D4LSDKFlowFactoryContract = koin.koin.get()
        assertNotNull(builder)
    }
}
