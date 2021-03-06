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

package care.data4life.datadonation.networking

import care.data4life.datadonation.DataDonationSDK.Environment
import care.data4life.datadonation.error.CoreRuntimeError
import care.data4life.datadonation.networking.Networking.RequestBuilder.Companion.ACCESS_TOKEN_FIELD
import care.data4life.datadonation.networking.Networking.RequestBuilder.Companion.ACCESS_TOKEN_VALUE_PREFIX
import care.data4life.datadonation.networking.plugin.HttpCustomContentType
import care.data4life.datadonation.networking.plugin.KtorPluginsContract
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory.createHelloWorldMockClient
import care.data4life.sdk.util.test.ktor.HttpMockClientResponseFactory.createHelloWorldOkResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.fullPath
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.toMap
import kotlinx.coroutines.GlobalScope
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RequestBuilderTest {
    private fun createMockClientWithAssertion(assert: (HttpRequestData) -> Unit): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    assert.invoke(request)
                    createHelloWorldOkResponse(this)
                }
            }
        }
    }

    @Test
    fun `It fulfils RequestBuilderTemplate`() {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        // When
        val template: Any = RequestBuilder.Factory(env, client)

        // Then
        assertTrue(template is Networking.RequestBuilderFactory)
    }

    @Test
    fun `Given withHost is called with Host it returns a RequestBuilderFactory`() {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        // When
        val builder: Any = RequestBuilder.Factory(env, client).withHost("somewhere")

        // Then
        assertTrue(builder is Networking.RequestBuilderFactory)
    }

    @Test
    fun `Given create is called with a Environment and a HttpClient it returns a RequestBuilder`() {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        // When
        val builder: Any = RequestBuilder.Factory(env, client).create()

        // Then
        assertTrue(builder is Networking.RequestBuilder)
    }

    @Test
    fun `Given a instance was create with an Environment and it was prepared and executed it uses GET by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Get
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed it calls the given Host`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.host,
                expected = env.url
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a overruled Host and it was prepared and executed it calls the overruled Host`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val host = "somewhere"
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.host,
                expected = host
            )
        }

        // When
        val builder = RequestBuilder.Factory(Environment.DEVELOPMENT, client)
            .withHost(host)
            .create()

        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed it calls the root by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/"
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed with a Path it calls the given path`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val path = listOf("somewhere", "in", "the", "FileTree")

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/somewhere/in/the/FileTree"
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare(path = path)
    }

    @Test
    fun `Given a instance was create with a Environment and it was executed it calls the given Host via HTTPS`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.STAGING
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.protocol,
                expected = URLProtocol.HTTPS
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed it uses the default Port`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.port,
                expected = URLProtocol.HTTPS.defaultPort
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and a Port and it was prepared and executed it uses the given Port`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val port = 17

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.port,
                expected = port
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client, port).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom headers to the request by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.headers.toMap(),
                expected = mapOf(
                    "Accept-Charset" to listOf("UTF-8"),
                    "Accept" to listOf("*/*")
                )
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setHeaders was called with Headers and it was prepared and executed itsets the given headers to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val headers = mapOf(
            "HEADER" to "head",
            "HEADER2" to "head"
        )

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals<Any>(
                actual = request.headers.toMap(),
                expected = mapOf(
                    "Accept-Charset" to listOf("UTF-8"),
                    "Accept" to listOf("*/*"),
                    "HEADER" to listOf(headers["HEADER"]),
                    "HEADER2" to listOf(headers["HEADER2"])
                )
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setHeaders(headers).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom parameter to the request by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.parameters.toMap(),
                expected = emptyMap()
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setParameter was called with parameter and it was prepared and executed itsets custom parameter to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val parameter = mapOf(
            "PARAM" to "par",
            "PARAM2" to "par2",
        )

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals<Any>(
                actual = request.url.parameters.toMap(),
                expected = mapOf(
                    "PARAM" to listOf(parameter["PARAM"]),
                    "PARAM2" to listOf(parameter["PARAM2"])
                )
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setParameter(parameter).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed ithas no AccessToken by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertFalse(request.headers.toMap().containsKey(ACCESS_TOKEN_FIELD))
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setParameter was called with an AccessToken and it was prepared and executed itattaches the to toke to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val token = "TOKEN"

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertTrue(request.headers.toMap().containsKey(ACCESS_TOKEN_FIELD))
            assertEquals(
                actual = request.headers.toMap()[ACCESS_TOKEN_FIELD]!!.size,
                expected = 1
            )
            assertEquals(
                actual = request.headers.toMap()[ACCESS_TOKEN_FIELD]!!.first(),
                expected = "$ACCESS_TOKEN_VALUE_PREFIX $token"
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setAccessToken(token).prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom ContentType by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.headers.toMap(),
                expected = mapOf(
                    "Accept-Charset" to listOf("UTF-8"),
                    "Accept" to listOf("*/*")
                )
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, useJsonContentType is called and it was prepared and executed itsets the ContentType for JSON to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        allowSpecialFloatingPointValues = true
                        useArrayPolymorphism = false
                    }
                )
            }

            install(HttpCustomContentType) {
                this.replacementHeader = KtorPluginsContract.CustomTypeHeader.replacementHeader
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers.toMap(),
                        expected = mapOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf("application/json")
                        )
                    )

                    createHelloWorldOkResponse(this)
                }
            }
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.useJsonContentType().prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed ithas no Body by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.body.contentLength,
                expected = 0
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare().execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody is called with a Payload and it was prepared and executed with GET, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        val error = assertFailsWith<CoreRuntimeError.RequestValidationFailure> {
            // When
            val builder = RequestBuilder.Factory(env, client).create()
            builder.setBody("Wups").prepare(Networking.Method.GET)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "GET cannot be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody is called with a Payload and it was prepared and executed with HEAD, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        val error = assertFailsWith<CoreRuntimeError.RequestValidationFailure> {
            // When
            val builder = RequestBuilder.Factory(env, client).create()
            builder.setBody("Wups").prepare(Networking.Method.HEAD)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "HEAD cannot be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was not called and it was prepared and executed with POST, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        // When
        val error = assertFailsWith<CoreRuntimeError.RequestValidationFailure> {
            // When
            val builder = RequestBuilder.Factory(env, client).create()
            builder.prepare(Networking.Method.POST)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "POST must be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was not called and it was prepared and executed with PUT, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        val error = assertFailsWith<CoreRuntimeError.RequestValidationFailure> {
            // When
            val builder = RequestBuilder.Factory(env, client).create()
            builder.prepare(Networking.Method.PUT)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "PUT must be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was not called and it was prepared and executed with DELETE, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createHelloWorldMockClient()

        val error = assertFailsWith<CoreRuntimeError.RequestValidationFailure> {
            // When
            val builder = RequestBuilder.Factory(env, client).create()
            builder.prepare(Networking.Method.DELETE)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "DELETE must be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed with HEAD it uses head`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Head
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.prepare(Networking.Method.HEAD).execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with POST it uses post`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Post
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.POST).execute()
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with POST it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )
                    createHelloWorldOkResponse(this)
                }
            }
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.POST).execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with PUT it uses put`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Put
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.PUT).execute()
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with PUT it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )

                    createHelloWorldOkResponse(this)
                }
            }
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.PUT).execute()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with DELETE it uses delete`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Delete
            )
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.DELETE).execute()
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with DELETE it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.DEVELOPMENT
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )

                    createHelloWorldOkResponse(this)
                }
            }
        }

        // When
        val builder = RequestBuilder.Factory(env, client).create()
        builder.setBody(payload).prepare(Networking.Method.DELETE).execute()
    }
}
