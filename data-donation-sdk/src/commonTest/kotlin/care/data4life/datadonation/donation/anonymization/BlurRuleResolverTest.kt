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

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationGlobalBlur
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceBlur
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceConfiguration
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
            emptyList()
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, a ProgramAnonymizationBlur and a empty List of ProgramResource it returns a BlurRule provided by the ProgramAnonymization`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()
        val programAnonymization = ProgramAnonymizationGlobalBlur(
            targetTimeZone = "does not matter",
            questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
            researchSubject = BlurFunction.END_OF_DAY
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            programAnonymization,
            emptyList()
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = programAnonymization.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = programAnonymization.questionnaireResponseAuthored
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programAnonymization.researchSubject
        )
        assertEquals(
            actual = result.questionnaireResponseItemBlurMapping,
            expected = emptyList()
        )
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it returns null if no ProgramResource match the FHIRResource`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()
        val programResources = listOf(
            ProgramFhirResourceConfiguration(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    targetTimeZone = "does not matter",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            programResources
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
        val programResources = listOf(
            ProgramFhirResourceConfiguration(
                url = "this is the one",
                versions = listOf("1.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "42",
                            function = BlurFunction.END_OF_MONTH
                        )
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            programResources
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it resolves the Blur for the given FHIRResource and returns a BlurRule provided by the ProgramResource`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy(
            questionnaire = Reference(
                reference = "this is the one|1.0.0"
            )
        )
        val programResources = listOf(
            ProgramFhirResourceConfiguration(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    targetTimeZone = "does not matter",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
            ProgramFhirResourceConfiguration(
                url = "this is the one",
                versions = listOf("1.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    targetTimeZone = "here",
                    questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "42",
                            function = BlurFunction.END_OF_MONTH
                        )
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            null,
            programResources
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = programResources[1].fhirBlur!!.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = programResources[1].fhirBlur!!.questionnaireResponseAuthored
        )

        assertSame(
            actual = result.questionnaireResponseItemBlurMapping,
            expected = programResources[1].fhirBlur!!.itemBlurs
        )

        assertNull(result.researchSubject)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, ProgramAnonymizationBlur and a List of ProgramResource it merges both rulessets in favour of the ProgramResource`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy(
            questionnaire = Reference(
                reference = "this is the one|1.0.0"
            )
        )
        val programAnonymization = ProgramAnonymizationGlobalBlur(
            targetTimeZone = "does not matter",
            questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
            researchSubject = BlurFunction.END_OF_DAY
        )
        val programResources = listOf(
            ProgramFhirResourceConfiguration(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    targetTimeZone = "does not matter",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
            ProgramFhirResourceConfiguration(
                url = "this is the one",
                versions = listOf("1.0.0"),
                fhirBlur = ProgramFhirResourceBlur(
                    targetTimeZone = "here",
                    questionnaireResponseAuthored = BlurFunction.START_OF_MONTH,
                    itemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "42",
                            function = BlurFunction.END_OF_MONTH
                        )
                    )
                )
            )
        )

        // When
        val result = BlurRuleResolver.resolveBlurRule(
            questionnaireResponse,
            programAnonymization,
            programResources
        )

        // Then
        assertEquals(
            actual = result!!.targetTimeZone,
            expected = programResources[1].fhirBlur!!.targetTimeZone
        )

        assertEquals(
            actual = result.questionnaireResponseAuthored,
            expected = programResources[1].fhirBlur!!.questionnaireResponseAuthored
        )

        assertSame(
            actual = result.questionnaireResponseItemBlurMapping,
            expected = programResources[1].fhirBlur!!.itemBlurs
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programAnonymization.researchSubject
        )
    }
}
