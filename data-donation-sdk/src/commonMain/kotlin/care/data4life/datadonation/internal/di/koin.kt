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
import care.data4life.datadonation.internal.domain.repositories.*
import care.data4life.datadonation.internal.domain.usecases.*
import care.data4life.datadonation.internal.utils.Base64Factory
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object DataDonationKoinContext {
    lateinit var koinApp: KoinApplication
}

internal fun initKoin(configuration: Contract.Configuration): KoinApplication {
    DataDonationKoinContext.koinApp = koinApplication {
        modules(
            module {
                single<Contract.Configuration> { configuration }
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
            },
            platformModule,
            coreModule
        )
    }
    return DataDonationKoinContext.koinApp
}

private val coreModule = module {

    single {
        HttpClient {
            install(JsonFeature) {
                serializer =
                    KotlinxSerializer(kotlinx.serialization.json.Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        allowSpecialFloatingPointValues = true
                        useArrayPolymorphism = false
                    })
            }
            install(Logging) {
                logger = SimpleLogger()
                level = LogLevel.ALL
            }
        }
    }

    //HybridEncryption
    single { HybridEncryptionRegistry(get()) }


    //Services
    single { ConsentService(get(), get()) }
    single { DonationService(get(), get()) }


    //DataStores
    single<RemoteUserConsentRepository> { UserConsentDataStore(get()) }
    single<RegistrationRepository.Remote> { RegistrationDataStore(get()) }
    single<ConsentDocumentRepository.Remote> { ConsentDocumentDataStore(get()) }
    single<DonationRepository.Remote> { DonationDataStore(get()) }
    single<ServiceTokenRepository.Remote> { ServiceTokenDataStore(get()) }


    //Repositories
    single<UserConsentRepository> { UserConsentRepositoryImpl(get(), get()) }
    single { RegistrationRepository(get()) }
    single { ConsentDocumentRepository(get(), get()) }
    single { CredentialsRepository(get()) }
    single { DonationRepository(get()) }
    single { ServiceTokenRepository(get()) }

    //Usecases
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
    }
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
    single { RevokeUserConsent(get()) }
}

expect val platformModule: Module

private class SimpleLogger : Logger {
    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
