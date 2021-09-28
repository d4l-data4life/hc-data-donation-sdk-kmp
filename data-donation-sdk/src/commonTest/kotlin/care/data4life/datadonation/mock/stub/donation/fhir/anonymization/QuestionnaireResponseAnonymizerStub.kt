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

package care.data4life.datadonation.mock.stub.donation.fhir.anonymization

import care.data4life.datadonation.donation.fhir.anonymization.AnonymizationContract
import care.data4life.datadonation.donation.fhir.anonymization.model.BlurModelContract.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException
import care.data4life.hl7.fhir.FhirVersion

internal class QuestionnaireResponseAnonymizerStub :
    AnonymizationContract.QuestionnaireResponseAnonymizer,
    MockContract.Stub {
    var whenAnonymize: ((questionnaireResponse: CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>, ProgramType, QuestionnaireResponseBlur?) -> CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>)? = null

    override fun anonymize(
        questionnaireResponse: CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion>,
        programType: ProgramType,
        rule: QuestionnaireResponseBlur?
    ): CompatibilityWrapperContract.QuestionnaireResponse<FhirVersion, FhirVersion, FhirVersion, FhirVersion> {
        return whenAnonymize?.invoke(questionnaireResponse, programType, rule) ?: throw MockException()
    }

    override fun clear() {
        whenAnonymize = null
    }
}
