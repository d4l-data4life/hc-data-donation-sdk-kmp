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
import care.data4life.datadonation.Result
import care.data4life.datadonation.ResultPipe
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract.Companion.DATA_DONATION_ANNOTATION
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract.Companion.PROGRAM_ANNOTATION_PREFIX
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.DonorRecord
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import care.data4life.datadonation.donation.model.DonorIdentity
import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

internal class DonorKeyStorageRepository(
    private val provider: DataDonationSDK.DonorKeyStorageProvider,
    private val serializer: Json,
    scope: CoroutineScope
) : DonorKeyStorageRepositoryContract {
    private val loadPipe = ResultPipe<DataDonationSDK.DonorKeyRecord?, Throwable>(scope)
    private val saveAndDeletePipe = ResultPipe<Unit?, Throwable>(scope)

    private fun mapDonor(
        programName: String,
        donorKeyRecord: DataDonationSDK.DonorKeyRecord
    ): Donor {
        return Donor(
            recordId = donorKeyRecord.recordId,
            donorIdentity = serializer.decodeFromString(
                DonorIdentity.serializer(),
                donorKeyRecord.data
            ),
            programName = programName
        )
    }

    private fun mapDonorOrNull(
        programName: String,
        donorKeyRecord: Any?
    ): Donor? {
        return if (donorKeyRecord is DataDonationSDK.DonorKeyRecord) {
            mapDonor(programName, donorKeyRecord)
        } else {
            null
        }
    }

    private fun mapLoadResult(
        programName: String,
        result: Result<DataDonationSDK.DonorKeyRecord?, Throwable>
    ): Donor? {
        return when (result) {
            is Result.Success<*, *> -> mapDonorOrNull(programName, result.value)
            is Result.Error<*, *> -> throw DonorKeyStorageError.KeyLoadingError(result.error)
        }
    }

    override suspend fun load(programName: String): Donor? {
        val annotations = listOf(
            "${PROGRAM_ANNOTATION_PREFIX}$programName",
            DATA_DONATION_ANNOTATION
        )

        provider.load(
            annotations = annotations.freeze(),
            pipe = loadPipe
        )

        return mapLoadResult(programName, loadPipe.receive())
    }

    private fun mapDonorToDonorKey(newDonor: NewDonor): DonationDataContract.DonorRecord {
        return DonorRecord(
            recordId = newDonor.recordId,
            data = serializer.encodeToString(
                DonorIdentity.serializer(),
                newDonor.donorIdentity
            ),
            annotations = listOf(
                "${PROGRAM_ANNOTATION_PREFIX}${newDonor.programName}",
                DATA_DONATION_ANNOTATION
            )
        )
    }

    private fun mapSaveResult(result: Result<Unit?, Throwable>) {
        return when (result) {
            is Result.Success<*, *> -> Unit
            is Result.Error<*, *> -> throw DonorKeyStorageError.KeySavingError(result.error)
        }
    }

    override suspend fun save(newDonor: NewDonor) {
        val donorKey = mapDonorToDonorKey(newDonor)

        provider.save(
            donorRecord = donorKey.freeze(),
            pipe = saveAndDeletePipe
        )

        return mapSaveResult(saveAndDeletePipe.receive())
    }

    private fun mapDeletionResult(result: Result<Unit?, Throwable>) {
        return when (result) {
            is Result.Success<*, *> -> Unit
            is Result.Error<*, *> -> throw DonorKeyStorageError.KeyDeletionError(result.error)
        }
    }

    override suspend fun delete(donor: Donor) {
        provider.delete(
            recordId = donor.recordId.freeze(),
            pipe = saveAndDeletePipe
        )

        return mapDeletionResult(saveAndDeletePipe.receive())
    }
}
