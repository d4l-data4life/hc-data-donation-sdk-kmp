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

import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract
import care.data4life.datadonation.consent.userconsent.UserConsentContract
import care.data4life.datadonation.donation.DonationContract
import care.data4life.datadonation.error.DataDonationFlowErrorMapper
import care.data4life.datadonation.mock.spy.D4LFlowFactorySpy
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentControllerStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentControllerStub
import care.data4life.datadonation.mock.stub.donation.DonationControllerStub
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
import kotlin.test.assertSame

class ClientDonationTest {
    private var flowSpy = D4LFlowFactorySpy()

    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given registerDonor is called with a ProgramName it delegates its Parameter to the DonationController and returns a runnable Flow which emits Unit`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val donationFlow = DonationControllerStub()
        val scope = CoroutineScope(testCoroutineContext)

        val programName = "Potato"

        val capturedProgramName = Channel<String>()

        donationFlow.whenRegister = { delegatedProgramName ->
            launch {
                capturedProgramName.send(delegatedProgramName)
            }

            Unit
        }

        val di = koinApplication {
            modules(
                module {
                    single<DonationContract.Controller> {
                        donationFlow
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

                    single<UserConsentContract.Controller> {
                        UserConsentControllerStub()
                    }
                }
            )
        }

        val client = Client(di)

        // When
        client.registerDonor(programName).ktFlow.collect { result ->
            // Then
            assertSame(
                actual = result,
                expected = Unit
            )
        }

        assertEquals(
            actual = capturedProgramName.receive(),
            expected = programName
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
