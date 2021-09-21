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

package care.data4life.datadonation.networking.plugin

import care.data4life.sdk.util.test.coroutine.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HttpCustomContentTypeTest {
    @Test
    fun `It fulfils HttpClientFeature`() {
        val feature: Any = HttpCustomContentType

        assertTrue(feature is HttpClientFeature<*, *>)
    }

    @Test
    fun `It has a key`() {
        assertEquals(
            actual = HttpCustomContentType.key.name,
            expected = "HttpCustomContentType"
        )
    }

    @Test
    fun `Given the plugin was intalled without adding a ReplacementHeader or AmendmentHeader it does nothing`() {
        val client = HttpClient(MockEngine) {
            // Given
            install(HttpCustomContentType)

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf("*/*")
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com")
        }
    }

    @Test
    fun `Given the plugin was intalled with a ReplacementHeader it ignores the header, if no content was given`() {
        val client = HttpClient(MockEngine) {
            // Given
            install(HttpCustomContentType) {
                this.replacementHeader = "X-CustomType"
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf("*/*")
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com")
        }
    }

    @Test
    fun `Given the plugin was intalled with a ReplacementHeader it replaces the ContentType header, if the ReplacementHeader contains a value`() {
        // Given
        val replacementHeader = "X-CustomType"
        val expected = "text/plain"

        val client = HttpClient(MockEngine) {

            install(HttpCustomContentType) {
                this.replacementHeader = replacementHeader
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf(expected)
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com") {
                this.header(replacementHeader, expected)
            }
        }
    }

    @Test
    fun `Given the plugin was intalled with a ReplacementHeader it replaces the ContentType header, with multiple values in lexical order`() {
        // Given
        val replacementHeader = "X-CustomType"
        val expected = listOf("text/plain", "application/json")

        val client = HttpClient(MockEngine) {

            install(HttpCustomContentType) {
                this.replacementHeader = replacementHeader
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to expected.sorted()
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com") {
                this.header(replacementHeader, expected)
            }
        }
    }

    @Test
    fun `Given the plugin was intalled with a AmendmentHeader it ignores the header, if no content was given`() {
        val client = HttpClient(MockEngine) {
            // Given
            install(HttpCustomContentType) {
                this.amendmentHeader = "X-CustomType"
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf("*/*")
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com")
        }
    }

    @Test
    fun `Given the plugin was intalled with a AmendmentHeader it replaces the ContentType header, if the AmendmentHeader contains a value`() {
        // Given
        val replacementHeader = "X-CustomType"
        val expected = "text/plain"

        val client = HttpClient(MockEngine) {

            install(HttpCustomContentType) {
                this.amendmentHeader = replacementHeader
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to listOf("*/*", expected)
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com") {
                this.header(replacementHeader, expected)
            }
        }
    }

    @Test
    fun `Given the plugin was intalled with a AmendmentHeader it replaces the ContentType header, with multiple values in lexical order`() {
        // Given
        val replacementHeader = "X-CustomType"
        val expected = listOf("text/plain", "application/json")

        val client = HttpClient(MockEngine) {

            install(HttpCustomContentType) {
                this.amendmentHeader = replacementHeader
            }

            engine {
                addHandler { request ->
                    // Then
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept-Charset" to listOf("UTF-8"),
                            "Accept" to expected.toMutableList()
                                .also { it.add("*/*") }
                                .sorted()
                        )
                    )

                    respondOk("Never mind")
                }
            }
        }

        // When
        runBlockingTest {
            client.get<String>("example.com") {
                this.header(replacementHeader, expected)
            }
        }
    }
}
