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

import care.data4life.datadonation.donation.program.model.BlurFunctionReference
import care.data4life.datadonation.donation.program.model.ProgramBlur
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResearchSubjectBlurRuleRuleResolverTest {
    @Test
    fun `It fulfils ResearchSubjectBlurRuleResolver`() {
        val resolver: Any = ResearchSubjectBlurRuleResolver

        assertTrue(resolver is AnonymizationContract.ResearchSubjectBlurRuleResolver)
    }

    @Test
    fun `Given resolveBlurRule is called with null as ProgramBlur`() {
        // Given
        val programBlur = null

        // When
        val result = ResearchSubjectBlurRuleResolver.resolveBlurRule(
            programBlur,
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a ProgramBlur, contains no Reference for a ResearchSubject, it returns null`() {
        // Given
        val programBlur = ProgramBlur(
            targetTimeZone = "does not matter",
            researchSubjectBlurFunctionReference = null
        )

        // When
        val result = ResearchSubjectBlurRuleResolver.resolveBlurRule(
            programBlur,
        )

        // Then
        assertNull(result)
    }

    @Test
    fun `Given resolveBlurRule is called with a ProgramBlur, contains a Reference for a ResearchSubject, it returns ResearchSubjectBlurRule`() {
        // Given
        val programBlur = ProgramBlur(
            targetTimeZone = "does not matter",
            researchSubjectBlurFunctionReference = BlurFunctionReference.END_OF_DAY
        )

        // When
        val result = ResearchSubjectBlurRuleResolver.resolveBlurRule(
            programBlur,
        )

        // Then
        assertNotNull(result)

        assertEquals(
            actual = result.researchSubject,
            expected = programBlur.researchSubjectBlurFunctionReference
        )

        assertEquals(
            actual = result.targetTimeZone,
            expected = programBlur.targetTimeZone
        )
    }
}
