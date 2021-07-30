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

import care.data4life.datadonation.internal.data.model.ConsentCreationPayload
import care.data4life.datadonation.internal.data.model.ConsentRevocationPayload
import care.data4life.datadonation.internal.data.model.ConsentSignature
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
import care.data4life.datadonation.lang.ConsentServiceError
import care.data4life.datadonation.lang.CoreRuntimeError
import care.data4life.datadonation.lang.HttpRuntimeError
import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleConsentDocument
import care.data4life.datadonation.mock.fixture.ConsentFixtures.sampleUserConsent
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.service.ConsentErrorHandlerStub
import care.data4life.datadonation.mock.stub.service.networking.RequestBuilderSpy
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createErrorMockClient
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createMockClientWithResponse
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils ConsentService`() {
        val service: Any = ConsentService(
            RequestBuilderSpy.Factory(),
            ConsentErrorHandlerStub(),
            ClockStub()
        )

        assertTrue(service is ServiceContract.ConsentService)
    }

    @Test
    fun `Given createUserConsent was called with a AccessToken and a Version it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val clock = ClockStub()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val version = 23

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleCreateUserConsent = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        clock.whenNow = { Instant.DISTANT_FUTURE }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            // When
            val service = ConsentService(
                requestTemplate,
                errorHandler,
                clock
            )
            service.createUserConsent(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val clock = ClockStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val version = 23

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val expectedTime = Instant.DISTANT_PAST

        clock.whenNow = { expectedTime }

        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "",
                status = HttpStatusCode.NoContent
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService(
            requestTemplate,
            ConsentErrorHandlerStub(),
            clock
        )
        val result = service.createUserConsent(
            accessToken = accessToken,
            consentDocumentKey = consentDocumentKey,
            version = version
        )

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(requestTemplate.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
            expected = ConsentCreationPayload(
                consentDocumentKey,
                version,
                expectedTime.toString()
            )
        )
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a consentDocumentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentDocumentKey = "tomato"

        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleFetchConsentDocuments = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            val service = ConsentService(
                requestTemplate,
                errorHandler,
                ClockStub()
            )
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentDocumentKey = consentDocumentKey
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
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a consentDocumentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentDocumentKey = "tomato"

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            val service = ConsentService(
                requestTemplate,
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentDocumentKey = consentDocumentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a consentDocumentKey it returns a List of ConsentDocument`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentDocumentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            sampleConsentDocument,
            sampleConsentDocument.copy(key = "soup")
        )

        val client = createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "[]"
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(dummyKtor, client)
        }

        // When
        val service = ConsentService(
            requestTemplate,
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.fetchConsentDocuments(
            accessToken = accessToken,
            version = version,
            language = language,
            consentDocumentKey = consentDocumentKey
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedParameter,
            expected = mapOf(
                "key" to consentDocumentKey,
                VERSION to version,
                LANGUAGE to language
            )
        )
    }

    @Test
    fun `Given fetchUserConsents was called with a AccessToken, Latest and a consentDocumentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleFetchUserConsents = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            // When
            val service = ConsentService(
                requestTemplate,
                errorHandler,
                ClockStub()
            )
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentDocumentKey = consentDocumentKey
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
    fun `Given fetchUserConsents was called with a AccessToken, Latest and a consentDocumentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val accessToken = "potato"
        val consentDocumentKey = "tomato"

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            val service = ConsentService(
                requestTemplate,
                ConsentErrorHandlerStub(),
                ClockStub()
            )
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentDocumentKey = consentDocumentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchUserConsents was called with a AccessToken, LatestConsent and a consentDocumentKey it returns a List of UserConsents`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val lastedConsent = true
        val consentDocumentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            sampleUserConsent,
            sampleUserConsent.copy(accountId = "potato")
        )

        val client = createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService(
            requestTemplate,
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.fetchUserConsents(
            accessToken = accessToken,
            latestConsent = lastedConsent,
            consentDocumentKey = consentDocumentKey
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedParameter,
            expected = mapOf(
                LATEST_CONSENT to lastedConsent,
                USER_CONSENT_KEY to consentDocumentKey
            )
        )
    }

    @Test
    fun `Given requestSignatureRegistration was called with a AccessToken and a Message it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val message = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRequestSignatureConsentRegistration = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            val service = ConsentService(
                requestTemplate,
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
    fun `Given a instance had been created and requestSignatureConsentRegistration was called with a AccessToken and a Message it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        val accessToken = "potato"
        val message = "tomato"

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            val service = ConsentService(
                requestTemplate,
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
    fun `Given a instance had been created and requestSignatureConsentRegistration was called with a AccessToken and a Message it returns a ConsentSignature`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = ConsentSignature("test")

        val client = createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService(
            requestTemplate,
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(requestTemplate.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val message = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRequestSignatureDonation = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            // When
            val service = ConsentService(
                requestTemplate,
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val accessToken = "potato"
        val message = "tomato"

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            val service = ConsentService(
                requestTemplate,
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = ConsentSignature("ada")

        val client = createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService(
            requestTemplate,
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(requestTemplate.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentServiceError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentErrorHandlerStub()

        errorHandler.whenHandleRevokeUserConsent = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<ConsentServiceError.Forbidden> {
            // When
            val service = ConsentService(
                requestTemplate,
                errorHandler,
                ClockStub()
            )
            service.revokeUserConsent(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey
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
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "",
                status = HttpStatusCode.NoContent
            )
        }

        requestTemplate.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService(
            requestTemplate,
            ConsentErrorHandlerStub(),
            ClockStub()
        )
        val result = service.revokeUserConsent(accessToken = accessToken, consentDocumentKey = consentDocumentKey)

        // Then
        assertSame(
            actual = result,
            expected = Unit
        )
        assertEquals(
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
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(requestTemplate.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
            expected = ConsentRevocationPayload(consentDocumentKey)
        )
    }
}
