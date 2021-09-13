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

package care.data4life.datadonation.session

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.sdk.util.test.coroutine.testCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class SessionKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveSessionKoinModule is called it creates a Module, which contains a UserSessionTokenRepository`() {
        // When
        val koin = koinApplication {
            modules(
                resolveSessionKoinModule(),
                module {
                    single<Clock> { ClockStub() }
                    single<DataDonationSDK.UserSessionTokenProvider> {
                        UserSessionTokenProviderStub()
                    }
                    single<CoroutineScope> { CoroutineScope(testCoroutineContext) }
                }
            )
        }
        // Then
        val builder: SessionTokenRepositoryContract = koin.koin.get()
        assertNotNull(builder)
    }
}
