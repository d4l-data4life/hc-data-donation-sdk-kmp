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
import care.data4life.datadonation.DataDonationSDK.Environment
import care.data4life.datadonation.consent.consentdocument.resolveConsentDocumentKoinModule
import care.data4life.datadonation.consent.userconsent.resolveConsentKoinModule
import care.data4life.datadonation.donation.consentsignature.resolveConsentSignatureKoinModule
import care.data4life.datadonation.donation.donationservice.resolveDonationServiceKoinModule
import care.data4life.datadonation.donation.donorkeystorage.resolveDonorKeyStorageKoinModule
import care.data4life.datadonation.donation.program.resolveProgramKoinModule
import care.data4life.datadonation.donation.publickeyservice.resolvePublicKeyServiceKoinModule
import care.data4life.datadonation.donation.resolveDonationKoinModule
import care.data4life.datadonation.networking.plugin.resolveKtorPlugins
import care.data4life.datadonation.networking.resolveNetworking
import care.data4life.datadonation.session.resolveSessionKoinModule
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

internal fun initKoin(
    environment: Environment,
    userSession: DataDonationSDK.UserSessionTokenProvider,
    keyStorage: DataDonationSDK.DonorKeyStorageProvider
): KoinApplication {
    val dependencies = mutableListOf<Module>()

    dependencies.addAll(
        sharedDependencies(environment, userSession, keyStorage)
    )

    dependencies.addAll(
        consentFlowDependencies()
    )

    dependencies.addAll(
        donationFlowDependencies()
    )

    return koinApplication {
        modules(dependencies)
    }
}

internal fun sharedDependencies(
    environment: Environment,
    userSession: DataDonationSDK.UserSessionTokenProvider,
    keyStorage: DataDonationSDK.DonorKeyStorageProvider
): List<Module> {
    return listOf(
        resolveRootModule(
            environment,
            userSession,
            keyStorage
        ),
        resolveNetworking(),
        resolveKtorPlugins(),
        resolveSessionKoinModule()
    )
}

internal fun consentFlowDependencies(): List<Module> {
    return listOf(
        resolveConsentKoinModule(),
        resolveConsentDocumentKoinModule(),
    )
}

internal fun donationFlowDependencies(): List<Module> {
    return listOf(
        resolveDonationKoinModule(),
        resolveProgramKoinModule(),
        resolvePublicKeyServiceKoinModule(),
        resolveDonorKeyStorageKoinModule(),
        resolveConsentSignatureKoinModule(),
        resolveDonationServiceKoinModule()
    )
}
