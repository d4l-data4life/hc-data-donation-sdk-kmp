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

package care.data4life.datadonation.internal.data.service

import care.data4life.datadonation.core.model.ModelContract.Environment
import care.data4life.datadonation.internal.data.model.ConsentCreationPayload
import care.data4life.datadonation.internal.data.model.ConsentRevocationPayload
import care.data4life.datadonation.internal.data.model.ConsentSignatureType
import care.data4life.datadonation.internal.data.model.ConsentSigningRequest
import care.data4life.datadonation.internal.data.service.ServiceContract.Companion.DEFAULT_DONATION_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LANGUAGE
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.VERSION
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.CONSENTS_DOCUMENTS
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.SIGNATURES
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.USER_CONSENTS
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.Path
import care.data4life.datadonation.lang.CoreRuntimeError
import care.data4life.datadonation.lang.HttpRuntimeError
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.fake.createDefaultMockClient
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.service.ConsentErrorHandlerStub
import care.data4life.datadonation.mock.stub.service.networking.CallBuilderSpy
import care.data4life.sdk.util.test.runBlockingTest
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentServiceTest {
    @BeforeTest
    fun setUp() {
        CallBuilderSpy.clear()
    }

    @Test
    fun `It fulfils ConsentService`() {
        val service: Any = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )

        assertTrue(service is ServiceContract.ConsentService)
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException("Test")
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleFetchConsentDocuments = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                ClockStub()
            )
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentKey = consentKey
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            listOf("something")
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseCastFailure> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it returns a List of ConsentDocument`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.consentDocument,
            DummyData.consentDocument.copy(key = "soup")
        )

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.fetchConsentDocuments(
            accessToken = accessToken,
            version = version,
            language = language,
            consentKey = consentKey
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(CONSENTS_DOCUMENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedParameter,
            expected = mapOf(
                USER_CONSENT_KEY to consentKey,
                VERSION to version,
                LANGUAGE to language
            )
        )
    }

    @Test
    fun `Given fetchUserConsents was called with a AccessToken, Latest and a ConsentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val consentKey = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException()
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleFetchUserConsents = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                ClockStub()
            )
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentKey = consentKey
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given fetchUserConsents was called with a AccessToken, Latest and a ConsentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val consentKey = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            listOf("something")
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseCastFailure> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchUserConsents was called with a AccessToken, LatestConsent and a ConsentKey it returns a List of UserConsents`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val lastedConsent = true
        val consentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "potato")
        )

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.fetchUserConsents(
            accessToken = accessToken,
            latestConsent = lastedConsent,
            consentKey = consentKey
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedParameter,
            expected = mapOf(
                LATEST_CONSENT to lastedConsent,
                USER_CONSENT_KEY to consentKey
            )
        )
    }

    @Test
    fun `Given createUserConsent was called with a AccessToken and a Version it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val clock = ClockStub()
        val accessToken = "potato"
        val consentKey = "custom-consent-key"
        val version = 23

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException()
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleCreateUserConsent = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        clock.whenNow = { Instant.DISTANT_FUTURE }

        // Then
        val result = assertFailsWith<RuntimeException> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                clock
            )
            service.createUserConsent(
                accessToken = accessToken,
                consentKey = consentKey,
                version = version
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given createUserConsent was called with a AccessToken and a Version it just runs`() = runBlockingTest {
        // Given
        val clock = ClockStub()

        val accessToken = "potato"
        val consentKey = "custom-consent-key"
        val version = 23

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val expectedTime = Instant.DISTANT_PAST
        val response = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "potato")
        )

        clock.whenNow = { expectedTime }

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            clock
        )
        val result = service.createUserConsent(
            accessToken = accessToken,
            consentKey = consentKey,
            version = version
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.POST
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(CallBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentCreationPayload(
                consentKey,
                version,
                expectedTime.toString()
            )
        )
    }

    @Test
    fun `Given requestSignatureRegistration was called with a AccessToken and a Message it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException()
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRequestSignatureConsentRegistration = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                ClockStub()
            )
            service.requestSignatureConsentRegistration(
                accessToken = accessToken,
                message = message
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given requestSignatureConsentRegistration was called with a AccessToken and a Message it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            listOf("something")
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseCastFailure> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.requestSignatureConsentRegistration(
                accessToken = accessToken,
                message = message
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given requestSignatureConsentRegistration was called with a AccessToken and a Message it returns a ConsentSignature`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = DummyData.consentSignature

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.requestSignatureConsentRegistration(
            accessToken = accessToken,
            message = message
        )

        // Then
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.POST
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.addAll(listOf(USER_CONSENTS, DEFAULT_DONATION_CONSENT_KEY, SIGNATURES))
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(CallBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentSigningRequest(
                DEFAULT_DONATION_CONSENT_KEY,
                message,
                ConsentSignatureType.CONSENT_ONCE.apiValue
            )
        )
        assertSame(
            actual = result,
            expected = response
        )
    }

    @Test
    fun `Given requestSignatureDonation was called with a AccessToken and a Message it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException()
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRequestSignatureDonation = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                ClockStub()
            )
            service.requestSignatureDonation(
                accessToken = accessToken,
                message = message
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given requestSignatureDonation was called with a AccessToken and a Message it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            listOf("something")
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseCastFailure> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.requestSignatureDonation(
                accessToken = accessToken,
                message = message
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given requestSignatureDonation was called with a AccessToken and a Message it returns a ConsentSignature`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = DummyData.consentSignature

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.requestSignatureDonation(
            accessToken = accessToken,
            message = message
        )

        // Then
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.PUT
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.addAll(listOf(USER_CONSENTS, DEFAULT_DONATION_CONSENT_KEY, SIGNATURES))
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(CallBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentSigningRequest(
                DEFAULT_DONATION_CONSENT_KEY,
                message,
                ConsentSignatureType.NORMAL_USE.apiValue
            )
        )
        assertSame(
            actual = result,
            expected = response
        )
    }

    @Test
    fun `Given revokeUserConsent was called with a AccessToken it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val consentKey = "custom-consent-key"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException()
        var capturedError: HttpRuntimeError? = null

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRevokeUserConsent = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        CallBuilderSpy.onExecute = { _, _ ->
            throw error
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            // When
            val service = ConsentService(
                CallBuilderSpy.getInstance(
                    Environment.DEV,
                    createDefaultMockClient()
                ),
                errorHandler,
                ClockStub()
            )
            service.revokeUserConsent(
                accessToken = accessToken,
                consentKey = consentKey
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given revokeUserConsent was called with a AccessToken it just runs`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val consentKey = "custom-consent-key"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "potato")
        )

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService(
            CallBuilderSpy.getInstance(
                Environment.DEV,
                createDefaultMockClient()
            ),
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.revokeUserConsent(accessToken = accessToken, consentKey = consentKey)

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.DELETE
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(CallBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentRevocationPayload(consentKey)
        )
    }
}
