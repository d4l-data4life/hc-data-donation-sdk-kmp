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

package care.data4life.datadonation.consent.consentdocument

import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract.ApiService.Companion.PATH
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.fixture.ConsentDocumentFixture.sampleConsentDocument
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentErrorHandlerStub
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentDocumentApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils ConsentDocumentApiService`() {
        val service: Any = ConsentDocumentApiService(
            RequestBuilderSpy.Factory(),
            ConsentDocumentErrorHandlerStub(),
        )

        assertTrue(service is ConsentDocumentContract.ApiService)
    }

    @Test
    fun `Given fetchConsentDocuments was called with a AccessToken, Version, Language and a consentDocumentKey it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = "23"
        val language = "zh-TW-hans-de-informal-x-old"
        val consentDocumentKey = "tomato"

        val requestTemplate = RequestBuilderSpy.Factory()
        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = ConsentDocumentError.UnexpectedFailure(23)
        var capturedError: HttpRuntimeError? = null

        val client = createErrorMockClient(error)

        val errorHandler = ConsentDocumentErrorHandlerStub()

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
        val result = assertFailsWith<ConsentDocumentError.UnexpectedFailure> {
            val service = ConsentDocumentApiService(
                requestTemplate,
                errorHandler
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
        val version = "23"
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
            val service = ConsentDocumentApiService(
                requestTemplate,
                ConsentDocumentErrorHandlerStub()
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
        val version = "23"
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
        val service = ConsentDocumentApiService(
            requestTemplate,
            ConsentDocumentErrorHandlerStub()
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
            expected = PATH
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
                "version" to version,
                "language" to language
            )
        )
    }
}
