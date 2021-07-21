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
import care.data4life.datadonation.mock.stub.service.ConsentServiceStub
import care.data4life.datadonation.mock.stub.service.UserSessionTokenServiceStub
import care.data4life.sdk.util.test.runBlockingTest
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
            ConsentServiceStub(),
            UserSessionTokenServiceStub()
        )

        assertTrue(repo is RepositoryContract.UserConsentRepository)
    }

    @Test
    fun `Given createUserConsent is called with a Version and a ConsentKey, it resolves the SessionToken and delegates everything to the UserConsentRemote and just runs`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val consentKey = "custom-consent-key"
        val version = 23

        var capturedToken: String? = null
        var capturedVersion: Int? = null
        var capturedConsentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenCreateUserConsent = { delegatedToken, delegatedConsentKey, delegatedVersion ->
            capturedToken = delegatedToken
            capturedConsentKey = delegatedConsentKey
            capturedVersion = delegatedVersion
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.createUserConsent(consentKey, version)

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
            actual = capturedConsentKey,
            expected = consentKey
        )
    }

    @Test
    fun `Given fetchUserConsents is called without a ConsentKey, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val userConsents = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentKey = delegatedConsentKey
            userConsents
        }

        val repo = UserConsentRepository(consentService, sessionService)

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
        assertFalse(capturedFlag!!)
        assertNull(capturedConsentKey)
    }

    @Test
    fun `Given fetchUserConsents is called with a ConsentKey, it resolves the SessionToken and delegates it to the ConsentService and returns a List of UserConsent`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val consentKey = "soup"
        val userConsents = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "tomato")
        )

        var capturedToken: String? = null
        var capturedFlag: Boolean? = null
        var capturedConsentKey: String? = null

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenFetchUserConsents = { delegatedToken, delegatedFlag, delegatedConsentKey ->
            capturedToken = delegatedToken
            capturedFlag = delegatedFlag
            capturedConsentKey = delegatedConsentKey
            userConsents
        }

        val repo = UserConsentRepository(consentService, sessionService)

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

        assertFalse(capturedFlag!!)
        assertEquals(
            actual = capturedConsentKey,
            expected = consentKey
        )
    }

    @Test
    fun `Given signUserConsentRegistration is called with a Message, it resolves the SessionToken and delegates that to the ConsentService and returns a String`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val message = "soup"
        val signature = "potato"

        var capturedToken: String? = null
        var capturedMessage: String? = null

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenRequestSignatureConsentRegistration = { delegatedToken, delegatedMessage ->
            capturedToken = delegatedToken
            capturedMessage = delegatedMessage
            DummyData.consentSignature.copy(signature = signature)
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.signUserConsentRegistration(message)

        // Then
        assertSame(
            actual = result,
            expected = signature
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
    fun `Given signUserConsentDonation is called with a Message, it resolves the SessionToken and delegates that to the ConsentService and returns a String`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val message = "soup"
        val signature = "potato"

        var capturedToken: String? = null
        var capturedMessage: String? = null

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenRequestSignatureDonation = { delegatedToken, delegatedMessage ->
            capturedToken = delegatedToken
            capturedMessage = delegatedMessage
            DummyData.consentSignature.copy(signature = signature)
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        val result = repo.signUserConsentDonation(message)

        // Then
        assertSame(
            actual = result,
            expected = signature
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
    fun `Given revokeUserConsent is called with a ConsentKey, it resolves the SessionToken and delegates that to the ConsentService and just runs`() = runBlockingTest {
        // Given
        val consentService = ConsentServiceStub()
        val sessionService = UserSessionTokenServiceStub()

        val sessionToken = "token"
        val consentKey = "custom-consent-key"

        var capturedToken: String? = null
        var capturedConsentKey: String? = "NotNull"

        sessionService.whenSessionToken = { sessionToken }
        consentService.whenRevokeUserConsent = { delegatedToken, delegatedConsentKey ->
            capturedToken = delegatedToken
            capturedConsentKey = delegatedConsentKey
        }

        val repo = UserConsentRepository(consentService, sessionService)

        // When
        repo.revokeUserConsent(consentKey)

        // Then
        assertEquals(
            actual = capturedToken,
            expected = sessionToken
        )
        assertEquals(
            actual = capturedConsentKey,
            expected = consentKey
        )
    }
}
