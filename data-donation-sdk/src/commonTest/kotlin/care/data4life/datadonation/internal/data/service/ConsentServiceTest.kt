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
import care.data4life.datadonation.internal.data.service.ServiceContract.Companion.LOCAL_PORT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LANGUAGE
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.VERSION
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.USER_CONSENTS
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.spy.CallBuilderSpy
import care.data4life.datadonation.mock.util.defaultResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import runBlockingTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentServiceTest {
    @BeforeTest
    fun setUp() {
        CallBuilderSpy.clear()
    }

    @Test
    fun `It fulfils ConsentServiceFactory`() {
        val service: Any = ConsentService

        assertTrue(service is ServiceContract.ConsentServiceFactory)
    }

    @Test
    fun `Given getInstance is called with a Environment, a HTTPClient and a CallBuilderFactory it returns a ConsentService`() {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        // When
        val service: Any = ConsentService.getInstance(env, client, CallBuilderSpy)

        // Then
        assertTrue(service is ServiceContract.ConsentService)
    }

    @Test
    fun `Given getInstance is called with a non LOCAL Environment, a HTTPClient and a CallBuilderFactory it initialises a CallBuilder, while delegating the HTTPClient and Environment`() {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.STAGING

        // When
        ConsentService.getInstance(env, client, CallBuilderSpy)

        // Then
        assertSame(
            actual = CallBuilderSpy.delegatedClient,
            expected = client
        )
        assertSame(
            actual = CallBuilderSpy.delegatedEnvironment,
            expected = env
        )
        assertNull(CallBuilderSpy.delegatedPort)
    }

    @Test
    fun `Given getInstance is called with a LOCAL Environment, a HTTPClient and a CallBuilderFactory it initialises a CallBuilder, while delegating the HTTPClient, LOCAL_PORT and Environment`() {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        // When
        ConsentService.getInstance(env, client, CallBuilderSpy)

        // Then
        assertSame(
            actual = CallBuilderSpy.delegatedClient,
            expected = client
        )
        assertSame(
            actual = CallBuilderSpy.delegatedEnvironment,
            expected = env
        )
        assertEquals(
            actual = CallBuilderSpy.delegatedPort,
            expected = LOCAL_PORT
        )
    }

    @Test
    fun `Given a instance had been created and fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            "Fail!"
        }

        // Then
        val error = assertFailsWith<InternalErrorException> {
            // When
            val service = ConsentService.getInstance(env, client, CallBuilderSpy)
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response."
        )
    }

    @Test
    fun `Given a instance had been created and fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it returns a List of ConsentDocument`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        var capturedMethod: ServiceContract.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.consentDocument,
            DummyData.consentDocument.copy(key = "soup")
        )

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService.getInstance(env, client, CallBuilderSpy)
        val result = service.fetchConsentDocuments(
            accessToken = accessToken,
            version = version,
            language = language,
            consentKey = consentKey
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )
        assertEquals(
            actual = capturedMethod,
            expected = ServiceContract.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedParameter,
            expected = mapOf(
                USER_CONSENT_KEY to consentKey,
                VERSION to version,
                LANGUAGE to language
            )
        )
    }

    @Test
    fun `Given a instance had been created and fetchUserConsents was called with a AccessToken, Latest and a ConsentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val consentKey = "tomato"

        CallBuilderSpy.onExecute = { _, _ ->
            "Fail!"
        }

        // Then
        val error = assertFailsWith<InternalErrorException> {
            // When
            val service = ConsentService.getInstance(env, client, CallBuilderSpy)
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response."
        )
    }

    @Test
    fun `Given a instance had been created and fetchUserConsents was called with a AccessToken, LatestConsent and a ConsentKey it returns a List of UserConsents`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) { engine { addHandler { defaultResponse() } } }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val lastedConsent = true
        val consentKey = "tomato"

        var capturedMethod: ServiceContract.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "potato")
        )

        CallBuilderSpy.onExecute = { method, path ->
            capturedMethod = method
            capturedPath = path
            response
        }

        // When
        val service = ConsentService.getInstance(env, client, CallBuilderSpy)
        val result = service.fetchUserConsents(
            accessToken = accessToken,
            latestConsent = lastedConsent,
            consentKey = consentKey
        )

        // Then
        assertSame(
            actual = result,
            expected = response
        )
        assertEquals(
            actual = capturedMethod,
            expected = ServiceContract.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = CallBuilderSpy.lastInstance!!.delegatedParameter,
            expected = mapOf(
                LATEST_CONSENT to lastedConsent,
                USER_CONSENT_KEY to consentKey
            )
        )
    }
}
