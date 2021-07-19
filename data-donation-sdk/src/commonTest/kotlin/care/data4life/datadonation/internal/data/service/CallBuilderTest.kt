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

import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.data.exception.InternalErrorException
import care.data4life.datadonation.internal.data.service.ServiceContract.CallBuilder.Companion.ACCESS_TOKEN_FIELD
import care.data4life.datadonation.internal.data.service.ServiceContract.CallBuilder.Companion.ACCESS_TOKEN_VALUE_PREFIX
import care.data4life.datadonation.mock.fake.defaultResponse
import care.data4life.datadonation.mock.fake.getDefaultMockClient
import care.data4life.sdk.util.test.runWithContextBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestData
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
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class CallBuilderTest {
    private fun createMockClientWithAssertion(assert: (HttpRequestData) -> Unit): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    assert.invoke(request)
                    defaultResponse(this)
                }
            }
        }
    }

    @Test
    fun `It fulfils CallBuilderFactory`() {
        val factory: Any = CallBuilder

        assertTrue(factory is ServiceContract.CallBuilderFactory)
    }

    @Test
    fun `Given getInstance is called with a Environment and a HttpClient it returns a CallBuilder`() {
        // Given
        val env = Environment.LOCAL
        val client = getDefaultMockClient()

        // When
        val builder: Any = CallBuilder.getInstance(env, client)

        // Then
        assertTrue(builder is ServiceContract.CallBuilder)
    }

    @Test
    fun `Given a instance was create with an Environment and it was prepared and executed ituses GET by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Get
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itcalls the given Host`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.host,
                expected = env.url
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itcalls the root by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/"
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed with a Path it calls the given path`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val path = listOf("somewhere", "in", "the", "FileTree")

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.fullPath,
                expected = "/somewhere/in/the/FileTree"
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare(path = path)
    }

    @Test
    fun `Given a instance was create with a LOCAL Environment and it was prepared and executed itcalls the given Host via HTTP`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.protocol,
                expected = URLProtocol.HTTP
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a non LOCAL Environment and it was prepared and executed itcalls the given Host via HTTP`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
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
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed ituses the default Port`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.port,
                expected = 80
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and a Port and it was prepared and executed ituses the given Port`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val port = 17

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.port,
                expected = port
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client, port)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom headers to the request by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
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
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment, setHeaders was called with Headers and it was prepared and executed itsets the given headers to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val headers = mapOf(
            "HEADER" to "head",
            "HEADER2" to "head"
        )

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
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
        val builder = CallBuilder.getInstance(env, client)
        builder.setHeaders(headers).prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom parameter to the request by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.parameters.toMap(),
                expected = emptyMap()
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment, setParameter was called with parameter and it was prepared and executed itsets custom parameter to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val parameter = mapOf(
            "PARAM" to "par",
            "PARAM2" to "par2",
        )

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.parameters.toMap(),
                expected = mapOf(
                    "PARAM" to listOf(parameter["PARAM"]),
                    "PARAM2" to listOf(parameter["PARAM2"])
                )
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setParameter(parameter).prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed ithas no AccessToken by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertFalse(request.headers.toMap().containsKey(ACCESS_TOKEN_FIELD))
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment, setParameter was called with an AccessToken and it was prepared and executed itattaches the to toke to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val token = "TOKEN"

        val env = Environment.LOCAL
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
        val builder = CallBuilder.getInstance(env, client)
        builder.setAccessToken(token).prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed itsets no custom ContentType by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
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
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment, useJsonContentType is called and it was prepared and executed itsets the ContentType for JSON to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
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

                    defaultResponse(this)
                }
            }
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.useJsonContentType().prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment and it was prepared and executed ithas no Body by default`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.body.contentLength,
                expected = 0
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.prepare().receive()
    }

    @Test
    fun `Given a instance was create with a Environment, setBody is called with a Payload and it was prepared and executed with GET, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = getDefaultMockClient()

        val error = assertFailsWith<InternalErrorException> {
            // When
            val builder = CallBuilder.getInstance(env, client)
            builder.setBody("Wups").prepare(ServiceContract.Method.GET)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "GET cannot be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was not called and it was prepared and executed with POST, it fails`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val env = Environment.LOCAL
        val client = getDefaultMockClient()

        // When
        val error = assertFailsWith<InternalErrorException> {
            // When
            val builder = CallBuilder.getInstance(env, client)
            builder.prepare(ServiceContract.Method.POST)
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
        val env = Environment.LOCAL
        val client = getDefaultMockClient()

        val error = assertFailsWith<InternalErrorException> {
            // When
            val builder = CallBuilder.getInstance(env, client)
            builder.prepare(ServiceContract.Method.PUT)
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
        val env = Environment.LOCAL
        val client = getDefaultMockClient()

        val error = assertFailsWith<InternalErrorException> {
            // When
            val builder = CallBuilder.getInstance(env, client)
            builder.prepare(ServiceContract.Method.DELETE)
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "DELETE must be combined with a RequestBody."
        )
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with POST it uses post`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Post
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.POST)
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with POST it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )
                    defaultResponse(this)
                }
            }
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.POST)
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with PUT it uses put`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Put
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.PUT)
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with PUT it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )

                    defaultResponse(this)
                }
            }
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.PUT)
    }

    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with DELETE it uses delete`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.method,
                expected = HttpMethod.Delete
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.DELETE)
    }

    @KtorExperimentalAPI
    @Test
    fun `Given a instance was create with a Environment, setBody was called with a Payload and it was prepared and executed with DELETE it attaches the body to the request`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val payload = "蕃茄湯"

        val env = Environment.LOCAL
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.body.toByteReadPacket().readText(),
                        expected = payload
                    )

                    defaultResponse(this)
                }
            }
        }

        // When
        val builder = CallBuilder.getInstance(env, client)
        builder.setBody(payload).prepare(ServiceContract.Method.DELETE)
    }

    @Test
    fun `Given a instance was create with a Environment, newBuilder was it creates a new instance with the same initial parameter`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val port = 23
        val env = Environment.DEV
        val client = createMockClientWithAssertion { request ->
            // Then
            assertEquals(
                actual = request.url.host,
                expected = env.url
            )
            assertEquals(
                actual = request.url.port,
                expected = port
            )
            assertEquals(
                actual = request.url.protocol,
                expected = URLProtocol.HTTPS
            )
        }

        // When
        val builder = CallBuilder.getInstance(env, client, port)
        val newBuilder: Any = builder.newBuilder()

        // Then
        assertTrue(newBuilder is ServiceContract.CallBuilder)
        assertNotSame(
            actual = builder,
            illegal = newBuilder
        )
        builder.prepare().receive<Any>()
        newBuilder.prepare().receive<Any>()
    }
}
