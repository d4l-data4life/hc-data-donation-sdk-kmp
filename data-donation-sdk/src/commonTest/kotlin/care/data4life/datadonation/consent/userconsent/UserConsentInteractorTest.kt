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

package care.data4life.datadonation.consent.userconsent

import care.data4life.datadonation.mock.fixture.UserConsentFixture
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentRepositoryStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class UserConsentInteractorTest {
    @Test
    fun `It fulfils UserConsentInteractor`() {
        val interactor: Any = UserConsentInteractor(
            UserConsentRepositoryStub(),
            UserSessionTokenRepositoryStub()
        )

        assertTrue(interactor is UserConsentContract.Interactor)
    }

    @Test
    fun `Given createUserConsent is called, it delegates the call to the UserContentRepository with the given parameters and returns the first consent`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessTokenCreate = "session"
        val accessTokenFetch = "token"
        val tokens = mutableListOf(accessTokenCreate, accessTokenFetch)

        val consentDocumentKey = "custom-consent-key"
        val consentDocumentVersion = "42"

        var capturedTokenCreate: String? = null
        var capturedConsentDocumentKeyCreate: String? = "NotNull"
        var capturedVersion: String? = null

        var capturedTokenFetch: String? = null
        var capturedConsentDocumentKeyFetch: String? = null

        val consent = UserConsentFixture.sampleUserConsent

        val repo = UserConsentRepositoryStub()

        sessionTokenRepository.whenSessionToken = { tokens.removeAt(0) }

        repo.whenCreateUserConsent = { delegatedToken, delegatedConsentDocumentKey, delegatedVersion ->
            capturedConsentDocumentKeyCreate = delegatedConsentDocumentKey
            capturedVersion = delegatedVersion
            capturedTokenCreate = delegatedToken
        }
        repo.whenFetchUserConsents = { delegatedToken, delegatedConsentDocumentKey ->
            capturedConsentDocumentKeyFetch = delegatedConsentDocumentKey
            capturedTokenFetch = delegatedToken
            listOf(consent, UserConsentFixture.sampleUserConsent.copy(accountId = "not expected"))
        }

        // When
        val result = UserConsentInteractor(repo, sessionTokenRepository).createUserConsent(
            consentDocumentKey,
            consentDocumentVersion
        )

        // Then
        assertSame(
            actual = consent,
            expected = result
        )
        assertEquals(
            actual = capturedConsentDocumentKeyCreate,
            expected = consentDocumentKey
        )
        assertEquals(
            actual = capturedTokenCreate,
            expected = accessTokenCreate
        )
        assertEquals(
            actual = capturedVersion,
            expected = consentDocumentVersion
        )
        assertNull(capturedConsentDocumentKeyFetch)
        assertEquals(
            actual = capturedTokenFetch,
            expected = accessTokenFetch
        )
    }

    @Test
    fun `Given fetchAllUserConsents is called, it calls to the ConsentRepository without a consentDocumentKey`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessToken = "session"
        val dummyConsentList = listOf(UserConsentFixture.sampleUserConsent)

        var capturedToken: String? = null
        var capturedConsentDocumentKey: String? = null
        val repo = UserConsentRepositoryStub()

        repo.whenFetchUserConsents = { delegatedToken, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        sessionTokenRepository.whenSessionToken = { accessToken }

        // When
        val result = UserConsentInteractor(repo, sessionTokenRepository).fetchAllUserConsents()

        // Then
        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given fetchUserConsents is called, it delegates the call to the ConsentRepository with the consentDocumentKey`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessToken = "session"
        val consentDocumentKey = "key"
        val dummyConsentList = listOf(UserConsentFixture.sampleUserConsent)

        var capturedToken: String? = null
        var capturedConsentDocumentKey: String? = null
        val repo = UserConsentRepositoryStub()

        repo.whenFetchUserConsents = { delegatedToken, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        sessionTokenRepository.whenSessionToken = { accessToken }

        // When
        val result = UserConsentInteractor(repo, sessionTokenRepository).fetchUserConsents(consentDocumentKey)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }

    @Test
    fun `Given revokeUserConsentis called, it delegates the call to the ConsentRepository with the given consentDocumentKey and return the latest consent`() = runBlockingTest {
        // Given
        val sessionTokenRepository = UserSessionTokenRepositoryStub()

        val accessTokenRevoke = "session"
        val accessTokenFetch = "token"
        val tokens = mutableListOf(accessTokenRevoke, accessTokenFetch)

        var capturedTokenRevoke: String? = null
        val consentDocumentKey = "custom-consent-key"
        var capturedConsentDocumentKeyRevoke: String? = "NotNull"

        var capturedTokenFetch: String? = null
        var capturedConsentDocumentKeyFetch: String? = null

        val consent = UserConsentFixture.sampleUserConsent

        val repo = UserConsentRepositoryStub()

        sessionTokenRepository.whenSessionToken = { tokens.removeAt(0) }

        repo.whenRevokeUserConsent = { delegatedToken, delegatedConsentDocumentKey ->
            capturedTokenRevoke = delegatedToken
            capturedConsentDocumentKeyRevoke = delegatedConsentDocumentKey
        }

        repo.whenFetchUserConsents = { delegatedToken, delegatedConsentDocumentKey ->
            capturedTokenFetch = delegatedToken
            capturedConsentDocumentKeyFetch = delegatedConsentDocumentKey
            listOf(consent, UserConsentFixture.sampleUserConsent.copy(accountId = "not expected"))
        }

        // When
        val result = UserConsentInteractor(repo, sessionTokenRepository).revokeUserConsent(consentDocumentKey)

        // Then
        assertSame(
            actual = consent,
            expected = result
        )
        assertEquals(
            actual = capturedTokenRevoke,
            expected = accessTokenRevoke
        )
        assertEquals(
            actual = capturedConsentDocumentKeyRevoke,
            expected = consentDocumentKey
        )
        assertNull(capturedConsentDocumentKeyFetch)
        assertEquals(
            actual = capturedTokenFetch,
            expected = accessTokenFetch
        )
    }
}
