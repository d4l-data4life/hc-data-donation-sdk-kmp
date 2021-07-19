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
import care.data4life.datadonation.internal.data.model.ConsentCreationPayload
import care.data4life.datadonation.internal.data.model.ConsentRevocationPayload
import care.data4life.datadonation.internal.data.model.ConsentSignatureType
import care.data4life.datadonation.internal.data.model.ConsentSigningRequest
import care.data4life.datadonation.internal.data.service.ServiceContract.Companion.DEFAULT_DONATION_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.Companion.LOCAL_PORT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LANGUAGE
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.LATEST_CONSENT
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.USER_CONSENT_KEY
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PARAMETER.VERSION
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.CONSENTS_DOCUMENTS
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.SIGNATURES
import care.data4life.datadonation.internal.data.service.ServiceContract.ConsentService.Companion.PATH.USER_CONSENTS
import care.data4life.datadonation.internal.data.service.networking.Networking
import care.data4life.datadonation.internal.data.service.networking.Path
import care.data4life.datadonation.lang.CoreRuntimeException
import care.data4life.datadonation.mock.DummyData
import care.data4life.datadonation.mock.fake.createDefaultMockClient
import care.data4life.datadonation.mock.fake.createMockClientWithResponse
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.mock.stub.service.networking.RequestBuilderSpy
import care.data4life.sdk.util.test.runBlockingTest
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ConsentServiceTest {
    private val dummyKtor = HttpRequestBuilder()

    @BeforeTest
    fun setUp() {
        RequestBuilderSpy.clear()
    }

    @Test
    fun `It fulfils ConsentServiceFactory`() {
        val service: Any = ConsentService

        assertTrue(service is ServiceContract.ConsentServiceFactory)
    }

    @Test
    fun `Given getInstance is called with a Environment, a HTTPClient, Clock and a requestBuilderFactory it returns a ConsentService`() {
        // Given
        val client = createDefaultMockClient()
        val env = Environment.LOCAL

        // When
        val service: Any = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())

        // Then
        assertTrue(service is ServiceContract.ConsentService)
    }

    @Test
    fun `Given getInstance is called with a non LOCAL Environment, a HTTPClient, Clock and a requestBuilderFactory it initialises a requestBuilder, while delegating the HTTPClient and Environment`() {
        // Given
        val client = createDefaultMockClient()
        val env = Environment.STAGING

        // When
        ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())

        // Then
        assertSame(
            actual = RequestBuilderSpy.delegatedClient,
            expected = client
        )
        assertSame(
            actual = RequestBuilderSpy.delegatedEnvironment,
            expected = env
        )
        assertNull(RequestBuilderSpy.delegatedPort)
    }

    @Test
    fun `Given getInstance is called with a LOCAL Environment, a HTTPClient, Clock and a requestBuilderFactory it initialises a requestBuilder, while delegating the HTTPClient, LOCAL_PORT and Environment`() {
        // Given
        val client = createDefaultMockClient()
        val env = Environment.LOCAL

        // When
        ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())

        // Then
        assertSame(
            actual = RequestBuilderSpy.delegatedClient,
            expected = client
        )
        assertSame(
            actual = RequestBuilderSpy.delegatedEnvironment,
            expected = env
        )
        assertEquals(
            actual = RequestBuilderSpy.delegatedPort,
            expected = LOCAL_PORT
        )
    }

    @Test
    fun `Given a instance had been created and fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it fails due to unexpected response`() = runBlockingTest {
        // Given
        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        RequestBuilderSpy.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeException.ResponseTransformFailure> {
            // When
            val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
            service.fetchConsentDocuments(
                accessToken = accessToken,
                version = version,
                language = language,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given a instance had been created and fetchConsentDocuments was called with a AccessToken, Version, Language and a ConsentKey it returns a List of ConsentDocument`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val version = 23
        val language = "zh-TW-hans-de-informal-x-old"
        val consentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.consentDocument,
            DummyData.consentDocument.copy(key = "soup")
        )

        val client = createMockClientWithResponse(listOf(response)) { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(dummyKtor, client)
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
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
            expected = Networking.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(CONSENTS_DOCUMENTS)
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedParameter,
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
        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val consentKey = "tomato"

        RequestBuilderSpy.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeException.ResponseTransformFailure> {
            // When
            val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
            service.fetchUserConsents(
                accessToken = accessToken,
                latestConsent = false,
                consentKey = consentKey
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given a instance had been created and fetchUserConsents was called with a AccessToken, LatestConsent and a ConsentKey it returns a List of UserConsents`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val lastedConsent = true
        val consentKey = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = listOf(
            DummyData.userConsent,
            DummyData.userConsent.copy(accountId = "potato")
        )

        val client = createMockClientWithResponse(listOf(response)) { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
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
            expected = Networking.Method.GET
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedParameter,
            expected = mapOf(
                LATEST_CONSENT to lastedConsent,
                USER_CONSENT_KEY to consentKey
            )
        )
    }

    @Test
    fun `Given a instance had been created and createUserConsent was called with a AccessToken and a Version it just runs`() = runBlockingTest {
        // Given
        val clock = ClockStub()

        val accessToken = "potato"
        val consentKey = "custom-consent-key"
        val version = 23

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val expectedTime = Instant.DISTANT_PAST

        clock.whenNow = { expectedTime }

        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "",
                status = HttpStatusCode.NoContent
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, clock)
        val result = service.createUserConsent(
            accessToken = accessToken,
            consentKey = consentKey,
            version = version
        )

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
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(RequestBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentCreationPayload(
                consentKey,
                version,
                expectedTime.toString()
            )
        )
    }

    @Test
    fun `Given a instance had been created and requestSignatureConsentRegistration was called with a AccessToken and a Message it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val message = "tomato"

        RequestBuilderSpy.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeException.ResponseTransformFailure> {
            // When
            val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
            service.requestSignatureConsentRegistration(
                accessToken = accessToken,
                message = message
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given a instance had been created and requestSignatureConsentRegistration was called with a AccessToken and a Message it returns a ConsentSignature`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = DummyData.consentSignature

        val client = createMockClientWithResponse(listOf(response)) { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
        val result = service.requestSignatureConsentRegistration(
            accessToken = accessToken,
            message = message
        )

        // Then
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.POST
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.addAll(listOf(USER_CONSENTS, DEFAULT_DONATION_CONSENT_KEY, SIGNATURES))
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(RequestBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentSigningRequest(
                DEFAULT_DONATION_CONSENT_KEY,
                message,
                ConsentSignatureType.CONSENT_ONCE.apiValue
            )
        )
        assertSame(
            actual = result,
            expected = response
        )
    }

    @Test
    fun `Given a instance had been created and requestSignatureDonation was called with a AccessToken and a Message it fails due to a unexpected response`() = runBlockingTest {
        // Given
        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        val accessToken = "potato"
        val message = "tomato"

        RequestBuilderSpy.onPrepare = { _, _ ->
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // Then
        val error = assertFailsWith<CoreRuntimeException.ResponseTransformFailure> {
            // When
            val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
            service.requestSignatureDonation(
                accessToken = accessToken,
                message = message
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unexpected Response"
        )
    }

    @Test
    fun `Given a instance had been created and requestSignatureDonation was called with a AccessToken and a Message it returns a ConsentSignature`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val message = "tomato"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null
        val response = DummyData.consentSignature

        val client = createMockClientWithResponse(listOf(response)) { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "something"
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
        val result = service.requestSignatureDonation(
            accessToken = accessToken,
            message = message
        )

        // Then
        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.PUT
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.addAll(listOf(USER_CONSENTS, DEFAULT_DONATION_CONSENT_KEY, SIGNATURES))
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(RequestBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentSigningRequest(
                DEFAULT_DONATION_CONSENT_KEY,
                message,
                ConsentSignatureType.NORMAL_USE.apiValue
            )
        )
        assertSame(
            actual = result,
            expected = response
        )
    }

    @Test
    fun `Given a instance had been created and revokeUserConsent was called with a AccessToken it just runs`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val consentKey = "custom-consent-key"

        var capturedMethod: Networking.Method? = null
        var capturedPath: Path? = null

        val client = createMockClientWithResponse { scope ->
            return@createMockClientWithResponse scope.respond(
                content = "",
                status = HttpStatusCode.NoContent
            )
        }
        val env = Environment.LOCAL

        RequestBuilderSpy.onPrepare = { method, path ->
            capturedMethod = method
            capturedPath = path
            HttpStatement(
                dummyKtor,
                client
            )
        }

        // When
        val service = ConsentService.getInstance(env, client, RequestBuilderSpy, ClockStub())
        val result = service.revokeUserConsent(accessToken = accessToken, consentKey = consentKey)

        // Then
        assertEquals(
            actual = result,
            expected = Unit
        )

        assertEquals(
            actual = capturedMethod,
            expected = Networking.Method.DELETE
        )
        assertEquals(
            actual = capturedPath,
            expected = ServiceContract.ConsentService.ROOT.toMutableList().also {
                it.add(USER_CONSENTS)
            }
        )

        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.createdInstances,
            expected = 1
        )
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedAccessToken,
            expected = accessToken
        )
        assertTrue(RequestBuilderSpy.lastInstance!!.delegatedJsonFlag)
        assertEquals(
            actual = RequestBuilderSpy.lastInstance!!.delegatedBody,
            expected = ConsentRevocationPayload(consentKey)
        )
    }
}
