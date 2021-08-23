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

import care.data4life.hl7.fhir.stu3.codesystem.ResearchSubjectStatus
import care.data4life.hl7.fhir.stu3.model.Extension
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.Identifier
import care.data4life.hl7.fhir.stu3.model.Meta
import care.data4life.hl7.fhir.stu3.model.Narrative
import care.data4life.hl7.fhir.stu3.model.Period
import care.data4life.hl7.fhir.stu3.model.Reference

internal data class DataDonationResearchSubject(
    override val id: String,
    override val implicitRules: String,
    override val language: String,
    override val meta: Meta,
    override val contained: List<FhirResource>,
    override val extension: List<Extension>,
    override val modifierExtension: List<Extension>,
    override val text: Narrative,
    override val resourceType: String,

    override val status: ResearchSubjectStatus,
    override val study: Reference,
    override val actualArm: String,
    override val assignedArm: String,
    override val consent: Reference,
    override val identifier: Identifier,
    override val individual: Reference,
    override val period: Period,
) : DataDonationFhirModelContract.ResearchSubject
