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

package care.data4life.datadonation.internal.domain.repository

import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.stub.ConsentDocumentRemoteStub
import care.data4life.datadonation.mock.stub.UserSessionTokenDataStoreStub
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentDocumentRepositoryTest {
    @Test
    fun `It fulfils ConsentDocumentRepository`() {
        val repo: Any = ConsentDocumentRepository(
            ConsentDocumentRemoteStub(),
            UserSessionTokenDataStoreStub()
        )

        assertTrue(repo is RepositoryInternalContract.ConsentDocumentRepository)
    }

    @Test
    fun `Given fetchConsentDocuments is called with a AccessToken, a Version and a ConsentKey, it resolves the SessionToken and delegates that to the ConsentDocumentRemote and returns a List of ConsentDocuments`() = runBlockingTest {
        // Given
        val remote = ConsentDocumentRemoteStub()
        val session = UserSessionTokenDataStoreStub()

        val consentKey = "tomato"
        val version = 23
        val language = "de-j-old-n-kotlin-x-done"

        val sessionToken = "token"
        val consentDocuments = listOf(
            DummyData.consentDocument,
            DummyData.consentDocument.copy(key = "potato")
        )

        var capturedSessionToken: String? = null
        var capturedVersion: Int? = null
        var capturedLanguage: String? = null
        var capturedConsentKey: String? = null

        session.sessionToken = sessionToken

        remote.whenFetchConsentDocuments = { delegatedSessionToken, delegatedVersion, delegatedLanguage, delegatedConsentKey ->
            capturedSessionToken = delegatedSessionToken
            capturedLanguage = delegatedLanguage
            capturedVersion = delegatedVersion
            capturedConsentKey = delegatedConsentKey
            consentDocuments
        }

        val repo = ConsentDocumentRepository(remote, session)

        // When
        val result = repo.fetchConsentDocuments(language, version, consentKey)

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
            actual = capturedConsentKey,
            expected = consentKey
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
