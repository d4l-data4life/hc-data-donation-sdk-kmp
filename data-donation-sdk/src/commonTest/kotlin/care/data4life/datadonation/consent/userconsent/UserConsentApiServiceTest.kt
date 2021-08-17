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

import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.consent.userconsent.UserConsentContract.ApiService.Companion.ROUTE
import care.data4life.datadonation.consent.userconsent.model.ConsentCreationPayload
import care.data4life.datadonation.consent.userconsent.model.ConsentRevocationPayload
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.fixture.UserConsentFixture.sampleUserConsent
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.consent.userconsent.UserConsentErrorHandlerStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.Path
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

class UserConsentApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils ConsentApiService`() {
        val service: Any = UserConsentApiService(
            RequestBuilderSpy.Factory(),
            UserConsentErrorHandlerStub(),
            ClockStub()
        )

        assertTrue(service is UserConsentContract.ApiService)
    }

    @Test
    fun `Given createUserConsent was called with a AccessToken and a Version it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val clock = ClockStub()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val version = "23"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = UserConsentError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = UserConsentErrorHandlerStub()

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
        val result = assertFailsWith<UserConsentError.Forbidden> {
            // When
            val service = UserConsentApiService(
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
        val version = "23"

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
        val service = UserConsentApiService(
            requestTemplate,
            UserConsentErrorHandlerStub(),
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
            expected = ROUTE
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
    fun `Given fetchUserConsents was called with a AccessToken, Latest and a consentDocumentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "tomato"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = UserConsentError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = UserConsentErrorHandlerStub()

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
        val result = assertFailsWith<UserConsentError.Forbidden> {
            // When
            val service = UserConsentApiService(
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
            val service = UserConsentApiService(
                requestTemplate,
                UserConsentErrorHandlerStub(),
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
        val service = UserConsentApiService(
            requestTemplate,
            UserConsentErrorHandlerStub(),
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
            expected = ROUTE
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
    fun `Given revokeUserConsent was called with a AccessToken it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = UserConsentError.Forbidden()
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = UserConsentErrorHandlerStub()

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
        val result = assertFailsWith<UserConsentError.Forbidden> {
            // When
            val service = UserConsentApiService(
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
        val service = UserConsentApiService(
            requestTemplate,
            UserConsentErrorHandlerStub(),
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
            expected = ROUTE
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
