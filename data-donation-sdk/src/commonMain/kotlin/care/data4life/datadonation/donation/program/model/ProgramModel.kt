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

package care.data4life.datadonation.donation.program.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProgramItemBlur(
    override val linkId: String,
    @SerialName("fn")
    override val field: ProgramModelContract.BlurField
) : ProgramModelContract.ProgramItemBlur

@Serializable
internal data class ProgramResourceBlur(
    override val location: String? = null,
    override val authored: ProgramModelContract.BlurField? = null,
    override val items: List<ProgramItemBlur>
) : ProgramModelContract.ProgramResourceBlur

@Serializable
internal data class ProgramResource(
    override val url: String,
    override val versions: List<String>? = null,
    override val blur: ProgramResourceBlur? = null
) : ProgramModelContract.ProgramResource

@Serializable
internal data class ProgramAnonymizationBlurValue(
    override val location: String,
    override val authored: ProgramModelContract.BlurField? = null,
    override val researchSubject: ProgramModelContract.BlurField? = null
) : ProgramModelContract.ProgramAnonymizationBlurValue

@Serializable
internal data class ProgramAnonymization(
    override val blur: ProgramAnonymizationBlurValue? = null
) : ProgramModelContract.ProgramAnonymization

@Serializable
internal data class ProgramConfiguration(
    override val consentKey: String,
    override val resources: List<ProgramResource>,
    override val anonymization: ProgramAnonymization? = null,
    override val triggerList: List<String>? = null,
    override val delay: Double,
    override val studyID: String
) : ProgramModelContract.ProgramConfiguration

@Serializable
internal data class Program(
    override val name: String,
    override val slug: String,
    override val tenantID: String,
    override val donation: ProgramConfiguration? = null
) : ProgramModelContract.Program
