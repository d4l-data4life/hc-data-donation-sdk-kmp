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
import care.data4life.datadonation.core.listener.ListenerInternalContract
import care.data4life.datadonation.core.listener.listenerModule
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.di.coreModule
import care.data4life.datadonation.internal.di.platformModule
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsentsFactory
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract.Usecase
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import care.data4life.datadonation.mock.stub.FetchUserConsentStub
import care.data4life.datadonation.mock.stub.FetchUserUsecaseStub
import care.data4life.datadonation.mock.stub.ResultListenerStub
import care.data4life.datadonation.mock.stub.TaskRunnerStub
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
    fun `Given fetchAllUserConsents is called with a ResultListener it resolves the Usecase with its default Parameter and delegates them to the TaskRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<UserConsent>>()
        val usecase = FetchUserUsecaseStub()

        var capturedParameter: UsecaseContract.FetchUserConsentsParameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: Usecase<*>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                resolveRootModule(config),
                coreModule(),
                platformModule(),
                listenerModule(config),
                module(override = true) {
                    single {
                        FetchUserConsentStub().also {
                            it.whenWithParameter = { delegateParameter ->
                                capturedParameter = delegateParameter
                                usecase
                            }
                        }
                    } bind UsecaseContract.FetchUserConsents::class

                    single {
                        TaskRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                            }
                        }
                    } bind ListenerInternalContract.TaskRunner::class
                }
            )
        }

        val client = Client(config, di)

        // When
        client.fetchAllUserConsents(listener)

        // Then
        assertEquals(
            actual = capturedParameter,
            expected = FetchUserConsentsFactory.Parameters()
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
    fun `Given fetchUserConsents is called with a ConsentKey and with a ResultListener it resolves the Usecase with its Parameter, which contains the given value and delegates them to the TaskRunner`() {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<UserConsent>>()
        val usecase = FetchUserUsecaseStub()

        val consentKey = "abc"

        var capturedParameter: UsecaseContract.FetchUserConsentsParameter? = null
        var capturedListener: ListenerContract.ResultListener<*>? = null
        var capturedUsecase: Usecase<*>? = null

        config.whenGetEnvironment = { Environment.LOCAL }

        val di = koinApplication {
            modules(
                resolveRootModule(config),
                coreModule(),
                platformModule(),
                listenerModule(config),
                module(override = true) {
                    single {
                        FetchUserConsentStub().also {
                            it.whenWithParameter = { delegateParameter ->
                                capturedParameter = delegateParameter
                                usecase
                            }
                        }
                    } bind UsecaseContract.FetchUserConsents::class

                    single {
                        TaskRunnerStub().also {
                            it.whenRunListener = { delegatedResultListener, delegatedUsecase ->
                                capturedListener = delegatedResultListener
                                capturedUsecase = delegatedUsecase
                            }
                        }
                    } bind ListenerInternalContract.TaskRunner::class
                }
            )
        }

        val client = Client(config, di)

        // When
        client.fetchUserConsents(consentKey, listener)

        // Then
        assertEquals(
            actual = capturedParameter,
            expected = FetchUserConsentsFactory.Parameters(consentKey)
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
