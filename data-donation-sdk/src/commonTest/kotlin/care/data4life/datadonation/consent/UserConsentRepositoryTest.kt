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

package care.data4life.datadonation.consent

import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleUserConsent
import care.data4life.datadonation.mock.stub.consent.ConsentApiServiceStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class UserConsentRepositoryTest {
    @Test
    fun `It fulfils UserConsentRepository`() {
        val repo: Any = UserConsentRepository(
            ConsentApiServiceStub(),
            UserSessionTokenRepositoryStub()
        )

        assertTrue(repo is ConsentContract.Repository)
    }

    @Test
    fun `Given createUserConsent is called with a Version and a consentDocumentKey, it resolves the SessionToken and delegates everything to the UserConsentRemote and just runs`() = runBlockingTest {
        // Given
        val consentService = ConsentApiServiceStub()
        val sessionService = UserSessionTokenRepositoryStub()

        val sessionToken = "token"
        val consentDocumentKey = "custom-consent-key"
        val version = "23"

        var capturedToken: String? = null
        var capturedVersion: String? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenCreateUserConsent = { delegatedToken, delegatedConsentDocumentKey, delegatedVersion ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            capturedVersion = delegatedVersion
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.createUserConsent(consentDocumentKey, version)

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedVersion,
            expected = version
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }

    @Test
    fun `Given fetchUserConsents is called without a consentDocumentKey, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = ConsentApiServiceStub()
        val sessionService = UserSessionTokenRepositoryStub()

        val sessionToken = "token"
        val userConsents = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            userConsents
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.fetchUserConsents(null)

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertFalse(capturedFlag!!)
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given fetchUserConsents is called with a consentDocumentKey, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = ConsentApiServiceStub()
        val sessionService = UserSessionTokenRepositoryStub()

        val sessionToken = "token"
        val consentDocumentKey = "soup"
        val userConsents = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentDocumentKey: String? = null

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            userConsents
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.fetchUserConsents(consentDocumentKey)

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )

        assertFalse(capturedFlag!!)
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }

    @Test
    fun `Given revokeUserConsent is called with a consentDocumentKey, it resolves the SessionToken and delegates that to the ConsentService and just runs`() = runBlockingTest {
        // Given
        val consentService = ConsentApiServiceStub()
        val sessionService = UserSessionTokenRepositoryStub()

        val sessionToken = "token"
        val consentDocumentKey = "custom-consent-key"

        var capturedToken: String? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenRevokeUserConsent = { delegatedToken, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        repo.revokeUserConsent(consentDocumentKey)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }
}
