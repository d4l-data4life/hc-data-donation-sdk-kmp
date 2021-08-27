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

package care.data4life.datadonation.mock.stub.donation.donorkeystorage

import care.data4life.datadonation.Annotations
import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.DonationDataContract
import care.data4life.datadonation.EncodedDonorIdentity
import care.data4life.datadonation.RecordId
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class DonorKeyStorageProviderStub : DataDonationSDK.DonorKeyStorageProvider, MockContract.Stub {
    var whenLoad: ((Annotations, (RecordId, EncodedDonorIdentity) -> Unit, (Exception) -> Unit) -> Unit)? = null
    var whenSave: ((DonationDataContract.DonorKey, () -> Unit, (Exception) -> Unit) -> Unit)? = null
    var whenDelete: ((RecordId, () -> Unit, (Exception) -> Unit) -> Unit)? = null

    override fun load(
        annotations: Annotations,
        onSuccess: (recordId: RecordId, data: EncodedDonorIdentity) -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        return whenLoad?.invoke(annotations, onSuccess, onError) ?: throw MockException()
    }

    override fun save(
        donorKey: DonationDataContract.DonorKey,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        return whenSave?.invoke(donorKey, onSuccess, onError) ?: throw MockException()
    }

    override fun delete(recordId: RecordId, onSuccess: () -> Unit, onError: (error: Exception) -> Unit) {
        return whenDelete?.invoke(recordId, onSuccess, onError) ?: throw MockException()
    }

    override fun clear() {
        whenSave = null
        whenDelete = null
        whenLoad = null
    }
}
