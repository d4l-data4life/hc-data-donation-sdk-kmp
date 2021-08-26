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

package care.data4life.datadonation.donation.publickeyservice

import care.data4life.datadonation.mock.fixture.PublicKeyServiceFixture
import care.data4life.datadonation.mock.stub.donation.publickeyservice.PublicKeyServiceErrorHandlerStub
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
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class PublicKeyServiceApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils PublicKeyServiceApiService`() {
        val service: Any = PublicKeyServiceApiService(
            RequestBuilderSpy.Factory(),
            PublicKeyServiceErrorHandlerStub()
        )

        assertTrue(service is PublicKeyServiceContract.ApiService)
    }

    @Test
    fun `Given fetchPublicKeys was called it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = PublicKeyServiceError.UnexpectedFailure(error.statusCode.value)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = PublicKeyServiceErrorHandlerStub()

        errorHandler.whenHandleFetchPublicKeys = { delegatedError ->
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
        val result = assertFailsWith<PublicKeyServiceError.UnexpectedFailure> {
            // When
            PublicKeyServiceApiService(
                requestTemplate,
                errorHandler,
            ).fetchPublicKeys()
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
    fun `Given fetchPublicKeys was called, it calls the API and returns RawKeys`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val response = PublicKeyServiceFixture.sampleRawKeys

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
        val result = PublicKeyServiceApiService(
            requestTemplate,
            PublicKeyServiceErrorHandlerStub(),
        ).fetchPublicKeys()

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
            expected = listOf("mobile", "credentials.json")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
    }

    @Test
    fun `Given fetchLatestUpdate was called it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = PublicKeyServiceError.UnexpectedFailure(error.statusCode.value)
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)

        val errorHandler = PublicKeyServiceErrorHandlerStub()

        errorHandler.whenHandleFetchLatestUpdate = { delegatedError ->
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
        val result = assertFailsWith<PublicKeyServiceError.UnexpectedFailure> {
            // When
            PublicKeyServiceApiService(
                requestTemplate,
                errorHandler,
            ).fetchPublicKeyHeaders()
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
    fun `Given fetchLatestUpdate was called, it calls the API and returns the headers`() = runBlockingTest {
        // Given
        val requestTemplate = RequestBuilderSpy.Factory()

        val response = headersOf("last-modified", "Thu, 22 Jul 2021 09:48:40 GMT")

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                status = HttpStatusCode.OK,
                content = "",
                headers = response
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
        val result = PublicKeyServiceApiService(
            requestTemplate,
            PublicKeyServiceErrorHandlerStub(),
        ).fetchPublicKeyHeaders()

        // Then
        assertSame(
            actual = result,
            expected = response
        )

        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.HEAD
        )
        assertEquals(
            actual = capturedPath,
            expected = listOf("mobile", "credentials.json")
        )

        assertEquals(
            actual = requestTemplate.createdInstances,
            expected = 1
        )
    }
}
