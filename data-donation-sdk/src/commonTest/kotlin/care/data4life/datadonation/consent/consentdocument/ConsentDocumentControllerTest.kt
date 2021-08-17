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

package care.data4life.datadonation.consent.consentdocument

import care.data4life.datadonation.mock.fixture.ConsentDocumentFixture.sampleConsentDocument
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentRepositoryStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentDocumentControllerTest {
    @Test
    fun `It fulfils ConsentDocumentController`() {
        val controller: Any = ConsentDocumentController(
            ConsentDocumentRepositoryStub(),
            UserSessionTokenRepositoryStub()
        )

        assertTrue(controller is ConsentDocumentContract.Controller)
    }

    @Test
    fun `Given fetchConsentDocuments is called, it delegates the call to the ConsentDocumentRepository with the given parameters`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessToken = "session"
        val consentDocumentVersion = "42"
        val language = "de-j-old-n-kotlin-x-done"
        val consentDocumentKey = "tomato"

        var capturedVersion: String? = null
        var capturedLanguage: String? = null
        var capturedConsentDocumentKey: String? = null
        var capturedAccessToken: String? = null

        val consentDocuments = listOf(sampleConsentDocument)

        val repo = ConsentDocumentRepositoryStub()

        sessionTokenRepository.whenSessionToken = { accessToken }

        repo.whenFetchConsentDocuments = { delegatedToken, delegatedKey, delegatedVersion, delegatedLanguage ->
            capturedVersion = delegatedVersion
            capturedLanguage = delegatedLanguage
            capturedConsentDocumentKey = delegatedKey
            capturedAccessToken = delegatedToken
            consentDocuments
        }

        // When
        val result = ConsentDocumentController(
            repo,
            sessionTokenRepository
        ).fetchConsentDocuments(
            consentDocumentKey = consentDocumentKey,
            consentDocumentVersion = consentDocumentVersion,
            language = language
        )

        // Then
        assertSame(
            actual = consentDocuments,
            expected = result
        )
        assertEquals(
            actual = capturedLanguage,
            expected = language
        )
        assertEquals(
            actual = capturedVersion,
            expected = consentDocumentVersion
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
        assertEquals(
            actual = capturedAccessToken,
            expected = accessToken
        )
    }
}
