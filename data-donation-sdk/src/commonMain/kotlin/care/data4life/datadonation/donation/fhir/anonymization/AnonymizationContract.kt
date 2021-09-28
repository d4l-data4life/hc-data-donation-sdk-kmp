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

import care.data4life.datadonation.donation.fhir.AllowedReference
import care.data4life.datadonation.donation.fhir.anonymization.model.BlurModelContract
import care.data4life.datadonation.donation.fhir.anonymization.model.QuestionnaireResponseBlurRule
import care.data4life.datadonation.donation.fhir.anonymization.model.ResearchSubjectBlurRule
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.FhirWrapper
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponse
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.ResearchSubject
import care.data4life.datadonation.donation.program.model.BlurFunctionReference
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.hl7.fhir.FhirVersion
import care.data4life.hl7.fhir.common.datetime.XsDateTime

internal typealias TargetTimeZone = String

// TODO Move type cast if necessary into the Anonymizer
internal interface AnonymizationContract {
    fun interface Redactor {
        fun redact(valueString: String?): String?

        companion object {
            const val REDACTED = "REDACTED"
        }
    }

    interface QuestionnaireResponseBlurRuleResolver {
        fun resolveBlurRule(
            questionnaireResponse: QuestionnaireResponse<out FhirVersion, out FhirVersion, out FhirVersion, out FhirVersion>,
            programRule: ProgramBlur?,
            fhirResourceConfigurations: Map<AllowedReference, QuestionnaireResponseBlur?>
        ): QuestionnaireResponseBlurRule?
    }

    interface ResearchSubjectBlurRuleResolver {
        fun resolveBlurRule(
            programRule: ProgramBlur?,
        ): ResearchSubjectBlurRule?
    }

    fun interface DateTimeConcealer {
        fun blur(
            fhirDateTime: XsDateTime,
            targetTimeZone: TargetTimeZone,
            functionReference: BlurFunctionReference
        ): XsDateTime
    }

    interface QuestionnaireResponseAnonymizer {
        fun anonymize(
            questionnaireResponse: QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
            programType: ProgramType,
            rule: BlurModelContract.QuestionnaireResponseBlurRule?
        ): QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>
    }

    interface ResearchSubjectAnonymizer {
        fun anonymize(
            researchSubject: ResearchSubject<FhirVersion, FhirVersion, FhirVersion>,
            rule: BlurModelContract.ResearchSubjectBlurRule?
        ): ResearchSubject<FhirVersion, FhirVersion, FhirVersion>
    }

    interface FhirAnonymizer {
        fun anonymize(
            fhirResource: FhirWrapper<FhirVersion>,
            programType: ProgramType,
            globalProgramRule: ProgramBlur?,
            localResourceRule: Map<AllowedReference, QuestionnaireResponseBlur?>
        ): FhirWrapper<FhirVersion>
    }
}
