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

package care.data4life.datadonation.consentdocument

import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleConsentDocument
import care.data4life.datadonation.mock.stub.consentdocument.ConsentDocumentApiServiceStub
import care.data4life.datadonation.mock.stub.service.UserSessionTokenServiceStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentDocumentRepositoryTest {
    @Test
    fun `It fulfils ConsentDocumentRepository`() {
        val repo: Any = ConsentDocumentRepository(
            ConsentDocumentApiServiceStub(),
            UserSessionTokenServiceStub()
        )

        assertTrue(repo is ConsentDocumentContract.ConsentDocumentRepository)
    }

    @Test
    fun `Given fetchConsentDocuments is called with a AccessToken, a Version and a consentDocumentKey, it resolves the SessionToken and delegates that to the ConsentService and returns a List of ConsentDocuments`() = runBlockingTest {
        // Given
        val consentService = ConsentDocumentApiServiceStub()
        val sessionTokenService = UserSessionTokenServiceStub()

        val consentDocumentKey = "tomato"
        val version = "23"
        val language = "de-j-old-n-kotlin-x-done"

        val sessionToken = "token"
        val consentDocuments = listOf(
            sampleConsentDocument,
            sampleConsentDocument.copy(key = "potato")
        )

        var capturedSessionToken: String? = null
        var capturedVersion: String? = null
        var capturedLanguage: String? = null
        var capturedconsentDocumentKey: String? = null

        sessionTokenService.whenSessionToken = { sessionToken }

        consentService.whenFetchConsentDocuments = { delegatedSessionToken, delegatedVersion, delegatedLanguage, delegatedconsentDocumentKey ->
            capturedSessionToken = delegatedSessionToken
            capturedLanguage = delegatedLanguage
            capturedVersion = delegatedVersion
            capturedconsentDocumentKey = delegatedconsentDocumentKey
            consentDocuments
        }

        val repo = ConsentDocumentRepository(consentService, sessionTokenService)

        // When
        val result = repo.fetchConsentDocuments(language, version, consentDocumentKey)

        // Then
        assertSame(
            actual = result,
            expected = consentDocuments
        )
        assertEquals(
            actual = capturedSessionToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedconsentDocumentKey,
            expected = consentDocumentKey
        )
        assertEquals(
            actual = capturedLanguage,
            expected = language
        )
        assertEquals(
            actual = capturedVersion,
            expected = version
        )
    }
}
