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

package care.data4life.datadonation.integration

import care.data4life.datadonation.Annotations
import care.data4life.datadonation.Client
import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.DonationDataContract
import care.data4life.datadonation.EncodedDonorIdentity
import care.data4life.datadonation.RecordId
import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.di.consentFlowDependencies
import care.data4life.datadonation.di.donationFlowDependencies
import care.data4life.datadonation.di.sharedDependencies
import care.data4life.datadonation.mock.ResourceLoader
import care.data4life.datadonation.mock.stub.ClockStub
import care.data4life.datadonation.session.SessionTokenRepositoryContract
import care.data4life.sdk.util.Base64
import care.data4life.sdk.util.test.annotation.RobolectricTestRunner
import care.data4life.sdk.util.test.annotation.RunWithRobolectricTestRunner
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import care.data4life.sdk.util.test.ktor.HttpMockClientFactory
import co.touchlab.stately.isolate.IsolateState
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class ClientDonationRegistrationFlowModuleTest {
    private fun initKoin(
        httpClient: HttpClient,
        sessionTokenProvider: DataDonationSDK.UserSessionTokenProvider,
        keyStorageProvider: DataDonationSDK.DonorKeyStorageProvider,
        cryptoService: CryptoContract.Service,
        clock: Clock? = null
    ): KoinApplication {
        val dependencies = mutableListOf<Module>()

        dependencies.addAll(
            sharedDependencies(
                DataDonationSDK.Environment.DEVELOPMENT,
                sessionTokenProvider,
                keyStorageProvider
            )
        )

        dependencies.addAll(
            consentFlowDependencies()
        )

        dependencies.addAll(
            donationFlowDependencies()
        )

        dependencies.add(
            module {
                factory(
                    override = true,
                    qualifier = named("blankHttpClient")
                ) { httpClient }
                single<CryptoContract.Service> (override = true) { cryptoService }
            }
        )

        if (clock is Clock) {
            dependencies.add(
                module {
                    single<Clock>(override = true) {
                        clock
                    }
                }
            )
        }

        return koinApplication {
            modules(dependencies)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun `Given registerDonor is called with a ProgramName and a DonorKey was added somewhere else, it just runs`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "Tomato"
        val token = "Tofu"
        val sessionToken = "vinegar"

        val encryptedJSON = "geheim"

        val httpClient = HttpMockClientFactory.createMockClientWithResponse { scope, request ->
            println(request.url.fullPath)
            when (request.url.fullPath) {
                "/program/api/v1/programs/$programName" -> {
                    assertEquals(
                        actual = request.method,
                        expected = HttpMethod.Get
                    )

                    scope.respond(
                        content = ResourceLoader.loader.load("/fixture/program/SampleProgram.json"),
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            "Content-Type" to listOf("application/json")
                        )
                    )
                }
                "/mobile/credentials.json" -> {
                    assertEquals(
                        actual = request.method,
                        expected = HttpMethod.Get
                    )

                    scope.respond(
                        content = ResourceLoader.loader.load("/fixture/publicKeyService/SamplePublicKeys.json"),
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            "Content-Type" to listOf("application/json")
                        )
                    )
                }
                "/donation/api/v1/token" -> {
                    assertEquals(
                        actual = request.method,
                        expected = HttpMethod.Get
                    )

                    scope.respond(
                        content = token,
                        status = HttpStatusCode.OK,
                    )
                }
                "/consent/api/v1/userConsents/d4l.sample/signatures" -> {
                    assertEquals(
                        actual = request.method,
                        expected = HttpMethod.Post
                    )
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Authorization" to listOf("Bearer $sessionToken"),
                            "Accept" to listOf("application/json"),
                            "Accept-Charset" to listOf("UTF-8")
                        )
                    )

                    scope.respond(
                        content = ResourceLoader.loader.load("/fixture/consentSignature/ExampleConsentSignature.json"),
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            "Content-Type" to listOf("application/json")
                        )
                    )
                }
                "/donation/api/v1/register" -> {
                    assertEquals(
                        actual = request.headers,
                        expected = headersOf(
                            "Accept" to listOf("application/json"), // TODO: This needs to be application/octet-stream, however Ktor does not provide a working setter for ContentType during Runtime, we need to build a custom Plugin for that
                            "Accept-Charset" to listOf("UTF-8")
                        )
                    )

                    launch {
                        assertEquals(
                            actual = request.body.toByteReadPacket().readText(),
                            expected = encryptedJSON
                        )
                    }

                    scope.respond(
                        content = "",
                        status = HttpStatusCode.OK
                    )
                }
                else -> throw RuntimeException("Unknown path ${request.url.fullPath}")
            }
        }

        val clock = ClockStub().also {
            it.whenNow = { Instant.fromEpochSeconds(SessionTokenRepositoryContract.CACHE_LIFETIME_IN_SECONDS.toLong() + 30) }
        }

        val encryptedPayload = IsolateState {
            mutableListOf(
                "1".encodeToByteArray(),
                encryptedJSON.encodeToByteArray()
            )
        }
        val plainPayload = IsolateState {
            mutableListOf(
                "{\"token\":\"$token\",\"donorID\":\"PublicKey\"}",
                "{\"consentMessageJSON\":\"{\\\"consentDocumentKey\\\":\\\"d4l.sample\\\",\\\"payload\\\":\\\"${Base64.encodeToString(encryptedPayload.access { it[0] })}\\\",\\\"signatureType\\\":\\\"consentOnce\\\"}\",\"signature\":\"NmjTvCdZmqgLJ95DZaLkOtN0IaBqPGVH4BUmRVf68lvMG3GmXzSpKFGCWGmMNSv4PNJDHZq/wewDPOQ54LHMQLYcBm7HMn2kyi8ImaWshizCyrHobe4euLj0NhgyguIpMCxa7gcSuZtUgDPh4Sk9s7yJjVz6tZRpSzOJkyPWGWIbIt0wBHOPNOJ3oUiPcQkXCNnyc5h/OdUnMTHyNBHNfiG/z6YIom15Wu0lAaFmd+YIDdgJgbkvXGMWtVB71N5JH9m3BTCRgaacao7th/Vk2PvW2LMqwMdBQy4XM6xXxKaTms4c7ehuJUpBqMkUp+RBuubcGISWI9BbSZxUhrIUSA==\"}"
            )
        }
        val cryptor = CryptoService { payload, publicKey ->
            assertEquals(
                actual = publicKey,
                expected = ResourceLoader.loader.load("/fixture/donation/DonationServicePublicKey.txt").trim()
            )

            assertEquals(
                actual = payload.decodeToString(),
                expected = plainPayload.access { it.removeAt(0) }
            )

            encryptedPayload.access { it.removeAt(0) }
        }

        val keyStorage = DonorKeyStorageWithKeyProvider { annotations ->
            assertEquals(
                actual = annotations,
                expected = listOf(
                    "program:Tomato",
                    "d4l-donation-key"
                )
            )
        }

        val client = Client(
            initKoin(
                httpClient,
                UserSessionTokenProvider(sessionToken),
                keyStorage,
                cryptor,
                clock
            )
        )

        // When
        client.registerDonor(programName).ktFlow.collect { result ->
            assertEquals(
                actual = result,
                expected = Unit
            )
        }
    }

    private class UserSessionTokenProvider(
        private val sessionToken: String
    ) : DataDonationSDK.UserSessionTokenProvider {
        override fun getUserSessionToken(
            onSuccess: (sessionToken: String) -> Unit,
            onError: (error: Exception) -> Unit
        ) { onSuccess(sessionToken) }
    }

    private class DonorKeyStorageWithKeyProvider(
        private val annotationAssertion: (annotations: Annotations) -> Unit
    ) : DataDonationSDK.DonorKeyStorageProvider {
        override fun load(
            annotations: Annotations,
            onSuccess: (recordId: RecordId, data: EncodedDonorIdentity) -> Unit,
            onNotFound: () -> Unit,
            onError: (error: Exception) -> Unit
        ) {
            annotationAssertion(annotations)

            onSuccess(
                "NotImportant",
                ResourceLoader.loader.load("/fixture/donation/ExampleDonorIdentity.json")
            )
        }

        override fun save(
            donorRecord: DonationDataContract.DonorRecord,
            onSuccess: () -> Unit,
            onError: (error: Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }

        override fun delete(
            recordId: RecordId,
            onSuccess: () -> Unit,
            onError: (error: Exception) -> Unit
        ) {
            TODO("Not yet implemented")
        }
    }

    private class CryptoService(
        private val valueAssertion: (ByteArray, String) -> ByteArray

    ) : CryptoContract.Service {
        override fun encrypt(payload: ByteArray, publicKey: String): ByteArray {
            return valueAssertion(payload, publicKey)
        }

        override fun sign(payload: ByteArray, privateKey: String, saltLength: Int): ByteArray {
            TODO("Not yet implemented")
        }
    }
}
