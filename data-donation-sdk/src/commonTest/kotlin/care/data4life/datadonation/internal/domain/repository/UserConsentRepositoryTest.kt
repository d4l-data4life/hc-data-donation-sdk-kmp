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

import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.DummyData
import care.data4life.datadonation.internal.mock.MockContract
import care.data4life.datadonation.internal.mock.MockException
import care.data4life.datadonation.internal.mock.stub.UserSessionTokenDataStoreStub
import runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class UserConsentRepositoryTest {
    @Test
    fun `It fulfils UserConsentRepository`() {
        val repo: Any = UserConsentRepository(UserConsentRemoteStub(), UserSessionTokenDataStoreStub())

        assertTrue(repo is RepositoryInternalContract.UserConsentRepository)
    }

    @Test
    fun `Given createUserConsent is called with a Version and a Language, it resolves the SessionToken and delegates everything to the UserConsentRemote and just runs`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val version = 23
        val language = "de-j-old-n-kotlin-x-done"

        var capturedToken: String? = null
        var capturedVersion: Int? = null
        var capturedLanguage: String? = null

        session.sessionToken = sessionToken
        remote.whenCreateUserConsent = { delegatedToken, delegatedVersion, delegatedLanguage ->
            capturedToken = delegatedToken
            capturedVersion = delegatedVersion
            capturedLanguage = delegatedLanguage
        }

        val repo = UserConsentRepository(remote, session)

        // When
        repo.createUserConsent(version, language)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedVersion,
            expected = version
        )
        assertEquals(
            actual = capturedLanguage,
            expected = language
        )
    }

    @Test
    fun `Given fetchUserConsents is called without a ConsentKey, it resolves the SessionToken and delegates it to the UserConsentRemote and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val userConsents = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedConsentKey: String? = "NotNull"

        session.sessionToken = sessionToken
        remote.whenFetchUserConsents = { delegatedToken, delegatedConsentKey ->
            capturedToken = delegatedToken
            capturedConsentKey = delegatedConsentKey
            userConsents
        }

        val repo = UserConsentRepository(remote, session)

        // When
        val result = repo.fetchUserConsents()

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertNull(capturedConsentKey)
    }

    @Test
    fun `Given fetchUserConsents is called with a ConsentKey, it resolves the SessionToken and delegates it to the UserConsentRemote and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val consentKey = "soup"
        val userConsents = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedConsentKey: String? = null

        session.sessionToken = sessionToken
        remote.whenFetchUserConsents = { delegatedToken, delegatedConsentKey ->
            capturedToken = delegatedToken
            capturedConsentKey = delegatedConsentKey
            userConsents
        }

        val repo = UserConsentRepository(remote, session)

        // When
        val result = repo.fetchUserConsents(consentKey)

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedConsentKey,
            expected = consentKey
        )
    }

    @Test
    fun `Given signUserConsentRegistration is called with a Message, it resolves the SessionToken and delegates them to the UserConsentRemote and returns a String`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val message = "soup"
        val registrationMessage = "potato"

        var capturedToken: String? = null
        var capturedMessage: String? = null

        session.sessionToken = sessionToken
        remote.whenSignUserConsentRegistration = { delegatedToken, delegatedMessage ->
            capturedToken = delegatedToken
            capturedMessage = delegatedMessage
            registrationMessage
        }

        val repo = UserConsentRepository(remote, session)

        // When
        val result = repo.signUserConsentRegistration(message)

        // Then
        assertSame(
            actual = result,
            expected = registrationMessage
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedMessage,
            expected = message
        )
    }

    @Test
    fun `Given signUserConsentDonation is called with a Message, it resolves the SessionToken and delegates them to the UserConsentRemote and returns a String`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val message = "soup"
        val registrationMessage = "potato"

        var capturedToken: String? = null
        var capturedMessage: String? = null

        session.sessionToken = sessionToken
        remote.whenSignUserConsentDonation = { delegatedToken, delegatedMessage ->
            capturedToken = delegatedToken
            capturedMessage = delegatedMessage
            registrationMessage
        }

        val repo = UserConsentRepository(remote, session)

        // When
        val result = repo.signUserConsentDonation(message)

        // Then
        assertSame(
            actual = result,
            expected = registrationMessage
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedMessage,
            expected = message
        )
    }

    @Test
    fun `Given revokeUserConsent is called with a Language, it resolves the SessionToken and delegates them to the UserConsentRemote and just runs`() = runBlockingTest {
        // Given
        val remote = UserConsentRemoteStub()
        val session = UserSessionTokenDataStoreStub()
        val sessionToken = "token"
        val language = "de-j-old-n-kotlin-x-done"

        var capturedToken: String? = null
        var capturedLanguage: String? = null

        session.sessionToken = sessionToken
        remote.whenRevokeUserConsent = { delegatedToken, delegatedLanguage ->
            capturedToken = delegatedToken
            capturedLanguage = delegatedLanguage
        }

        val repo = UserConsentRepository(remote, session)

        // When
        repo.revokeUserConsent(language)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedLanguage,
            expected = language
        )
    }

    private class UserConsentRemoteStub : RepositoryInternalContract.UserConsentRemote, MockContract.Stub {
        var whenCreateUserConsent: ((String?, Int, String?) -> Unit)? = null
        var whenFetchUserConsents: ((String?, String?) -> List<UserConsent>)? = null
        var whenSignUserConsentRegistration: ((String, String) -> String)? = null
        var whenSignUserConsentDonation: ((String, String) -> String)? = null
        var whenRevokeUserConsent: ((String, String?) -> Unit)? = null

        override suspend fun createUserConsent(
            accessToken: String,
            version: Int,
            language: String?
        ) = whenCreateUserConsent?.invoke(accessToken, version, language) ?: throw MockException()

        override suspend fun fetchUserConsents(
            accessToken: String,
            consentKey: String?
        ): List<UserConsent> = whenFetchUserConsents?.invoke(accessToken, consentKey) ?: throw MockException()

        override suspend fun signUserConsentRegistration(
            accessToken: String,
            message: String
        ): String = whenSignUserConsentRegistration?.invoke(accessToken, message) ?: throw MockException()

        override suspend fun signUserConsentDonation(
            accessToken: String,
            message: String
        ): String = whenSignUserConsentDonation?.invoke(accessToken, message) ?: throw MockException()

        override suspend fun revokeUserConsent(
            accessToken: String,
            language: String?
        ) = whenRevokeUserConsent?.invoke(accessToken, language) ?: throw MockException()

        override fun clear() {
            whenCreateUserConsent = null
            whenFetchUserConsents = null
            whenSignUserConsentRegistration = null
            whenSignUserConsentDonation = null
            whenRevokeUserConsent = null
        }
    }
}
