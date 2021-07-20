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

package care.data4life.datadonation

import care.data4life.datadonation.core.listener.ListenerContract
import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.domain.usecases.CreateUserConsent
import care.data4life.datadonation.internal.domain.usecases.FetchConsentDocuments
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsents
import care.data4life.datadonation.internal.domain.usecases.RevokeUserConsent
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.runner.UsecaseRunnerContract
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.stub.CallbackStub
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import care.data4life.datadonation.mock.stub.CreateUserConsentStub
import care.data4life.datadonation.mock.stub.FetchConsentDocumentsStub
import care.data4life.datadonation.mock.stub.FetchUserConsentStub
import care.data4life.datadonation.mock.stub.ResultListenerStub
import care.data4life.datadonation.mock.stub.RevokeUserConsentStub
import care.data4life.datadonation.mock.stub.UsecaseRunnerStub
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClientTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `It fulfils DataDonationFactory`() {
        val factory: Any = Client

        assertTrue(factory is Contract.DataDonationFactory)
    }

    @Test
    fun `Given getInstance is called with a Configuration it returns a DataDonation`() {
        val client: Any = Client.getInstance(ClientConfigurationStub())

        assertTrue(client is Contract.DataDonation)
    }

    @Test
    fun `Given fetchAllUserConsents is called with a ResultListener it resolves the Usecase with its default Parameter and delegates them to the UsecaseRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<UserConsent>>()
        val usecase = FetchUserConsentStub()

        var capturedParameter: UsecaseContract.FetchUserConsents.Parameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: UsecaseContract.Usecase<*, *>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                module {
                    single {
                        usecase
                    } bind UsecaseContract.FetchUserConsents::class

                    single {
                        UsecaseRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase, delegatedParameter ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                                capturedParameter = delegatedParameter as UsecaseContract.FetchUserConsents.Parameter
                            }
                        }
                    } bind UsecaseRunnerContract::class
                }
            )
        }

        val client = Client(config, di)

        // When
        client.fetchAllUserConsents(listener)

        // Then
        assertEquals(
            actual = capturedParameter,
            expected = FetchUserConsents.Parameter()
        )
        assertSame(
            actual = capturedListener,
            expected = listener
        )
        assertSame(
            actual = capturedUsecase,
            expected = usecase
        )
    }

    @Test
    fun `Given fetchUserConsents is called with a ConsentKey and with a ResultListener  it resolves the Usecase with wraps the given Parameter and delegates that to the UsecaseRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<UserConsent>>()
        val usecase = FetchUserConsentStub()

        var capturedParameter: UsecaseContract.FetchUserConsents.Parameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: UsecaseContract.Usecase<*, *>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                module {
                    single {
                        usecase
                    } bind UsecaseContract.FetchUserConsents::class

                    single {
                        UsecaseRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase, delegatedParameter ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                                capturedParameter = delegatedParameter as UsecaseContract.FetchUserConsents.Parameter
                            }
                        }
                    } bind UsecaseRunnerContract::class
                }
            )
        }

        val consentKey = "key"
        val client = Client(config, di)

        // When
        client.fetchUserConsents(consentKey, listener)

        // Then
        assertEquals(
            actual = capturedParameter,
            expected = FetchUserConsents.Parameter(consentKey)
        )
        assertSame(
            actual = capturedListener,
            expected = listener
        )
        assertSame(
            actual = capturedUsecase,
            expected = usecase
        )
    }

    @Test
    fun `Given fetchConsentDocuments is called with a ConsentKey and with a ResultListener it resolves the Usecase with wraps the given Parameter and delegates that to the UsecaseRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<ConsentDocument>>()
        val usecase = FetchConsentDocumentsStub()

        val version = 23
        val language = "de-j-old-n-kotlin-x-done"
        val consentKey = "abc"

        var capturedParameter: UsecaseContract.FetchConsentDocuments.Parameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: UsecaseContract.Usecase<*, *>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                module {
                    single<UsecaseContract.FetchConsentDocuments> {
                        usecase
                    }

                    single {
                        UsecaseRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase, delegatedParameter ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                                capturedParameter = delegatedParameter as UsecaseContract.FetchConsentDocuments.Parameter
                            }
                        }
                    } bind UsecaseRunnerContract::class
                }
            )
        }

        val client = Client(config, di)

        // When
        val result = client.fetchConsentDocuments(
            version,
            language,
            consentKey,
            listener
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedParameter,
            expected = FetchConsentDocuments.Parameter(
                version = version,
                language = language,
                consentKey = consentKey
            )
        )
        assertSame(
            actual = capturedListener,
            expected = listener
        )
        assertSame(
            actual = capturedUsecase,
            expected = usecase
        )
    }

    @Test
    fun `Given revokeUserConsent is called with a ConsentKey and with a Callback it resolves the Usecase with wraps the given Parameter and delegates that to the TaskRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = CallbackStub()
        val usecase = RevokeUserConsentStub()

        val consentKey = "custom-consent-key"

        var capturedParameter: UsecaseContract.RevokeUserConsent.Parameter? = null
        var capturedListener: ListenerContract.Callback? = null
        var capturedUsecase: UsecaseContract.Usecase<*, *>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                module {
                    single {
                        usecase
                    } bind UsecaseContract.RevokeUserConsent::class

                    single {
                        UsecaseRunnerStub().also {
                            it.whenRunCallback = { delegatedResultListener, delegatedUsecase, delegatedParameter ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                                capturedParameter = delegatedParameter as UsecaseContract.RevokeUserConsent.Parameter
                            }
                        }
                    } bind UsecaseRunnerContract::class
                }
            )
        }

        val client = Client(config, di)

        // When
        val result = client.revokeUserConsent(
            consentKey,
            listener
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedParameter,
            expected = RevokeUserConsent.Parameter(
                consentKey = consentKey,
            )
        )
        assertSame(
            actual = capturedListener,
            expected = listener
        )
        assertSame(
            actual = capturedUsecase,
            expected = usecase
        )
    }

    @Test
    fun `Given createUserConsent is called with a ConsentDocumentVersion, a Language and with a ResultListener it resolves the Usecase with wraps the given Parameter and delegates that to the TaskRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<UserConsent>()
        val usecase = CreateUserConsentStub()

        val version = 23
        val consentKey = "custom-consent-key"
        val keyPair = DummyData.keyPair

        var capturedParameter: UsecaseContract.CreateUserConsent.Parameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: UsecaseContract.Usecase<*, *>? = null

        config.whenGetEnvironment = { Environment.LOCAL }
        config.whenGetDonorKeyPair = { keyPair }

        val di = koinApplication {
            modules(
                module {
                    single {
                        usecase
                    } bind UsecaseContract.CreateUserConsent::class

                    single {
                        UsecaseRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase, delegatedParameter ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                                capturedParameter = delegatedParameter as UsecaseContract.CreateUserConsent.Parameter
                            }
                        }
                    } bind UsecaseRunnerContract::class
                }
            )
        }

        val client = Client(config, di)

        // When
        client.createUserConsent(
            consentKey,
            version,
            listener
        )

        // Then
        assertEquals(
            actual = capturedParameter,
            expected = CreateUserConsent.Parameter(
                consentKey = consentKey,
                version = version,
                keyPair = keyPair
            )
        )
        assertSame(
            actual = capturedListener,
            expected = listener
        )
        assertSame(
            actual = capturedUsecase,
            expected = usecase
        )
    }
}
