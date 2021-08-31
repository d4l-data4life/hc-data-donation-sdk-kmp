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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.donation.DonationContract
import care.data4life.datadonation.donation.consentsignature.model.DeletionMessage
import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.donation.model.ConsentSigningRequest
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.fixture.ConsentSignatureFixture
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.donation.consentsignature.ConsentSignatureErrorHandlerStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.Path
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentSignatureApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils ConsentSignatureApiService`() {
        val service: Any = ConsentSignatureApiService(
            RequestBuilderSpy.Factory(),
            ConsentSignatureErrorHandlerStub()
        )

        assertTrue(service is ConsentSignatureContract.ApiService)
    }

    @Test
    fun `Given enableSigning was called with a AccessToken, a ConsentDocumentKey and a Message it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = "soup"

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentSignatureError.SigningIsAlreadyDisabled()
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = ConsentSignatureErrorHandlerStub()

        errorHandler.whenHandleEnableSigning = { delegatedError ->
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
        val result = assertFailsWith<ConsentSignatureError.SigningIsAlreadyDisabled> {
            // When
            ConsentSignatureApiService(
                requestTemplate,
                errorHandler,
            ).enableSigning(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
                signingRequest = signingRequest
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame<Any>(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given enableSigning was called with a AccessToken, a ConsentDocumentKey and a Message, it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        val clock = ClockStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = "soup"

        val expectedTime = Instant.DISTANT_PAST

        clock.whenNow = { expectedTime }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            ConsentSignatureApiService(
                requestTemplate,
                ConsentSignatureErrorHandlerStub(),
            ).enableSigning(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
                signingRequest = signingRequest
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given enableSigning was called with a AccessToken, a ConsentDocumentKey and a Message, it calls the API and returns a ConsentSignature`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = "soup"

        val response = ConsentSignatureFixture.sampleConsentSignature

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = HttpMockClientFactory.createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respondOk(
                content = ""
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
        val result = ConsentSignatureApiService(
            requestTemplate,
            ConsentSignatureErrorHandlerStub(),
        ).enableSigning(
            accessToken = accessToken,
            consentDocumentKey = consentDocumentKey,
            signingRequest = signingRequest
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )

        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.POST
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("consent", "api", "v1", "userConsents").toMutableList().also {
                it.add(consentDocumentKey)
                it.add("signatures")
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
            expected = signingRequest
        )
    }

    @Test
    fun `Given sign was called with a AccessToken, a ConsentDocumentKey and a Message it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = ConsentSigningRequest(
            consentDocumentKey = consentDocumentKey,
            payload = "soup",
            signatureType = DonationContract.ConsentSignatureType.NORMAL_USE
        )

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentSignatureError.SigningIsAlreadyDisabled()
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = ConsentSignatureErrorHandlerStub()

        errorHandler.whenHandleSigning = { delegatedError ->
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
        val result = assertFailsWith<ConsentSignatureError.SigningIsAlreadyDisabled> {
            // When
            ConsentSignatureApiService(
                requestTemplate,
                errorHandler,
            ).sign(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
                signingRequest = signingRequest
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame<Any>(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given sign was called with a AccessToken, a ConsentDocumentKey and a Message, it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }

        val clock = ClockStub()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = ConsentSigningRequest(
            consentDocumentKey = consentDocumentKey,
            payload = "soup",
            signatureType = DonationContract.ConsentSignatureType.NORMAL_USE
        )

        val expectedTime = Instant.DISTANT_PAST

        clock.whenNow = { expectedTime }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            ConsentSignatureApiService(
                requestTemplate,
                ConsentSignatureErrorHandlerStub(),
            ).sign(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
                signingRequest = signingRequest
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given sign was called with a AccessToken, a ConsentDocumentKey and a Message, it calls the API and returns a ConsentSignature`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = ConsentSigningRequest(
            consentDocumentKey = consentDocumentKey,
            payload = "soup",
            signatureType = DonationContract.ConsentSignatureType.NORMAL_USE
        )

        val response = ConsentSignatureFixture.sampleConsentSignature

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = HttpMockClientFactory.createMockClientWithResponse(listOf(response)) { scope, _ ->
            return@createMockClientWithResponse scope.respondOk(
                content = ""
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
        val result = ConsentSignatureApiService(
            requestTemplate,
            ConsentSignatureErrorHandlerStub(),
        ).sign(
            accessToken = accessToken,
            consentDocumentKey = consentDocumentKey,
            signingRequest = signingRequest
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )

        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.PUT
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("consent", "api", "v1", "userConsents").toMutableList().also {
                it.add(consentDocumentKey)
                it.add("signatures")
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
            expected = signingRequest
        )
    }

    @Test
    fun `Given disableSigning was called with a AccessToken, a ConsentDocumentKey and a SignedDeletionMessage it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = SignedDeletionMessage(
            message = DeletionMessage(
                "soup",
                DonationContract.ConsentSignatureType.REVOKE_ONCE,
                "1981",
                "abc"
            ),
            signature = "super-secret"
        )

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentSignatureError.SigningIsAlreadyDisabled()
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = ConsentSignatureErrorHandlerStub()

        errorHandler.whenHandleDisableSigning = { delegatedError ->
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
        val result = assertFailsWith<ConsentSignatureError.SigningIsAlreadyDisabled> {
            // When
            ConsentSignatureApiService(
                requestTemplate,
                errorHandler,
            ).disableSigning(
                accessToken = accessToken,
                consentDocumentKey = consentDocumentKey,
                deletionRequest = signingRequest
            )
        }

        // Then
        assertSame(
            actual = capturedError,
            expected = error
        )
        assertSame<Any>(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given disableSigning was called with a AccessToken, a ConsentDocumentKey and a SignedDeletionMessage, it calls the API and just runs`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val accessToken = "potato"
        val consentDocumentKey = "custom-consent-key"
        val signingRequest = SignedDeletionMessage(
            message = DeletionMessage(
                "soup",
                DonationContract.ConsentSignatureType.REVOKE_ONCE,
                "1981",
                "abc"
            ),
            signature = "super-secret"
        )

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
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
        val result = ConsentSignatureApiService(
            requestTemplate,
            ConsentSignatureErrorHandlerStub(),
        ).disableSigning(
            accessToken = accessToken,
            consentDocumentKey = consentDocumentKey,
            deletionRequest = signingRequest
        )

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
            expected = listOf("consent", "api", "v1", "userConsents").toMutableList().also {
                it.add(consentDocumentKey)
                it.add("signatures")
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
            expected = signingRequest
        )
    }
}
