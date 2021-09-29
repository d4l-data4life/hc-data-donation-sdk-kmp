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

import care.data4life.datadonation.DataDonationSDK.Environment
import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract
import care.data4life.datadonation.consent.userconsent.UserConsentContract
import care.data4life.datadonation.mock.stub.session.UserSessionTokenProviderStub
import care.data4life.sdk.util.test.coroutine.testCoroutineContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.context.stopKoin
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class KoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given initKoin is called with its appropriate parameter, the resulting KoinApplication contains a ConsentInteractor`() {
        // When
        val app = initKoin(
            Environment.DEVELOPMENT,
            UserSessionTokenProviderStub(),
            CoroutineScope(testCoroutineContext)
        )
        // Then
        val controller: UserConsentContract.Controller = app.koin.get()
        assertNotNull(controller)
    }

    @Test
    fun `Given initKoin is called with its appropriate parameter, the resulting KoinApplication contains a ConsentDocumentsInteractor`() {
        // When
        val app = initKoin(
            Environment.DEVELOPMENT,
            UserSessionTokenProviderStub(),
            CoroutineScope(testCoroutineContext)
        )
        // Then
        val controller: ConsentDocumentContract.Controller = app.koin.get()
        assertNotNull(controller)
    }

    @Test
    fun `Given initKoin is called with its appropriate parameter, which contain null, the resulting KoinApplication contains the given CoroutineScope`() {
        // Given
        val scope = CoroutineScope(testCoroutineContext)

        // When
        val app = initKoin(
            Environment.DEVELOPMENT,
            UserSessionTokenProviderStub(),
            scope
        )

        // Then
        assertSame(
            actual = app.koin.get(),
            expected = scope
        )
    }

    @Test
    fun `Given initKoin is called with its appropriate parameter, which contain null, the resulting KoinApplication contains a CoroutineScope`() {
        // When
        val app = initKoin(
            Environment.DEVELOPMENT,
            UserSessionTokenProviderStub(),
            null
        )

        // Then
        val scope: CoroutineScope = app.koin.get()
        assertNotNull(scope)
    }
}
