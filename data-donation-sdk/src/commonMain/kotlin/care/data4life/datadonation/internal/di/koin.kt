/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package care.data4life.datadonation.internal.di

import care.data4life.datadonation.Contract
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionRegistry
import care.data4life.datadonation.internal.data.service.ConsentService
import care.data4life.datadonation.internal.data.service.DonationService
import care.data4life.datadonation.internal.data.store.*
import care.data4life.datadonation.internal.domain.repositories.ConsentDocumentRepository
import care.data4life.datadonation.internal.domain.repositories.CredentialsRepository
import care.data4life.datadonation.internal.domain.repositories.DonationRepository
import care.data4life.datadonation.internal.domain.repositories.RegistrationRepository
import care.data4life.datadonation.internal.domain.repositories.ServiceTokenRepository
import care.data4life.datadonation.internal.domain.repositories.UserConsentRepository
import care.data4life.datadonation.internal.domain.usecases.*
import care.data4life.datadonation.internal.utils.Base64Factory
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module

// TODO: Break down dependencies and move them in their corresponding packages
internal fun initKoin(configuration: Contract.Configuration): KoinApplication {
    return koinApplication {
        modules(
            resolveRootModule(configuration),
            platformModule(),
            coreModule()
        )
    }
}

internal fun resolveRootModule(configuration: Contract.Configuration): Module {
    return module {
        single { configuration }
        single<CredentialsDataStore> {
            object : CredentialsDataStore {
                override fun getDataDonationPublicKey(): String =
                    configuration.getServicePublicKey(Contract.Service.DD)

                override fun getAnalyticsPlatformPublicKey(): String =
                    configuration.getServicePublicKey(Contract.Service.ALP)
            }
        }
        single<UserSessionTokenDataStore> {
            CachedUserSessionTokenDataStore(get(), Clock.System)
        }
        single { configuration.getEnvironment() }
    }
}

internal fun coreModule(): Module {
    return module {
        single {
            HttpClient {
                install(JsonFeature) {
                    serializer =
                        KotlinxSerializer(
                            kotlinx.serialization.json.Json {
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

        // HybridEncryption
        single { HybridEncryptionRegistry(get()) }

        // Services
        single { ConsentService(get(), get()) }
        single { DonationService(get(), get()) }

        // DataStores
        single { UserConsentDataStore(get()) } bind UserConsentRepository.Remote::class
        single { RegistrationDataStore(get()) } bind RegistrationRepository.Remote::class
        single { ConsentDocumentDataStore(get()) }
        single { DonationDataStore(get()) } bind DonationRepository.Remote::class
        single { ServiceTokenDataStore(get()) } bind ServiceTokenRepository.Remote::class

        // Repositories
        single { UserConsentRepository(get(), get()) } bind care.data4life.datadonation.internal.domain.repositories.Contract.UserConsentRepository::class
        single { RegistrationRepository(get()) }
        single { ConsentDocumentRepository(get(), get()) } bind ConsentDocumentRepository::class
        single { CredentialsRepository(get()) }
        single { DonationRepository(get()) }
        single { ServiceTokenRepository(get()) }

        // Usecases
        single {
            CreateRequestConsentPayload(
                get(),
                get(),
                get<HybridEncryptionRegistry>().hybridEncryptionDD,
                Base64Factory.createEncoder()
            )
        }
        single {
            DonateResources(
                get(),
                get(),
                get(),
                get(),
                get<HybridEncryptionRegistry>().hybridEncryptionALP
            )
        } bind DonateResources::class
        single { FilterSensitiveInformation() }
        single {
            RegisterNewDonor(
                get(),
                get()
            )
        }
        single { FetchConsentDocuments(get()) }
        single { CreateUserConsent(get()) }
        single { FetchUserConsents(get()) }
        single { RemoveInternalInformation(kotlinx.serialization.json.Json {}) }
        single { RevokeUserConsent(get()) } bind RevokeUserConsent::class
    }
}

internal expect fun platformModule(): Module

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
