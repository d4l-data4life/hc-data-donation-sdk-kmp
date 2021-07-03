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

import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.di.coreModule
import care.data4life.datadonation.internal.di.platformModule
import care.data4life.datadonation.internal.di.resolveRootModule
import care.data4life.datadonation.internal.domain.usecases.FetchUserConsentsFactory
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.mock.stub.ClientConfigurationStub
import care.data4life.datadonation.internal.mock.stub.FetchUserConsentStub
import care.data4life.datadonation.internal.mock.stub.FetchUserUsecaseStub
import care.data4life.datadonation.internal.mock.stub.ResultListenerStub
import kotlinx.coroutines.CoroutineScope
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import runBlockingTest
import testCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClientTest {
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
    fun `Given fetchUserConsents is called without a ConsentKey and with a ResultListener it executes the Usecase with its default Parameter`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<List<UserConsent>>()
        val usecase = FetchUserUsecaseStub()
        val consents = listOf(
            DummyData.userConsent
        )

        var capturedParameter: UsecaseContract.FetchUserConsentsParameter? = null
        var capturedResult: List<UserConsent>? = null
        var capturedError: Exception? = null

        config.whenGetEnvironment = { Environment.LOCAL }
        config.whenGetCoroutineContext = { CoroutineScope(testCoroutineContext) }

        val di = startKoin {
            modules(
                resolveRootModule(config),
                coreModule(),
                platformModule(),
                module(override = true) {
                    single<UsecaseContract.FetchUserConsents> {
                        FetchUserConsentStub().also {
                            it.whenWithParameter = { delegateParameter ->
                                capturedParameter = delegateParameter
                                usecase
                            }
                        }
                    } bind UsecaseContract.FetchUserConsents::class
                }
            )
        }

        val client = Client(config, di)

        listener.whenOnSuccess = { delegatedResult ->
            println("delegatedResult")
            capturedResult = delegatedResult
        }
        listener.whenOnError = { delegatedError ->
            capturedError = delegatedError
        }

        usecase.whenExecute = {
            consents
        }

        // When
        client.fetchUserConsents(listener)

        // Then
        assertNull(capturedError)
        assertEquals(
            actual = capturedResult,
            expected = consents
        )
        assertEquals(
            actual = capturedParameter,
            expected = FetchUserConsentsFactory.Parameters()
        )
    }
}
