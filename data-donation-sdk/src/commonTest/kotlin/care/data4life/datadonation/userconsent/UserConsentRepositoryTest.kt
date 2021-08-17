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

package care.data4life.datadonation.userconsent

import care.data4life.datadonation.mock.fixture.UserConsentFixture.sampleUserConsent
import care.data4life.datadonation.mock.stub.userconsent.UserConsentApiServiceStub
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
        val repo: Any = UserConsentRepository(UserConsentApiServiceStub())

        assertTrue(repo is UserConsentContract.Repository)
    }

    @Test
    fun `Given createUserConsent is called with a Version and a consentDocumentKey, it resolves the SessionToken and delegates everything to the UserConsentRemote and just runs`() = runBlockingTest {
        // Given
        val consentService = UserConsentApiServiceStub()

        val accessToken = "token"
        val consentDocumentKey = "custom-consent-key"
        val version = "23"

        var capturedToken: String? = null
        var capturedVersion: String? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        consentService.whenCreateUserConsent = { delegatedToken, delegatedConsentDocumentKey, delegatedVersion ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            capturedVersion = delegatedVersion
        }

        // When
        val result = UserConsentRepository(consentService).createUserConsent(
            accessToken,
            consentDocumentKey,
            version
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedToken,
            expected = accessToken
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
    fun `Given fetchAllUserConsents is called, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = UserConsentApiServiceStub()

        val accessToken = "token"
        val userConsents = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            userConsents
        }

        // When
        val result = UserConsentRepository(consentService).fetchAllUserConsents(accessToken)

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertFalse(capturedFlag!!)
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given fetchLatestUserConsents is called, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = UserConsentApiServiceStub()

        val accessToken = "token"
        val userConsents = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            userConsents
        }

        // When
        val result = UserConsentRepository(consentService).fetchLatestUserConsents(accessToken)

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertTrue(capturedFlag!!)
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given fetchUserConsents is called with a consentDocumentKey, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = UserConsentApiServiceStub()

        val accessToken = "token"
        val consentDocumentKey = "soup"
        val userConsents = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentDocumentKey: String? = null

        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            userConsents
        }

        // When
        val result = UserConsentRepository(consentService).fetchUserConsents(
            accessToken,
            consentDocumentKey
        )

        // Then
        assertSame(
            actual = result,
            expected = userConsents
        )
        assertEquals(
            actual = capturedToken,
            expected = accessToken
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
        val consentService = UserConsentApiServiceStub()

        val accessToken = "token"
        val consentDocumentKey = "custom-consent-key"

        var capturedToken: String? = null
        var capturedConsentDocumentKey: String? = "NotNull"

        consentService.whenRevokeUserConsent = { delegatedToken, delegatedConsentDocumentKey ->
            capturedToken = delegatedToken
            capturedConsentDocumentKey = delegatedConsentDocumentKey
        }

        // When
        UserConsentRepository(consentService).revokeUserConsent(accessToken, consentDocumentKey)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertEquals(
            actual = capturedConsentDocumentKey,
            expected = consentDocumentKey
        )
    }
}
