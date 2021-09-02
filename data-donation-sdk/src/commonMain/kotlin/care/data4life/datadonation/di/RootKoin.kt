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

package care.data4life.datadonation.di

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.crypto.CryptoService
import care.data4life.datadonation.crypto.KeyFactory
import care.data4life.datadonation.error.DataDonationFlowErrorMapper
import care.data4life.datadonation.util.JsonConfigurator
import care.data4life.datadonation.util.JsonConfiguratorContract
import care.data4life.sdk.flow.D4LSDKFlow
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.CoroutineScopeFactory
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun resolveRootModule(
    environment: DataDonationSDK.Environment,
    userSessionTokenProvider: DataDonationSDK.UserSessionTokenProvider,
    donorKeyStorageProvider: DataDonationSDK.DonorKeyStorageProvider
): Module {
    return module {
        single<DataDonationSDK.Environment> { environment }

        single<Clock> { Clock.System }

        single<DataDonationSDK.UserSessionTokenProvider> {
            userSessionTokenProvider
        }

        single<DataDonationSDK.DonorKeyStorageProvider> {
            donorKeyStorageProvider
        }

        single<CoroutineScope> {
            CoroutineScopeFactory.createScope("DataDonationBackgroundThreadScope")
        }

        single<DomainErrorMapperContract> {
            DataDonationFlowErrorMapper
        }

        single<D4LSDKFlowFactoryContract> {
            D4LSDKFlow
        }

        single<CryptoContract.Service> {
            CryptoService()
        }

        single<CryptoContract.KeyFactory> {
            KeyFactory
        }

        single<JsonConfiguratorContract>(named("DataDonationSerializerConfigurator")) {
            JsonConfigurator
        }

        single<Json>(named("DataDonationSerializer")) {
            Json {
                get<JsonConfiguratorContract>(named("DataDonationSerializerConfigurator"))
                    .configure(this)
            }
        }
    }
}
