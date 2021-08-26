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

import care.data4life.datadonation.consent.consentdocument.ConsentDocumentApiService
import care.data4life.datadonation.consent.consentdocument.ConsentDocumentError
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.stub.consent.consentdocument.ConsentDocumentErrorHandlerStub
import care.data4life.datadonation.mock.stub.donation.donationservice.DonationServiceErrorHandlerStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
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
        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
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
}
