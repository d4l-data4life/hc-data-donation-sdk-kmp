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

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import care.data4life.sdk.util.test.coroutine.testCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class RootKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Environment`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        assertSame(
            actual = koin.koin.get(),
            expected = env
        )
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Clock`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        val clock: Clock = koin.koin.get()
        assertNotNull(clock)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a UserSessionTokenProvider`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider: DataDonationSDK.UserSessionTokenProvider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        assertSame(
            actual = koin.koin.get(),
            expected = provider
        )
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a CoroutineContext`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        assertSame(
            actual = koin.koin.get(),
            expected = scope
        )
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a DomainErrorMapperContract object`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        val mapper: DomainErrorMapperContract = koin.koin.get()
        assertNotNull(mapper)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a D4LSDKFlowFactoryContract object`() {
        // Given
        val env = DataDonationSDK.Environment.DEV
        val provider = UserSessionTokenProviderStub()
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    provider,
                    scope
                )
            )
        }

        // Then
        val factory: D4LSDKFlowFactoryContract = koin.koin.get()
        assertNotNull(factory)
    }
}
