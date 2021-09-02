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
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentControllerStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentControllerStub
import care.data4life.datadonation.mock.stub.donation.donorkeystorage.DonorKeyStorageProviderStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenProviderStub
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
            UserSessionTokenProviderStub(),
            DonorKeyStorageProviderStub()
        )

        assertTrue(client is DataDonationSDK.DataDonationClient)
    }
}
