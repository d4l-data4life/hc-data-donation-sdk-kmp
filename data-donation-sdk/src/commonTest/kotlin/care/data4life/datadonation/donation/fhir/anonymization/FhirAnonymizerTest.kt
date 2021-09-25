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
import care.data4life.datadonation.donation.fhir.anonymization.model.BlurRule
import care.data4life.datadonation.donation.program.model.BlurFunctionReference
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramType
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import care.data4life.datadonation.mock.stub.donation.fhir.anonymization.BlurRuleResolverStub
import care.data4life.datadonation.mock.stub.donation.fhir.anonymization.QuestionnaireResponseAnonymizerStub
import care.data4life.datadonation.mock.stub.donation.fhir.anonymization.ResearchSubjectAnonymizerStub
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.codesystem.ResearchSubjectStatus
import care.data4life.hl7.fhir.stu3.model.DomainResource
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.Reference
import care.data4life.hl7.fhir.stu3.model.ResearchSubject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FhirAnonymizerTest {
    private val programBlur = ProgramBlur(
        targetTimeZone = "does not matter",
        questionnaireResponseAuthoredBlurFunctionReference = BlurFunctionReference.START_OF_DAY,
        researchSubjectBlurFunctionReference = BlurFunctionReference.END_OF_DAY
    )
    private val fhirResourceBlur = mapOf(
        "this is the one|1.0.0" to QuestionnaireResponseBlur(
            targetTimeZone = "here",
            authoredBlurFunctionReference = BlurFunctionReference.START_OF_MONTH,
            questionnaireResponseItemBlurs = listOf(
                QuestionnaireResponseItemBlur(
                    linkId = "42",
                    blurFunctionReference = BlurFunctionReference.END_OF_MONTH
                )
            )
        )
    )

    @Test
    fun `It fulfils FhirDateTimeSmearer`() {
        val smearer: Any = FhirAnonymizer(
            BlurRuleResolverStub(),
            QuestionnaireResponseAnonymizerStub(),
            ResearchSubjectAnonymizerStub()
        )

        assertTrue(smearer is AnonymizationContract.FhirAnonymizer)
    }

    @Test
    fun `Given anonymize is called with a arbitrary FhirResource and a ProgramDonationConfiguration it reflects the Resource`() {
        // Given
        val resource = DomainResource()

        // When
        val result = FhirAnonymizer(
            BlurRuleResolverStub(),
            QuestionnaireResponseAnonymizerStub(),
            ResearchSubjectAnonymizerStub()
        ).anonymize(resource, ProgramType.STUDY, programBlur, fhirResourceBlur)

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given anonymize is called with a QuestionaireResponse and a ProgramDonationConfiguration it resolves the BlurRule and delegates the call to QuestionaireResponseAnonymizer`() {
        // Given
        val programType = ProgramType.DIARY
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
        )

        val expected = resource.copy(status = QuestionnaireResponseStatus.COMPLETED)

        val blurResolver = BlurRuleResolverStub()

        val programBlur = this.programBlur.copy()
        val fhirResourceBlur = this.fhirResourceBlur.toMap()

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        var capturedFhirResource: FhirResource? = null
        var capturedProgramBlurRule: ProgramBlur? = null
        var capturedFhirResourceBlurRule: Map<AllowedReference, QuestionnaireResponseBlur?>? = null

        blurResolver.whenResolveBlurRule = { delegatedFhirResource, delegatedGlobalRule, delegatedLocalRule ->
            capturedFhirResource = delegatedFhirResource
            capturedProgramBlurRule = delegatedGlobalRule
            capturedFhirResourceBlurRule = delegatedLocalRule

            rule
        }

        val questionnaireResponseAnonymizer = QuestionnaireResponseAnonymizerStub()

        var capturedQuestionnaireResponse: QuestionnaireResponse? = null
        var capturedProgramType: ProgramType? = null
        var capturedBlurRule: BlurModelContract.QuestionnaireResponseBlur? = null

        questionnaireResponseAnonymizer.whenAnonymize = { delegatedQuestionnaireResponse, delegatedProgramType, delegatedRule ->
            capturedQuestionnaireResponse = delegatedQuestionnaireResponse
            capturedProgramType = delegatedProgramType
            capturedBlurRule = delegatedRule

            expected
        }

        // When
        val result = FhirAnonymizer(
            blurResolver,
            questionnaireResponseAnonymizer,
            ResearchSubjectAnonymizerStub()
        ).anonymize(resource, programType, programBlur, fhirResourceBlur)

        // Then
        assertSame(
            actual = result,
            expected = expected
        )

        assertSame(
            actual = capturedFhirResource,
            expected = resource
        )
        assertSame(
            actual = capturedProgramBlurRule,
            expected = programBlur
        )
        assertSame(
            actual = capturedFhirResourceBlurRule,
            expected = fhirResourceBlur
        )

        assertSame(
            actual = capturedQuestionnaireResponse,
            expected = resource
        )
        assertSame(
            actual = capturedProgramType,
            expected = programType
        )
        assertSame(
            actual = capturedBlurRule,
            expected = rule
        )
    }

    @Test
    fun `Given anonymize is called with a ResearchSubject and a ProgramDonationConfiguration it resolves the BlurRule and delegates the call to ResearchSubjectAnonymizer`() {
        // Given
        val resource = ResearchSubject(
            status = ResearchSubjectStatus.ACTIVE,
            study = Reference(),
            individual = Reference()
        )

        val expected = resource.copy(status = ResearchSubjectStatus.COMPLETED)

        val blurResolver = BlurRuleResolverStub()

        val programBlur = this.programBlur.copy()
        val fhirResourceBlur = this.fhirResourceBlur.toMap()

        val rule = BlurRule(
            targetTimeZone = "somewhere"
        )

        var capturedFhirResource: FhirResource? = null
        var capturedProgramBlurRule: ProgramBlur? = null
        var capturedFhirResourceBlurRule: Map<AllowedReference, QuestionnaireResponseBlur?>? = null

        blurResolver.whenResolveBlurRule = { delegatedFhirResource, delegatedGlobalRule, delegatedLocalRule ->
            capturedFhirResource = delegatedFhirResource
            capturedProgramBlurRule = delegatedGlobalRule
            capturedFhirResourceBlurRule = delegatedLocalRule

            rule
        }

        val researchSubjectAnonymizer = ResearchSubjectAnonymizerStub()

        var capturedResearchSubject: ResearchSubject? = null
        var capturedBlurRule: BlurModelContract.ResearchSubjectBlur? = null

        researchSubjectAnonymizer.whenAnonymize = { delegatedResearchSubject, delegatedRule ->
            capturedResearchSubject = delegatedResearchSubject
            capturedBlurRule = delegatedRule

            expected
        }

        // When
        val result = FhirAnonymizer(
            blurResolver,
            QuestionnaireResponseAnonymizerStub(),
            researchSubjectAnonymizer
        ).anonymize(resource, ProgramType.STUDY, programBlur, fhirResourceBlur)

        // Then
        assertSame(
            actual = result,
            expected = expected
        )

        assertNull(capturedFhirResource)
        assertSame(
            actual = capturedProgramBlurRule,
            expected = programBlur
        )
        assertEquals(
            actual = capturedFhirResourceBlurRule,
            expected = emptyMap()
        )

        assertSame(
            actual = capturedResearchSubject,
            expected = resource
        )
        assertSame(
            actual = capturedBlurRule,
            expected = rule
        )
    }
}
