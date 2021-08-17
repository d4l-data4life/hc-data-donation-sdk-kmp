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

package care.data4life.datadonation.donation.program

import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.mock.fixture.ProgramFixture
import care.data4life.datadonation.mock.stub.donation.program.ProgramErrorMapperStub
import care.data4life.datadonation.mock.stub.networking.RequestBuilderSpy
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.Path
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

class ProgramApiServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @Test
    fun `It fulfils ProgramService`() {
        val service: Any = ProgramApiService(
            RequestBuilderSpy.Factory(),
            ProgramErrorMapperStub(),
        )

        assertTrue(service is ProgramContract.ApiService)
    }

    @Test
    fun `Given fetchProgram was called with a AccessToken and a ProgramName it delegates HttpRequestErrors to its ErrorHandler`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val programName = "tomato"

        val requestTemplate = RequestBuilderSpy.Factory()

        val error = HttpRuntimeError(HttpStatusCode.TooManyRequests)
        val outgoingError = RuntimeException("Test")
        var capturedError: HttpRuntimeError? = null

        val client = HttpMockClientFactory.createErrorMockClient(error)
        val errorMapper = ProgramErrorMapperStub()

        errorMapper.whenMapFetchProgram = { delegatedError ->
            capturedError = delegatedError
            throw outgoingError
        }

        requestTemplate.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val result = assertFailsWith<RuntimeException> {
            // When
            val service = ProgramApiService(
                requestTemplate,
                errorMapper,
            )
            service.fetchProgram(
                accessToken = accessToken,
                programName = programName,
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
    fun `Given fetchProgram was called with a AccessToken and a ProgramName it fails due to an unexpected Response`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val programName = "tomato"

        val requestTemplate = RequestBuilderSpy.Factory()

        val client = HttpMockClientFactory.createMockClientWithResponse { scope, _ ->
            return@createMockClientWithResponse scope.respond(
                content = "not the response you are looking for"
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
            val service = ProgramApiService(
                requestTemplate,
                ProgramErrorMapperStub(),
            )
            service.fetchProgram(
                accessToken = accessToken,
                programName = programName,
            )
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given fetchProgram was called with a AccessToken and a ProgramName it returns a Program`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val programName = "tomato"

        val requestTemplate = RequestBuilderSpy.Factory()

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = ProgramFixture.sampleProgram

        val client = HttpMockClientFactory.createMockClientWithResponse(listOf(response)) { scope, _ ->
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
        val service = ProgramApiService(
            requestTemplate,
            ProgramErrorMapperStub(),
        )
        val result = service.fetchProgram(
            accessToken = accessToken,
            programName = programName,
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
            expected = ProgramContract.ApiService.ROUTE.toMutableList().also {
                it.add(programName)
            }
        )
        assertEquals(
            actual = requestTemplate.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
    }
}
