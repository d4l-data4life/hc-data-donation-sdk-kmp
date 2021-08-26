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

package care.data4life.datadonation.donation.donationservice

import care.data4life.datadonation.donation.donationservice.model.DeletionProof
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceErrorHandlerStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.Path
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.core.toByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonationServiceApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils DonationServiceApiService`() {
        val apiService: Any = DonationServiceApiService(
            RequestBuilderSpy.Factory(),
            DonationServiceErrorHandlerStub()
        )

        assertTrue(apiService is DonationServiceContract.ApiService)
    }

    @Test
    fun `Given fetchToken was called it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = DonationServiceError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = DonationServiceErrorHandlerStub()

        errorHandler.whenHandleFetchToken = { delegatedError ->
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
        val result = assertFailsWith<DonationServiceError.UnexpectedFailure> {
            DonationServiceApiService(
                requestTemplate,
                errorHandler
            ).fetchToken()
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
    fun `Given fetchToken was called it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val client = HttpMockClientFactory.createMockClientWithResponse(listOf(object {})) { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "23"
            )
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            // When
            DonationServiceApiService(
                requestTemplate,
                DonationServiceErrorHandlerStub()
            ).fetchToken()
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchToken was called it returns a Token`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val token = "potato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client =
            HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
                return@createMockClientWithResponse scope.respond(
                    content = token
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
        val result = DonationServiceApiService(
            requestTemplate,
            DonationServiceErrorHandlerStub()
        ).fetchToken()

        // Then
        assertEquals(
            actual = result,
            expected = token
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("donation", "api", "v1", "token")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
    }

    @Test
    fun `Given register was called with EncryptedJSON it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = DonationServiceError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = DonationServiceErrorHandlerStub()

        errorHandler.whenHandleFetchToken = { delegatedError ->
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
        val result = assertFailsWith<DonationServiceError.UnexpectedFailure> {
            DonationServiceApiService(
                requestTemplate,
                errorHandler
            ).register("secret".toByteArray())
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
    fun `Given register was called with EncryptedJSON it just runs`() = runBlockingTest {
        // Given
        val body = "secret".toByteArray()
        val requestTemplate = RequestBuilderSpy.Factory()

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client =
            HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
                return@createMockClientWithResponse scope.respond(
                    content = "any"
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
        val result = DonationServiceApiService(
            requestTemplate,
            DonationServiceErrorHandlerStub()
        ).register(body)

        // Then
        assertEquals(
            actual = result,
            expected = Unit
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.PUT
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("donation", "api", "v1", "register")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
            expected = body
        )
    }

    @Test
    fun `Given donate was called with MultipartData it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = DonationServiceError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = DonationServiceErrorHandlerStub()

        errorHandler.whenHandleFetchToken = { delegatedError ->
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
        val result = assertFailsWith<DonationServiceError.UnexpectedFailure> {
            DonationServiceApiService(
                requestTemplate,
                errorHandler
            ).donate(MultiPartFormDataContent(emptyList()))
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
    fun `Given donate was called with MultipartData it fails on 200 reponses`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val outgoingError = DonationServiceError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "any"
            )
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        val errorHandler = DonationServiceErrorHandlerStub()

        errorHandler.whenHandleFetchToken = { delegatedError ->
            capturedError = delegatedError
            outgoingError
        }

        // Then
        val result = assertFailsWith<DonationServiceError.UnexpectedFailure> {
            DonationServiceApiService(
                requestTemplate,
                errorHandler
            ).donate(MultiPartFormDataContent(emptyList()))
        }

        // Then
        assertEquals(
            actual = capturedError?.statusCode,
            expected = HttpStatusCode.OK
        )
        assertSame(
            actual = result,
            expected = outgoingError
        )
    }

    @Test
    fun `Given donate was called with MultipartData it just runs`() = runBlockingTest {
        // Given
        val body = MultiPartFormDataContent(emptyList())
        val requestTemplate = RequestBuilderSpy.Factory()

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client =
            HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
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
        val result = DonationServiceApiService(
            requestTemplate,
            DonationServiceErrorHandlerStub()
        ).donate(body)

        // Then
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
            expected = listOf("donation", "api", "v1", "donate")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
            expected = body
        )
    }

    @Test
    fun `Given revoke was called with MultipartData it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = DonationServiceError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = DonationServiceErrorHandlerStub()

        errorHandler.whenHandleFetchToken = { delegatedError ->
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
        val result = assertFailsWith<DonationServiceError.UnexpectedFailure> {
            DonationServiceApiService(
                requestTemplate,
                errorHandler
            ).revoke(MultiPartFormDataContent(emptyList()))
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
    fun `Given revoke was called with MultipartData it fails due to unexpected response`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "23"
            )
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeError.ResponseTransformFailure> {
            DonationServiceApiService(
                requestTemplate,
                DonationServiceErrorHandlerStub()
            ).revoke(MultiPartFormDataContent(emptyList()))
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given revoke was called with MultipartData it just runs`() = runBlockingTest {
        // Given
        val body = MultiPartFormDataContent(emptyList())
        val expected = DeletionProof(
            "asd",
            "basd"
        )
        val requestTemplate = RequestBuilderSpy.Factory()

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client =
            HttpMockClientFactory.createMockClientWithResponse(listOf(expected)) { scope, _ ->
                return@createMockClientWithResponse scope.respond(
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
        val result = DonationServiceApiService(
            requestTemplate,
            DonationServiceErrorHandlerStub()
        ).revoke(body)

        // Then
        assertSame(
            actual = result,
            expected = expected
        )
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.POST
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("donation", "api", "v1", "revoke")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedBody,
            expected = body
        )
    }
}
