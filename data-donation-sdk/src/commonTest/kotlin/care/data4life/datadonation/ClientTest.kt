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

import care.data4life.datadonation.Contract.Environment
import care.data4life.datadonation.internal.domain.usecases.CreateUserConsent
import care.data4life.datadonation.internal.domain.usecases.FetchConsentDocuments
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsents
import care.data4life.datadonation.internal.domain.usecases.RevokeUserConsent
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.stub.CreateUserConsentStub
import care.data4life.datadonation.mock.stub.FetchConsentDocumentsStub
import care.data4life.datadonation.mock.stub.FetchUserConsentStub
import care.data4life.datadonation.mock.stub.RevokeUserConsentStub
import care.data4life.datadonation.mock.stub.UserSessionTokenProviderStub
import care.data4life.sdk.util.test.runWithContextBlockingTest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        val client: Any = Client.getInstance(
            Environment.DEV,
            UserSessionTokenProviderStub()
        )

        assertTrue(client is Contract.DataDonation)
    }

    @Test
    fun `Given fetchConsentDocuments is called with a ConsentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of ConsentDocument`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchConsentDocumentsStub()
        val documents = listOf(DummyData.consentDocument)

        val version = 23
        val language = "de-j-old-n-kotlin-x-done"
        val consentKey = "abc"

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
                    single<UsecaseContract.FetchConsentDocuments> {
                        usecase
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.fetchConsentDocuments(
            version,
            language,
            consentKey,
        ).collect { result ->
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
                consentKey = consentKey
            )
        )
    }

    @Test
    fun `Given createUserConsent is called with a ConsentDocumentVersion and a Language it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = CreateUserConsentStub()
        val consent = DummyData.userConsent

        val version = 23
        val consentKey = "custom-consent-key"

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
                    single {
                        usecase
                    } bind UsecaseContract.CreateUserConsent::class
                }
            )
        }

        val client = Client(di)

        // When
        client.createUserConsent(
            consentKey,
            version
        ).collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = consent
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = CreateUserConsent.Parameter(
                consentKey = consentKey,
                version = version
            )
        )
    }

    @Test
    fun `Given fetchUserConsents is called with a ConsentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchUserConsentStub()
        val consents = listOf(DummyData.userConsent)

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
                    single {
                        usecase
                    } bind UsecaseContract.FetchUserConsents::class
                }
            )
        }

        val consentKey = "key"
        val client = Client(di)

        // When
        client.fetchUserConsents(
            consentKey
        ).collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = consents
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = FetchUserConsents.Parameter(
                consentKey = consentKey
            )
        )
    }

    @Test
    fun `Given fetchAllUserConsents is called it builds and delegates its Parameter to the Usecase and returns a runnable Flow which emits a List of UserConsent`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = FetchUserConsentStub()
        val consents = listOf(DummyData.userConsent)

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
                    single {
                        usecase
                    } bind UsecaseContract.FetchUserConsents::class
                }
            )
        }

        val client = Client(di)

        // When
        client.fetchAllUserConsents().collect { result ->
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
    }

    @Test
    fun `Given revokeUserConsent is called with a ConsentKey it builds and delegates its Parameter to the Usecase and returns a runnable Flow which just runs`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val usecase = RevokeUserConsentStub()

        val consentKey = "custom-consent-key"

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
                    single {
                        usecase
                    } bind UsecaseContract.RevokeUserConsent::class
                }
            )
        }

        val client = Client(di)

        // When
        client.revokeUserConsent(consentKey).collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = Unit
            )
        }

        assertEquals(
            actual = capturedParameter.receive(),
            expected = RevokeUserConsent.Parameter(
                consentKey = consentKey,
            )
        )
    }
}
