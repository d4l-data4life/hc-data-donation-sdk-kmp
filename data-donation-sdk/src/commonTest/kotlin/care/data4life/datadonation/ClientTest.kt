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

import care.data4life.datadonation.DataDonationSDK.Environment
import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract
import care.data4life.datadonation.consent.userconsent.UserConsentContract
import care.data4life.datadonation.error.DataDonationFlowErrorMapper
import care.data4life.datadonation.mock.fixture.ConsentDocumentFixture.sampleConsentDocument
import care.data4life.datadonation.mock.fixture.UserConsentFixture.sampleUserConsent
import care.data4life.datadonation.mock.spy.D4LFlowFactorySpy
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentControllerStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentControllerStub
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
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
import kotlin.test.assertNull
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

        assertTrue(factory is DataDonationSDK.DataDonationClientFactory)
    }

    @Test
    fun `Given getInstance is called with a Configuration it returns a DataDonation`() {
        val client: Any = Client.getInstance(
            Environment.DEVELOPMENT,
            UserSessionTokenProviderStub()
        )

        assertTrue(client is DataDonationSDK.DataDonationClient)
    }

    @Test
    fun `Given createUserConsent is called with a ConsentDocumentVersion and a Language it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val consentFlow = UserConsentControllerStub()
        val consent = sampleUserConsent
        val scope = CoroutineScope(testCoroutineContext)

        val version = "23"
        val consentDocumentKey = "custom-consent-key"

        val capturedKey = Channel<String>()
        val capturedVersion = Channel<String>()

        consentFlow.whenCreateUserConsent = { delegatedKey, delegatedVersion ->
            launch {
                capturedKey.send(delegatedKey)
                capturedVersion.send(delegatedVersion)
            }
            consent
        }

        val di = koinApplication {
            modules(
                module {
                    single<UserConsentContract.Controller> {
                        consentFlow
                    }
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<ConsentDocumentContract.Controller> {
                        ConsentDocumentControllerStub()
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
            actual = capturedKey.receive(),
            expected = consentDocumentKey,
        )

        assertEquals(
            actual = capturedVersion.receive(),
            expected = version,
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
        val consentDocumentFlow = ConsentDocumentControllerStub()
        val documents = listOf(sampleConsentDocument)
        val scope = CoroutineScope(testCoroutineContext)

        val version = "23"
        val language = "de-j-old-n-kotlin-x-done"
        val consentDocumentKey = "abc"

        val capturedKey = Channel<String>()
        val capturedVersion = Channel<String?>()
        val capturedLanguage = Channel<String?>()

        consentDocumentFlow.whenFetchConsentDocuments = { delegatedKey, delegatedVersion, delegatedLangauge ->
            launch {
                capturedKey.send(delegatedKey)
                capturedVersion.send(delegatedVersion)
                capturedLanguage.send(delegatedLangauge)
            }
            documents
        }

        val di = koinApplication {
            modules(
                module {
                    single<UserConsentContract.Controller> {
                        UserConsentControllerStub()
                    }

                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<ConsentDocumentContract.Controller> {
                        consentDocumentFlow
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
            actual = capturedKey.receive(),
            expected = consentDocumentKey
        )

        assertEquals(
            actual = capturedVersion.receive(),
            expected = version,
        )

        assertEquals(
            actual = capturedLanguage.receive(),
            expected = language,
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
        val consentFlow = UserConsentControllerStub()
        val consents = listOf(sampleUserConsent)
        val scope = CoroutineScope(testCoroutineContext)

        val capturedKey = Channel<String?>()

        consentFlow.whenFetchUserConsents = { delegatedKey ->
            launch {
                capturedKey.send(delegatedKey)
            }
            consents
        }

        val di = koinApplication {
            modules(
                module {
                    single<UserConsentContract.Controller> {
                        consentFlow
                    }

                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<ConsentDocumentContract.Controller> {
                        ConsentDocumentControllerStub()
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
            actual = capturedKey.receive(),
            expected = consentDocumentKey
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
        val consentFlow = UserConsentControllerStub()
        val consents = listOf(sampleUserConsent)
        val scope = CoroutineScope(testCoroutineContext)

        val capturedKey = Channel<String?>()

        consentFlow.whenFetchUserConsents = { delegatedKey ->
            launch {
                capturedKey.send(delegatedKey)
            }
            consents
        }

        val di = koinApplication {
            modules(
                module {
                    single<UserConsentContract.Controller> {
                        consentFlow
                    }
                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<ConsentDocumentContract.Controller> {
                        ConsentDocumentControllerStub()
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

        assertNull(capturedKey.receive())

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
        val consentFlow = UserConsentControllerStub()
        val scope = CoroutineScope(testCoroutineContext)

        val consentDocumentKey = "custom-consent-key"

        val capturedKey = Channel<String>()

        consentFlow.whenRevokeUserConsent = { delegatedKey ->
            launch {
                capturedKey.send(delegatedKey)
            }
            sampleUserConsent
        }

        val di = koinApplication {
            modules(
                module {
                    single<UserConsentContract.Controller> {
                        consentFlow
                    }

                    single<CoroutineScope> {
                        scope
                    }
                    single<DomainErrorMapperContract> {
                        DataDonationFlowErrorMapper
                    }
                    single<D4LSDKFlowFactoryContract> {
                        flowSpy
                    }

                    single<ConsentDocumentContract.Controller> {
                        ConsentDocumentControllerStub()
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
                expected = sampleUserConsent
            )
        }

        assertEquals(
            actual = capturedKey.receive(),
            expected = consentDocumentKey
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
