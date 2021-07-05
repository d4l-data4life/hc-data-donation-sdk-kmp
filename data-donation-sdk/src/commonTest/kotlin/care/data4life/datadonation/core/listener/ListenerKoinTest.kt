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

package care.data4life.datadonation.core.listener

import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ListenerKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveListenerModule is called with a ScopeResolver it creates a Module, which contains a TaskRunner`() {
        // Given
        val config = ClientConfigurationStub()

        // When
        val koin = koinApplication {
            modules(
                resolveListenerModule(),
                module {
                    single { config } bind ListenerContract.ScopeResolver::class
                }
            )
        }
        // Then
        val runner: ListenerContract.TaskRunner = koin.koin.get()
        assertNotNull(runner)
    }
}
