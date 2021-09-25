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

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.model.Reference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class BlurRuleResolverTest {
    private val questionnaireResponseTemplate = QuestionnaireResponse(
        status = QuestionnaireResponseStatus.COMPLETED
    )

    @Test
    fun `It fulfils BlurFunctionReslover`() {
        val resolver: Any = BlurRuleResolver

        assertTrue(resolver is AnonymizationContract.BlurRuleResolver)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a empty List of ProgramResource it returns null`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            emptyMap()
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, a ProgramAnonymizationBlur and a empty List of ProgramResource it returns a BlurRule provided by the ProgramAnonymization`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()
        val programBlur = ProgramBlur(
            targetTimeZone = "does not matter",
            questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
            researchSubject = BlurFunction.END_OF_DAY
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            programBlur,
            emptyMap()
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = programBlur.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = programBlur.questionnaireResponseAuthored
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programBlur.researchSubject
        )
        assertEquals(
            actual = result.questionnaireResponseItems,
            expected = emptyList()
        )
    }

    @Test
    fun `Given resolveBlurRule is called with a null QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it returns null`() {
        // Given
        val questionnaireResponse = null
        val programBlur = null
        val fhirResourceBlur = mapOf(
            "this is the one|1.0.0" to QuestionnaireResponseBlur(
                targetTimeZone = "here",
                questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "42",
                        function = BlurFunction.END_OF_MONTH
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            programBlur,
            fhirResourceBlur
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it returns null if no ProgramResource match the FHIRResource`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()
        val fhirResourceBlur = mapOf(
            "this is the one|1.0.0" to QuestionnaireResponseBlur(
                targetTimeZone = "does not matter",
                questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "23",
                        function = BlurFunction.END_OF_DAY
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            fhirResourceBlur
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it returns null if the matching ProgramResource contains no location`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy(
            questionnaire = Reference(
                reference = "this is the one|1.0.0"
            )
        )
        val fhirResourceBlur = mapOf(
            "this is the one|1.0.0" to QuestionnaireResponseBlur(
                questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "42",
                        function = BlurFunction.END_OF_MONTH
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            fhirResourceBlur
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it resolves the Blur for the given FHIRResource and returns a BlurRule provided by the ProgramResource`() {
        // Given
        val reference = "this is the one|1.0.0"
        val questionnaireResponse = questionnaireResponseTemplate.copy(
            questionnaire = Reference(
                reference = reference
            )
        )
        val fhirResourceBlur = mapOf(
            "somewhere over the rainbow|0.0.0" to QuestionnaireResponseBlur(
                targetTimeZone = "does not matter",
                questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "23",
                        function = BlurFunction.END_OF_DAY
                    )
                )
            ),
            reference to QuestionnaireResponseBlur(
                targetTimeZone = "here",
                questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "42",
                        function = BlurFunction.END_OF_MONTH
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            fhirResourceBlur
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = fhirResourceBlur[reference]!!.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = fhirResourceBlur[reference]!!.questionnaireResponseAuthored
        )

        assertSame(
            actual = result.questionnaireResponseItems,
            expected = fhirResourceBlur[reference]!!.questionnaireResponseItemBlurs
        )

        assertNull(result.researchSubject)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, ProgramAnonymizationBlur and a List of ProgramResource it merges both rulessets in favour of the ProgramResource`() {
        // Given
        val reference = "this is the one|1.0.0"
        val questionnaireResponse = questionnaireResponseTemplate.copy(
            questionnaire = Reference(
                reference = reference
            )
        )
        val programBlur = ProgramBlur(
            targetTimeZone = "does not matter",
            questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
            researchSubject = BlurFunction.END_OF_DAY
        )
        val fhirResourceBlur = mapOf(
            "somewhere over the rainbow|0.0.0" to QuestionnaireResponseBlur(
                targetTimeZone = "does not matter",
                questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "23",
                        function = BlurFunction.END_OF_DAY
                    )
                )
            ),
            reference to QuestionnaireResponseBlur(
                targetTimeZone = "here",
                questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                questionnaireResponseItemBlurs = listOf(
                    QuestionnaireResponseItemBlur(
                        linkId = "42",
                        function = BlurFunction.END_OF_MONTH
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            programBlur,
            fhirResourceBlur
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = fhirResourceBlur[reference]!!.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = fhirResourceBlur[reference]!!.questionnaireResponseAuthored
        )

        assertSame(
            actual = result.questionnaireResponseItems,
            expected = fhirResourceBlur[reference]!!.questionnaireResponseItemBlurs
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programBlur.researchSubject
        )
    }
}
