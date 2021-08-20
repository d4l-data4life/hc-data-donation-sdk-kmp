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
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationBlur
import care.data4life.datadonation.donation.program.model.ProgramResource
import care.data4life.datadonation.donation.program.model.ProgramResourceBlur
import care.data4life.datadonation.donation.program.model.ProgramResourceBlurItem
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
        val programAnonymization = ProgramAnonymizationBlur(
            location = "does not matter",
            authored = BlurFunction.START_OF_DAY,
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
            actual = result!!.location,
            expected = programAnonymization.location
        )

        assertEquals(
            actual = result.authored,
            expected = programAnonymization.authored
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programAnonymization.researchSubject
        )
        assertNull(result.resourceBlurItems)
    }

    @Test
    fun `Given resolveBlurRule is called with a QuestionnaireResponse, null as ProgramAnonymizationBlur and a List of ProgramResource it returns null if no ProgramResource match the FHIRResource`() {
        // Given
        val questionnaireResponse = questionnaireResponseTemplate.copy()
        val programResources = listOf(
            ProgramResource(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                blur = ProgramResourceBlur(
                    location = "does not matter",
                    authored = BlurFunction.START_OF_DAY,
                    items = listOf(
                        ProgramResourceBlurItem(
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
            ProgramResource(
                url = "this is the one",
                versions = listOf("1.0.0"),
                blur = ProgramResourceBlur(
                    authored = BlurFunction.START_OF_MONTH,
                    items = listOf(
                        ProgramResourceBlurItem(
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
            ProgramResource(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                blur = ProgramResourceBlur(
                    location = "does not matter",
                    authored = BlurFunction.START_OF_DAY,
                    items = listOf(
                        ProgramResourceBlurItem(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
            ProgramResource(
                url = "this is the one",
                versions = listOf("1.0.0"),
                blur = ProgramResourceBlur(
                    location = "here",
                    authored = BlurFunction.START_OF_MONTH,
                    items = listOf(
                        ProgramResourceBlurItem(
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
            actual = result!!.location,
            expected = programResources[1].blur!!.location
        )

        assertEquals(
            actual = result.authored,
            expected = programResources[1].blur!!.authored
        )

        assertSame(
            actual = result.resourceBlurItems,
            expected = programResources[1].blur!!.items
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
        val programAnonymization = ProgramAnonymizationBlur(
            location = "does not matter",
            authored = BlurFunction.START_OF_DAY,
            researchSubject = BlurFunction.END_OF_DAY
        )
        val programResources = listOf(
            ProgramResource(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0"),
                blur = ProgramResourceBlur(
                    location = "does not matter",
                    authored = BlurFunction.START_OF_DAY,
                    items = listOf(
                        ProgramResourceBlurItem(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
            ProgramResource(
                url = "this is the one",
                versions = listOf("1.0.0"),
                blur = ProgramResourceBlur(
                    location = "here",
                    authored = BlurFunction.START_OF_MONTH,
                    items = listOf(
                        ProgramResourceBlurItem(
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
            actual = result!!.location,
            expected = programResources[1].blur!!.location
        )

        assertEquals(
            actual = result.authored,
            expected = programResources[1].blur!!.authored
        )

        assertSame(
            actual = result.resourceBlurItems,
            expected = programResources[1].blur!!.items
        )

        assertEquals(
            actual = result.researchSubject,
            expected = programAnonymization.researchSubject
        )
    }
}
