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

package care.data4life.datadonation.donation.anonymization

import care.data4life.datadonation.donation.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.FhirResourceConfiguration
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramDonationConfiguration
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.model.FhirQuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse

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
            programRule: ProgramBlur?,
            fhirResourceConfigurations: List<FhirResourceConfiguration>
        ): BlurRule?

        companion object {
            const val REFERENCE_SEPARATOR = "|"
        }
    }

    fun interface DateTimeConcealer {
        fun blur(
            fhirDateTime: XsDateTime,
            targetTimeZone: TargetTimeZone,
            function: BlurFunction
        ): XsDateTime
    }

    interface QuestionnaireResponseAnonymizer {
        fun anonymize(
            questionnaireResponse: QuestionnaireResponse,
            rule: BlurRule?
        ): QuestionnaireResponse
    }

    interface FhirAnonymizer {
        fun anonymize(
            fhirResource: FhirResource,
            programConfiguration: ProgramDonationConfiguration
        ): FhirResource
    }
}
