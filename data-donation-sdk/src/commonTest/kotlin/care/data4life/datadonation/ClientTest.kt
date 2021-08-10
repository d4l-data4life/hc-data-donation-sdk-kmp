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

import care.data4life.datadonation.DataDonationSDKPublicAPI.Environment
import care.data4life.datadonation.internal.domain.usecases.CreateUserConsent
import care.data4life.datadonation.internal.domain.usecases.FetchConsentDocuments
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsents
import care.data4life.datadonation.internal.domain.usecases.RevokeUserConsent
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.lang.DataDonationFlowErrorMapper
import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleConsentDocument
import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleUserConsent
import care.data4life.datadonation.mock.spy.D4LFlowFactorySpy
import care.data4life.datadonation.mock.stub.CreateUserConsentStub
import care.data4life.datadonation.mock.stub.FetchConsentDocumentsStub
import care.data4life.datadonation.mock.stub.FetchUserConsentsStub
import care.data4life.datadonation.mock.stub.RevokeUserConsentStub
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.sdk.util.coroutine.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import care.data4life.sdk.util.test.coroutine.testCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ClientTest {
    private var flowSpy = D4LFlowFactorySpy()

    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `It fulfils DataDonationFactory`() {
        val factory: Any = Client

        assertTrue(factory is DataDonationSDKPublicAPI.DataDonationClientFactory)
    }

    @Test
    fun `Given getInstance is called with a Configuration it returns a DataDonation`() {
        val client: Any = Client.getInstance(
            Environment.DEV,
            UserSessionTokenProviderStub()
        )

        assertTrue(client is DataDonationSDKPublicAPI.DataDonationClient)
    }

    @Test
    fun `Given createUserConsent is called with a ConsentDocumentVersion and a Language it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = CreateUserConsentStub()
        val consent = sampleUserConsent
        val scope = CoroutineScope(testCoroutineContext)

        val version = "23"
        val consentDocumentKey = "custom-consent-key"

        val capturedParameter = Channel<UsecaseContract.CreateUserConsent.Parameter>()

        usecase.whenExecute = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
            consent
        }

        val di = koinApplication {
            modules(
                module {
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<UsecaseContract.CreateUserConsent> {
                        usecase
                    }
                    single<UsecaseContract.FetchConsentDocuments> {
                        FetchConsentDocumentsStub()
                    }
                    single<UsecaseContract.FetchUserConsents> {
                        FetchUserConsentsStub()
                    }
                    single<UsecaseContract.RevokeUserConsent> {
                        RevokeUserConsentStub()
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.createUserConsent(
            consentDocumentKey,
            version
        ).ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = consent
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = CreateUserConsent.Parameter(
                consentDocumentKey = consentDocumentKey,
                version = version
            )
        )

        assertSame(
            actual = flowSpy.capturedErrorMapper,
            expected = DataDonationFlowErrorMapper
        )
        assertSame(
            actual = flowSpy.capturedScope,
            expected = scope
        )
    }

    @Test
    fun `Given fetchConsentDocuments is called with a consentDocumentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of ConsentDocument`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchConsentDocumentsStub()
        val documents = listOf(sampleConsentDocument)
        val scope = CoroutineScope(testCoroutineContext)

        val version = "23"
        val language = "de-j-old-n-kotlin-x-done"
        val consentDocumentKey = "abc"

        val capturedParameter = Channel<UsecaseContract.FetchConsentDocuments.Parameter>()

        usecase.whenExecute = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
            documents
        }

        val di = koinApplication {
            modules(
                module {
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<UsecaseContract.FetchConsentDocuments> {
                        usecase
                    }
                    single<UsecaseContract.CreateUserConsent> {
                        CreateUserConsentStub()
                    }
                    single<UsecaseContract.FetchUserConsents> {
                        FetchUserConsentsStub()
                    }
                    single<UsecaseContract.RevokeUserConsent> {
                        RevokeUserConsentStub()
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.fetchConsentDocuments(
            consentDocumentKey,
            version,
            language,
        ).ktFlow.collect { result ->
            // Then
            assertSame(
                expected = documents,
                actual = result
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = FetchConsentDocuments.Parameter(
                version = version,
                language = language,
                consentDocumentKey = consentDocumentKey
            )
        )

        assertSame(
            actual = flowSpy.capturedErrorMapper,
            expected = DataDonationFlowErrorMapper
        )
        assertSame(
            actual = flowSpy.capturedScope,
            expected = scope
        )
    }

    @Test
    fun `Given fetchUserConsents is called with a consentDocumentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchUserConsentsStub()
        val consents = listOf(sampleUserConsent)
        val scope = CoroutineScope(testCoroutineContext)

        val capturedParameter = Channel<UsecaseContract.FetchUserConsents.Parameter>()

        usecase.whenExecute = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
            consents
        }

        val di = koinApplication {
            modules(
                module {
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<UsecaseContract.FetchUserConsents> {
                        usecase
                    }
                    single<UsecaseContract.FetchConsentDocuments> {
                        FetchConsentDocumentsStub()
                    }
                    single<UsecaseContract.CreateUserConsent> {
                        CreateUserConsentStub()
                    }
                    single<UsecaseContract.RevokeUserConsent> {
                        RevokeUserConsentStub()
                    }
                }
            )
        }

        val consentDocumentKey = "key"
        val client = Client(di)

        // When
        client.fetchUserConsents(
            consentDocumentKey
        ).ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = consents
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = FetchUserConsents.Parameter(
                consentDocumentKey = consentDocumentKey
            )
        )

        assertSame(
            actual = flowSpy.capturedErrorMapper,
            expected = DataDonationFlowErrorMapper
        )
        assertSame(
            actual = flowSpy.capturedScope,
            expected = scope
        )
    }

    @Test
    fun `Given fetchAllUserConsents is called it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchUserConsentsStub()
        val consents = listOf(sampleUserConsent)
        val scope = CoroutineScope(testCoroutineContext)

        val capturedParameter = Channel<UsecaseContract.FetchUserConsents.Parameter>()

        usecase.whenExecute = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
            consents
        }

        val di = koinApplication {
            modules(
                module {
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<UsecaseContract.FetchUserConsents> {
                        usecase
                    }
                    single<UsecaseContract.FetchConsentDocuments> {
                        FetchConsentDocumentsStub()
                    }
                    single<UsecaseContract.CreateUserConsent> {
                        CreateUserConsentStub()
                    }
                    single<UsecaseContract.RevokeUserConsent> {
                        RevokeUserConsentStub()
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.fetchAllUserConsents().ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = consents
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = FetchUserConsents.Parameter()
        )

        assertSame(
            actual = flowSpy.capturedErrorMapper,
            expected = DataDonationFlowErrorMapper
        )
        assertSame(
            actual = flowSpy.capturedScope,
            expected = scope
        )
    }

    @Test
    fun `Given revokeUserConsent is called with a consentDocumentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which just runs`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = RevokeUserConsentStub()
        val scope = CoroutineScope(testCoroutineContext)

        val consentDocumentKey = "custom-consent-key"

        val capturedParameter = Channel<UsecaseContract.RevokeUserConsent.Parameter>()

        usecase.whenExecute = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
            Unit
        }

        val di = koinApplication {
            modules(
                module {
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<UsecaseContract.RevokeUserConsent> {
                        usecase
                    }
                    single<UsecaseContract.FetchConsentDocuments> {
                        FetchConsentDocumentsStub()
                    }
                    single<UsecaseContract.CreateUserConsent> {
                        CreateUserConsentStub()
                    }
                    single<UsecaseContract.FetchUserConsents> {
                        FetchUserConsentsStub()
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.revokeUserConsent(consentDocumentKey).ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = Unit
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = RevokeUserConsent.Parameter(
                consentDocumentKey = consentDocumentKey,
            )
        )

        assertSame(
            actual = flowSpy.capturedErrorMapper,
            expected = DataDonationFlowErrorMapper
        )
        assertSame(
            actual = flowSpy.capturedScope,
            expected = scope
        )
    }
}
