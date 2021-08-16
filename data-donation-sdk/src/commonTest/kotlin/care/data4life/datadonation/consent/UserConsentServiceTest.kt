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

import care.data4life.datadonation.mock.fixture.ConsentFixtures
import care.data4life.datadonation.mock.stub.consent.UserConsentRepositoryStub
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class UserConsentServiceTest {
    @Test
    fun `It fulfils ConsentInteractor`() {
        val interactor: Any = UserConsentService(
            UserConsentRepositoryStub()
        )

        assertTrue(interactor is ConsentContract.Interactor)
    }

    @Test
    fun `Given createUserConsent is called, it delegates the call to the UserContentRepository with the given parameters and returns the first consent`() = runBlockingTest {
        // Given
        val consentDocumentKey = "custom-consent-key"
        val consentDocumentVersion = "42"

        var capturedConsentDocumentKeyCreate: String? = "NotNull"
        var capturedVersion: String? = null

        var capturedConsentDocumentKeyFetch: String? = null

        val consent = ConsentFixtures.sampleUserConsent

        val repo = UserConsentRepositoryStub()

        repo.whenCreateUserConsent = { delegatedConsentDocumentKey, delegatedVersion ->
            capturedConsentDocumentKeyCreate = delegatedConsentDocumentKey
            capturedVersion = delegatedVersion
        }
        repo.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKeyFetch = delegatedConsentDocumentKey
            listOf(consent, ConsentFixtures.sampleUserConsent.copy(accountId = "not expected"))
        }

        // When
        val result = UserConsentService(repo).createUserConsent(
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
            actual = capturedVersion,
            expected = consentDocumentVersion
        )
        assertNull(capturedConsentDocumentKeyFetch)
    }

    @Test
    fun `Given fetchAllUserConsents is called, it calls to the ConsentRepository without a consentDocumentKey`() = runBlockingTest {
        // Given
        val dummyConsentList = listOf(ConsentFixtures.sampleUserConsent)
        var capturedConsentDocumentKey: String? = null
        val repo = UserConsentRepositoryStub()

        repo.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        // When
        val result = UserConsentService(repo).fetchAllUserConsents()

        // Then
        assertSame(
            actual = result,
            expected = dummyConsentList
        )
        assertNull(capturedConsentDocumentKey)
    }

    @Test
    fun `Given fetchUserConsents is called, it delegates the call to the ConsentRepository with the consentDocumentKey`() = runBlockingTest {
        // Given
        val consentDocumentKey = "key"
        val dummyConsentList = listOf(ConsentFixtures.sampleUserConsent)
        var capturedConsentDocumentKey: String? = null
        val repo = UserConsentRepositoryStub()

        repo.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKey = delegatedConsentDocumentKey
            dummyConsentList
        }

        // When
        val result = UserConsentService(repo).fetchUserConsents(consentDocumentKey)

        // Then
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
        val consentDocumentKey = "custom-consent-key"
        var capturedConsentDocumentKeyRevoke: String? = "NotNull"

        var capturedConsentDocumentKeyFetch: String? = null

        val consent = ConsentFixtures.sampleUserConsent

        val repo = UserConsentRepositoryStub()

        repo.whenRevokeUserConsent = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKeyRevoke = delegatedConsentDocumentKey
        }

        repo.whenFetchUserConsents = { delegatedConsentDocumentKey ->
            capturedConsentDocumentKeyFetch = delegatedConsentDocumentKey
            listOf(consent, ConsentFixtures.sampleUserConsent.copy(accountId = "not expected"))
        }

        // When
        val result = UserConsentService(repo).revokeUserConsent(consentDocumentKey)

        // Then
        assertSame(
            actual = consent,
            expected = result
        )
        assertEquals(
            actual = capturedConsentDocumentKeyRevoke,
            expected = consentDocumentKey
        )
        assertNull(capturedConsentDocumentKeyFetch)
    }
}
