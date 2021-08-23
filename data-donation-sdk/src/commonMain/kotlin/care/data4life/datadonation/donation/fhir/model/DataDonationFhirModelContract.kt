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

import care.data4life.hl7.fhir.stu3.model.Attachment
import care.data4life.hl7.fhir.stu3.model.CodeableConcept
import care.data4life.hl7.fhir.stu3.model.Extension
import care.data4life.hl7.fhir.stu3.model.FhirDomainResource
import care.data4life.hl7.fhir.stu3.model.FhirObservation
import care.data4life.hl7.fhir.stu3.model.FhirQuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.FhirResearchSubject
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.Identifier
import care.data4life.hl7.fhir.stu3.model.Meta
import care.data4life.hl7.fhir.stu3.model.Narrative
import care.data4life.hl7.fhir.stu3.model.ObservationComponent
import care.data4life.hl7.fhir.stu3.model.ObservationReferenceRange
import care.data4life.hl7.fhir.stu3.model.ObservationRelated
import care.data4life.hl7.fhir.stu3.model.Period
import care.data4life.hl7.fhir.stu3.model.Quantity
import care.data4life.hl7.fhir.stu3.model.Range
import care.data4life.hl7.fhir.stu3.model.Ratio
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.model.SampledData
import care.data4life.hl7.fhir.stu3.primitive.Bool
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import care.data4life.hl7.fhir.stu3.primitive.Instant
import care.data4life.hl7.fhir.stu3.primitive.Time
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem as Fhir3QuestionnaireResponseItem

internal interface DataDonationFhirModelContract {
    // see: https://github.com/gesundheitscloud/data-donation-sdk-js/blob/25d36dde0a7ff7761f0d6d7c74d43773d8781d2e/src/lib/document.ts#L4
    interface DomainResource : FhirDomainResource {
        override val id: String
        override val implicitRules: String
        override val language: String
        override val meta: Meta
        override val contained: List<FhirResource>
        override val extension: List<Extension>
        override val modifierExtension: List<Extension>
        override val text: Narrative
    }

    // see: https://github.com/gesundheitscloud/data-donation-sdk-js/blob/25d36dde0a7ff7761f0d6d7c74d43773d8781d2e/src/lib/document.ts#L16
    interface QuestionnaireResponse : DomainResource, FhirQuestionnaireResponse {
        override val identifier: Identifier
        override val basedOn: List<Reference>
        override val parent: List<Reference>
        override val questionnaire: Reference
        override val subject: Reference
        override val context: Reference
        override val author: Reference
        override val authored: DateTime
        override val source: Reference
        override val item: List<Fhir3QuestionnaireResponseItem>
    }

    // see: https://github.com/gesundheitscloud/data-donation-sdk-js/blob/25d36dde0a7ff7761f0d6d7c74d43773d8781d2e/src/lib/document.ts#L30
    interface ResearchSubject : DomainResource, FhirResearchSubject {
        override val identifier: Identifier
        override val period: Period
        override val individual: Reference
        override val assignedArm: String
        override val actualArm: String
        override val consent: Reference
    }

    // see: https://github.com/gesundheitscloud/data-donation-sdk-js/blob/25d36dde0a7ff7761f0d6d7c74d43773d8781d2e/src/lib/document.ts#L41
    interface Observation : DomainResource, FhirObservation {
        override val basedOn: List<Reference>
        override val bodySite: CodeableConcept
        override val category: List<CodeableConcept>
        override val comment: String
        override val component: List<ObservationComponent>
        override val context: Reference
        override val dataAbsentReason: CodeableConcept
        override val device: Reference
        override val effectiveDateTime: DateTime
        override val effectivePeriod: Period
        override val identifier: List<Identifier>
        override val interpretation: CodeableConcept
        override val issued: Instant
        override val method: CodeableConcept
        override val performer: List<Reference>
        override val referenceRange: List<ObservationReferenceRange>
        override val related: List<ObservationRelated>
        override val specimen: Reference
        override val subject: Reference
        override val valueQuantity: Quantity
        override val valueCodeableConcept: CodeableConcept
        override val valueString: String
        override val valueBoolean: Bool
        override val valueRange: Range
        override val valueRatio: Ratio
        override val valueSampledData: SampledData
        override val valueAttachment: Attachment
        override val valueDateTime: DateTime
        override val valueTime: Time
        override val valuePeriod: Period
    }
}
