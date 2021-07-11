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

package care.data4life.datadonation.internal.data.service.networking

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun resolveNetworking(): Module {
    return module {
        single<Networking.CallBuilderFactory> {
            CallBuilder
        }

        single {
            HttpClient {
                install(JsonFeature) {
                    serializer =
                        KotlinxSerializer(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                                allowSpecialFloatingPointValues = true
                                useArrayPolymorphism = false
                            }
                        )
                }
                install(Logging) {
                    logger = SimpleLogger()
                    level = LogLevel.ALL
                }
            }
        }
    }
}

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
