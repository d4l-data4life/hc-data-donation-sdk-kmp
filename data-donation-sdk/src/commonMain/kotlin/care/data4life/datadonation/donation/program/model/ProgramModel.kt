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

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProgramResourceBlurItem(
    override val linkId: String,
    @SerialName("fn")
    @Contextual
    override val function: ProgramModelContract.BlurFunction
) : ProgramModelContract.ProgramResourceBlurItem

@Serializable
internal data class ProgramResourceBlur(
    override val location: String? = null,
    @Contextual
    override val authored: ProgramModelContract.BlurFunction? = null,
    override val items: List<ProgramResourceBlurItem>
) : ProgramModelContract.ProgramResourceBlur

@Serializable
internal data class ProgramResource(
    override val url: String,
    override val versions: List<String>? = null,
    override val blur: ProgramResourceBlur? = null
) : ProgramModelContract.ProgramResource

@Serializable
internal data class ProgramAnonymizationBlur(
    override val location: String,
    @Contextual
    override val authored: ProgramModelContract.BlurFunction? = null,
    @Contextual
    override val researchSubject: ProgramModelContract.BlurFunction? = null
) : ProgramModelContract.ProgramAnonymizationBlur

@Serializable
internal data class ProgramAnonymization(
    override val blur: ProgramAnonymizationBlur? = null
) : ProgramModelContract.ProgramAnonymization

@Serializable
internal data class ProgramDonationConfiguration(
    override val consentKey: String,
    override val resources: List<ProgramResource>,
    override val anonymization: ProgramAnonymization? = null,
    override val triggerList: List<String>? = null,
    override val delay: Double,
    override val studyID: String,
    @Contextual
    override val revocation: ProgramModelContract.RevocationMode = ProgramModelContract.RevocationMode.DELETE
) : ProgramModelContract.ProgramDonationConfiguration

@Serializable
internal data class Program(
    override val name: String,
    override val slug: String,
    override val tenantID: String,
    @SerialName("donation")
    override val configuration: ProgramDonationConfiguration? = null
) : ProgramModelContract.Program
