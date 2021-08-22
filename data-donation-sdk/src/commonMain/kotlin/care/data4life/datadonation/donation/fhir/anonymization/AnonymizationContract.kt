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

package care.data4life.datadonation.donation.fhir.anonymization

import care.data4life.datadonation.donation.fhir.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationGlobalBlur
import care.data4life.datadonation.donation.program.model.ProgramDonationConfiguration
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceConfiguration
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.model.FhirQuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.ResearchSubject

internal typealias TargetTimeZone = String

internal interface AnonymizationContract {
    fun interface Redactor {
        fun redact(valueString: String?): String?

        companion object {
            const val REDACTED = "REDACTED"
        }
    }

    interface BlurRuleResolver {
        fun resolveBlurRule(
            fhirResource: FhirQuestionnaireResponse?,
            programRuleGlobal: ProgramAnonymizationGlobalBlur?,
            programFhirResourceConfigurations: List<ProgramFhirResourceConfiguration>
        ): BlurRule?

        companion object {
            const val REFERENCE_SEPARATOR = "|"
        }
    }

    fun interface DateTimeSmearer {
        fun blur(
            fhirDateTime: XsDateTime,
            targetTimeZone: TargetTimeZone,
            rule: BlurFunction
        ): XsDateTime
    }

    interface QuestionnaireResponseAnonymizer {
        fun anonymize(
            questionnaireResponse: QuestionnaireResponse,
            programType: ProgramType,
            rule: BlurRule?
        ): QuestionnaireResponse
    }

    interface ResearchSubjectAnonymizer {
        fun anonymize(
            researchSubject: ResearchSubject,
            rule: BlurRule?
        ): ResearchSubject
    }

    interface FhirAnonymizer {
        fun anonymize(
            fhirResource: FhirResource,
            programType: ProgramType,
            programConfiguration: ProgramDonationConfiguration
        ): FhirResource
    }
}
