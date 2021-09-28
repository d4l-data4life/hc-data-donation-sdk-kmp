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

@Serializable(with = BlurFunctionSerializer::class)
enum class BlurFunction(val value: String) {
    START_OF_DAY("startOfDay"),
    END_OF_DAY("endOfDay"),
    START_OF_WEEK("startOfWeek"),
    END_OF_WEEK("endOfWeek"),
    START_OF_MONTH("startOfMonth"),
    END_OF_MONTH("endOfMonth")
}

@Serializable(with = RevocationModeSerializer::class)
enum class RevocationMode(val value: String) {
    DELETE("delete"),
    ANONYMIZE("anonymize")
}

@Serializable
internal data class QuestionnaireResponseItemBlur(
    val linkId: String,
    @SerialName("fn")
    @Contextual
    val function: BlurFunction
)

@Serializable
internal data class QuestionnaireResponseBlur(
    @SerialName("location")
    val targetTimeZone: String? = null,
    @Contextual
    @SerialName("authored")
    val questionnaireResponseAuthored: BlurFunction? = null,
    @SerialName("items")
    val questionnaireResponseItemBlurs: List<QuestionnaireResponseItemBlur>
)

@Serializable
internal data class FhirResourceConfiguration(
    val url: String,
    val versions: List<String>? = null,
    @SerialName("blur")
    val fhirBlur: QuestionnaireResponseBlur? = null
)

@Serializable
internal data class ProgramBlur(
    @SerialName("location")
    val targetTimeZone: String,
    @Contextual
    @SerialName("authored")
    val questionnaireResponseAuthored: BlurFunction? = null,
    @Contextual
    val researchSubject: BlurFunction? = null
)

@Serializable
internal data class ProgramConfiguration(
    @SerialName("blur")
    val programBlur: ProgramBlur? = null
)

@Serializable
internal data class ProgramDonationConfiguration(
    val consentKey: String,
    @SerialName("resources")
    val fhirResourceConfigurations: List<FhirResourceConfiguration>,
    @SerialName("anonymization")
    val programConfiguration: ProgramConfiguration? = null,
    val triggerList: List<String>? = null,
    val delay: Double,
    val studyID: String,
    @Contextual
    val revocation: RevocationMode = RevocationMode.DELETE
)

@Serializable
internal data class Program(
    val name: String,
    val slug: String,
    val tenantID: String,
    @SerialName("donation")
    val configuration: ProgramDonationConfiguration? = null
)
