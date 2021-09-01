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

package care.data4life.datadonation.donation.donorkeystorage

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.DonationDataContract
import care.data4life.datadonation.EncodedDonorIdentity
import care.data4life.datadonation.RecordId
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract.Companion.DATA_DONATION_ANNOTATION
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract.Companion.PROGRAM_ANNOTATION_PREFIX
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.DonorKey
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/*
* Please note the provider does not share the same Context/Scope/Thread as the SDK.
* This means the SDK needs to transfer any values from the Context/Scope/Thread of the provider
* into it's own. Additionally Closures in Swift are not blocking.
* Since the SDK Context/Scope/Thread is known and using Atomics like constant values is safe, the
* SDK is able to launch a new coroutine.
* The channel then makes the actual transfer from the provider Context/Scope/Thread into the
* SDK Context/Scope/Thread. Also Channels are blocking which then take care of any async delay caused
* by the coroutine of the Callbacks or Swift.
*/
internal class DonorKeyStorageRepository(
    private val provider: DataDonationSDK.DonorKeyStorageProvider,
    scope: CoroutineScope
) : DonorKeyStorageRepositoryContract {
    private val scope = AtomicReference(scope)

    private fun mapLoadResult(result: Any): Donor? {
        return when (result) {
            Unit -> null
            is Donor -> result
            is Exception -> throw DonorKeyStorageError.KeyLoadingError(result)
            else -> throw DonorKeyStorageError.KeyLoadingError()
        }
    }

    override suspend fun load(programName: String): Donor? {
        val incoming = Channel<Any>()
        val annotations = setOf(
            "${PROGRAM_ANNOTATION_PREFIX}$programName",
            DATA_DONATION_ANNOTATION
        )

        provider.load(
            annotations = annotations.freeze(),
            onSuccess = { recordId: RecordId, encodedDonorIdentity: EncodedDonorIdentity ->
                scope.get().launch {
                    incoming.send(
                        Donor(
                            recordId = recordId,
                            donorIdentity = encodedDonorIdentity,
                            programName = programName
                        )
                    )
                }
                Unit
            }.freeze(),
            onNotFound = {
                scope.get().launch {
                    incoming.send(Unit)
                }
                Unit
            }.freeze(),
            onError = { error: Exception ->
                scope.get().launch {
                    incoming.send(error)
                }
                Unit
            }.freeze()
        )

        return mapLoadResult(incoming.receive())
    }

    private fun mapDonorToDonorKey(newDonor: NewDonor): DonationDataContract.DonorKey {
        return DonorKey(
            recordId = newDonor.recordId,
            data = newDonor.donorIdentity,
            annotations = setOf(
                "${PROGRAM_ANNOTATION_PREFIX}${newDonor.programName}",
                DATA_DONATION_ANNOTATION
            )
        )
    }

    private fun mapSaveResult(result: Any) {
        return when (result) {
            is Unit -> result
            is Exception -> throw DonorKeyStorageError.KeySavingError(result)
            else -> throw DonorKeyStorageError.KeySavingError()
        }
    }

    override suspend fun save(newDonor: NewDonor) {
        val incoming = Channel<Any>()

        val donorKey = mapDonorToDonorKey(newDonor)

        provider.save(
            donorKey = donorKey.freeze(),
            onSuccess = {
                scope.get().launch {
                    incoming.send(Unit)
                }
                Unit
            }.freeze(),
            onError = { error: Exception ->
                scope.get().launch {
                    incoming.send(error)
                }

                Unit
            }.freeze()
        )

        return mapSaveResult(incoming.receive())
    }

    private fun mapDeleteResult(result: Any) {
        return when (result) {
            is Unit -> result
            is Exception -> throw DonorKeyStorageError.KeyDeletionError(result)
            else -> throw DonorKeyStorageError.KeyDeletionError()
        }
    }

    override suspend fun delete(donor: Donor) {
        val incoming = Channel<Any>()

        provider.delete(
            recordId = donor.recordId.freeze(),
            onSuccess = {
                scope.get().launch {
                    incoming.send(Unit)
                }
                Unit
            }.freeze(),
            onError = { error: Exception ->
                scope.get().launch {
                    incoming.send(error)
                }
                Unit
            }.freeze()
        )

        return mapDeleteResult(incoming.receive())
    }
}
