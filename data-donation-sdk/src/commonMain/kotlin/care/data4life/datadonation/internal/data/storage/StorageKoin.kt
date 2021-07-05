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

import kotlinx.datetime.Clock
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun resolveStorageModule(): Module {
    return module {
        single<StorageContract.UserConsentRemoteStorage> {
            UserConsentDataStore(get())
        } bind StorageContract.UserConsentRemoteStorage::class

        single<StorageContract.RegistrationRemoteStorage> {
            RegistrationDataStore(get())
        } bind StorageContract.RegistrationRemoteStorage::class

        single<StorageContract.ConsentDocumentRemoteStorage> {
            ConsentDocumentDataStorage(get())
        } bind StorageContract.ConsentDocumentRemoteStorage::class

        single<StorageContract.DonationRemoteStorage> {
            DonationDataStore(get())
        } bind StorageContract.DonationRemoteStorage::class

        single<StorageContract.ServiceTokenRemoteStorage> {
            ServiceTokenDataStore(get())
        } bind StorageContract.ServiceTokenRemoteStorage::class

        single<StorageContract.CredentialsDataStorage> {
            CredentialsDataStorage(get())
        } bind StorageContract.CredentialsDataStorage::class

        single<StorageContract.UserSessionTokenDataStorage> {
            CachedUserSessionTokenDataStorage(get(), Clock.System)
        } bind StorageContract.UserSessionTokenDataStorage::class
    }
}
