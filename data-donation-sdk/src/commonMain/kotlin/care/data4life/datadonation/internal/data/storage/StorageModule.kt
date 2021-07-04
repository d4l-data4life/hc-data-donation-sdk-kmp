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

package care.data4life.datadonation.internal.data.storage

import care.data4life.datadonation.internal.domain.repository.DonationRepository
import care.data4life.datadonation.internal.domain.repository.RegistrationRepository
import care.data4life.datadonation.internal.domain.repository.RepositoryContract
import care.data4life.datadonation.internal.domain.repository.ServiceTokenRepository
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

fun storageModule(): Module {
    return module {
        single<RepositoryContract.UserConsentRemoteStorage> {
            UserConsentDataStore(get())
        } bind RepositoryContract.UserConsentRemoteStorage::class

        single<RegistrationRepository.RemoteStorage> {
            RegistrationDataStore(get())
        } bind RegistrationRepository.RemoteStorage::class

        single<RepositoryContract.ConsentDocumentRemoteStorage> {
            ConsentDocumentDataStore(get())
        } bind RepositoryContract.ConsentDocumentRemoteStorage::class

        single<DonationRepository.RemoteStorage> {
            DonationDataStore(get())
        } bind DonationRepository.RemoteStorage::class

        single<ServiceTokenRepository.RemoteStorage> {
            ServiceTokenDataStore(get())
        } bind ServiceTokenRepository.RemoteStorage::class
    }
}
