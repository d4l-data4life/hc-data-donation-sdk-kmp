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

import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

internal class DonorKeyStorageRepositoryStub :
    DonorKeyStorageRepositoryContract,
    MockContract.Stub {
    var whenLoad: ((String) -> Donor)? = null
    var whenSave: ((NewDonor) -> Unit)? = null
    var whenDelete: ((Donor) -> Unit)? = null

    override suspend fun load(programName: String): Donor {
        return whenLoad?.invoke(programName) ?: throw MockException()
    }

    override suspend fun save(newDonor: NewDonor) {
        return whenSave?.invoke(newDonor) ?: throw MockException()
    }

    override suspend fun delete(donor: Donor) {
        return whenDelete?.invoke(donor) ?: throw MockException()
    }

    override fun clear() {
        whenLoad = null
        whenSave = null
        whenDelete = null
    }
}
