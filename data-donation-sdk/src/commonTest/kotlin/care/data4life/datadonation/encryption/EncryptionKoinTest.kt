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

package care.data4life.datadonation.encryption

import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.mock.stub.repository.CredentialsRepositoryStub
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class EncryptionKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveEncryptionModule is called it creates a Module, which contains a HybridEncryptionRegistry`() {
        // When
        val koin = koinApplication {
            modules(
                resolveEncryptionModule(),
                module {
                    single<RepositoryContract.CredentialsRepository> {
                        CredentialsRepositoryStub()
                    } bind RepositoryContract.CredentialsRepository::class
                }
            )
        }

        // Then
        val registry: EncryptionContract.HybridEncryptionRegistry = koin.koin.get()
        assertNotNull(registry)
    }
}