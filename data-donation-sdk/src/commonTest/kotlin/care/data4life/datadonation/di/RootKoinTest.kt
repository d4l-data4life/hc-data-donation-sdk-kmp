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
import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.mock.stub.donation.donorkeystorage.DonorKeyStorageProviderStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenProviderStub
import care.data4life.datadonation.util.JsonConfiguratorContract
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
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
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val builder: DataDonationSDK.Environment = koin.koin.get()
        assertNotNull(builder)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Clock`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
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
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val provider: DataDonationSDK.UserSessionTokenProvider = koin.koin.get()
        assertNotNull(provider)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a CoroutineContext`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val scope: CoroutineScope = koin.koin.get()
        assertNotNull(scope)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a DomainErrorMapperContract object`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
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
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val factory: D4LSDKFlowFactoryContract = koin.koin.get()
        assertNotNull(factory)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a DonorKeyStorageProvider`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val provider: DataDonationSDK.DonorKeyStorageProvider = koin.koin.get()
        assertNotNull(provider)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a CryptoService`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val crypto: CryptoContract.Service = koin.koin.get()
        assertNotNull(crypto)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a CryptoKeyFactory`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val factory: CryptoContract.KeyFactory = koin.koin.get()
        assertNotNull(factory)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a JsonConfigurator`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val configurator: JsonConfiguratorContract = koin.koin.get(named("DataDonationSerializerConfigurator"))
        assertNotNull(configurator)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, which contains a Json DataDonationSerializer`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                )
            )
        }
        // Then
        val serializer: Json = koin.koin.get(named("DataDonationSerializer"))
        assertNotNull(serializer)
    }

    @Test
    fun `Given resolveRootModule is called with its appropriate parameter it creates a Module, it invokes the JsonConfigurator to build the Serializer`() {
        // Given
        val env = DataDonationSDK.Environment.DEVELOPMENT
        val sessionProvider = UserSessionTokenProviderStub()
        val keyStorageProvider = DonorKeyStorageProviderStub()

        val expected = SerializersModule { }

        // When
        val koin = koinApplication {
            modules(
                resolveRootModule(
                    env,
                    sessionProvider,
                    keyStorageProvider
                ),
                module {
                    single(
                        override = true,
                        qualifier = named("DataDonationSerializerConfigurator")
                    ) {
                        JsonConfiguratorContract { jsonBuilder ->
                            jsonBuilder.serializersModule = expected
                            jsonBuilder
                        }
                    }
                }
            )
        }
        // Then
        val serializer: Json = koin.koin.get(named("DataDonationSerializer"))

        assertSame(
            actual = serializer.serializersModule,
            expected = expected
        )
    }
}
