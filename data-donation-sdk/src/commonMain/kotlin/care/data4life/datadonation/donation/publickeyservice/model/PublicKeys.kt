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
package care.data4life.datadonation.donation.publickeyservice.model

internal data class PublicKeys(
    val dataDonationService: ByteArray,
    val alp: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        return when {
            other !is PublicKeys -> false
            !dataDonationService.contentEquals(other.dataDonationService) -> false
            !alp.contentEquals(other.alp) -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = dataDonationService.contentHashCode()
        result = 31 * result + alp.contentHashCode()
        return result
    }
}
