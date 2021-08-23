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

package care.data4life.datadonation.donation.fhir.model

import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.Extension
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.Identifier
import care.data4life.hl7.fhir.stu3.model.Meta
import care.data4life.hl7.fhir.stu3.model.Narrative
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("QuestionnaireResponse")
internal data class DataDonationQuestionnaireResponse(
    override val id: String,
    override val implicitRules: String,
    override val language: String,
    override val meta: Meta,
    override val contained: List<FhirResource>,
    override val extension: List<Extension>,
    override val modifierExtension: List<Extension>,
    override val text: Narrative,

    override val identifier: Identifier,
    override val basedOn: List<Reference>,
    override val parent: List<Reference>,
    override val questionnaire: Reference,
    override val status: QuestionnaireResponseStatus,
    override val subject: Reference,
    override val context: Reference,
    override val author: Reference,
    override val authored: DateTime,
    override val source: Reference,
    override val item: List<QuestionnaireResponseItem>,
) : DataDonationFhirModelContract.QuestionnaireResponse {
    override val resourceType: String
        get() = resourceType()

    companion object {
        @JvmStatic
        fun resourceType(): String = "QuestionnaireResponse"
    }
}
