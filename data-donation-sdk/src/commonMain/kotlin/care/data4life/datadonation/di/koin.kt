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

import care.data4life.datadonation.DataDonationSDKPublicAPI
import care.data4life.datadonation.DataDonationSDKPublicAPI.Environment
import care.data4life.datadonation.consent.resolveConsentKoinModule
import care.data4life.datadonation.consentdocument.resolveConsentDocumentKoinModule
import care.data4life.datadonation.internal.data.service.resolveServiceModule
import care.data4life.datadonation.networking.plugin.resolveKtorPlugins
import care.data4life.datadonation.networking.resolveNetworking
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module

internal fun initKoin(
    environment: Environment,
    userSession: DataDonationSDKPublicAPI.UserSessionTokenProvider
): KoinApplication {
    return koinApplication {
        modules(
            resolveRootModule(
                environment,
                userSession
            ),
            resolveNetworking(),
            resolveKtorPlugins(),
            resolveConsentKoinModule(),
            resolveConsentDocumentKoinModule(),
            resolveServiceModule()
        )
    }
}

internal fun resolveRootModule(
    environment: Environment,
    userSessionTokenProvider: DataDonationSDKPublicAPI.UserSessionTokenProvider
): Module {
    return module {
        single<Environment> { environment }

        single<Clock> { Clock.System }

        single<DataDonationSDKPublicAPI.UserSessionTokenProvider> {
            userSessionTokenProvider
        }
    }
}
