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
import care.data4life.datadonation.core.listener.listenerModule
import care.data4life.datadonation.encryption.EncryptionContract
import care.data4life.datadonation.encryption.hybrid.HybridEncryptionRegistry
import care.data4life.datadonation.internal.data.service.ConsentService
import care.data4life.datadonation.internal.data.service.DonationService
import care.data4life.datadonation.internal.data.service.ServiceContract
import care.data4life.datadonation.internal.data.storage.*
import care.data4life.datadonation.internal.domain.repository.ConsentDocumentRepository
import care.data4life.datadonation.internal.domain.repository.CredentialsRepository
import care.data4life.datadonation.internal.domain.repository.DonationRepository
import care.data4life.datadonation.internal.domain.repository.RegistrationRepository
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.internal.domain.repository.ServiceTokenRepository
import care.data4life.datadonation.internal.domain.repository.UserConsentRepository
import care.data4life.datadonation.internal.domain.usecases.*
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
            coreModule(),
            listenerModule(configuration),
            storageModule(),
            usecaseModule()
        )
    }
}

internal fun resolveRootModule(configuration: Contract.Configuration): Module {
    return module {
        single { configuration }
        // TODO: Pull this out into storage
        single<StorageContract.CredentialsDataRemoteStorage> {
            object : StorageContract.CredentialsDataRemoteStorage {
                override fun getDataDonationPublicKey(): String =
                    configuration.getServicePublicKey(Contract.Service.DD)

                override fun getAnalyticsPlatformPublicKey(): String =
                    configuration.getServicePublicKey(Contract.Service.ALP)
            }
        }
        // TODO: Pull this out into storage
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
        single<EncryptionContract.HybridEncryptionRegistry> {
            HybridEncryptionRegistry(get())
        } bind EncryptionContract.HybridEncryptionRegistry::class

        // Services
        single<ServiceContract.ConsentService> {
            ConsentService(get(), get())
        } bind ServiceContract.ConsentService::class
        single<ServiceContract.DonationService> {
            DonationService(get(), get())
        } bind ServiceContract.DonationService::class

        // Repositories
        single<RepositoryContract.UserConsentRepository> {
            UserConsentRepository(get(), get())
        } bind RepositoryContract.UserConsentRepository::class
        single<RepositoryContract.RegistrationRepository> {
            RegistrationRepository(get())
        } bind RepositoryContract.RegistrationRepository::class
        single<RepositoryContract.ConsentDocumentRepository> {
            ConsentDocumentRepository(get(), get())
        } bind RepositoryContract.ConsentDocumentRepository::class
        single { CredentialsRepository(get()) }
        single<RepositoryContract.DonationRepository> {
            DonationRepository(get())
        } bind RepositoryContract.DonationRepository::class
        single<RepositoryContract.ServiceTokenRepository> {
            ServiceTokenRepository(get())
        } bind RepositoryContract.ServiceTokenRepository::class
    }
}

internal expect fun platformModule(): Module

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
