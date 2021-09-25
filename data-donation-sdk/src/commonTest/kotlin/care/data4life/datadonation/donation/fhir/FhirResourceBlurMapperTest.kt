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

package care.data4life.datadonation.donation.fhir

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.FhirResourceConfiguration
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FhirResourceBlurMapperTest {
    @Test
    fun `It fulfils AllowListMapper`() {
        val mapper: Any = FhirResourceBlurMapper

        assertTrue(mapper is FhirContract.FhirResourceBlurMapper)
    }

    @Test
    fun `Given map is called with a empty List, it maps to a empty Map`() {
        // Given
        val resources = emptyList<FhirResourceConfiguration>()

        // When
        val result = FhirResourceBlurMapper.map(resources)

        // Then
        assertEquals(
            actual = result,
            expected = result
        )
    }

    @Test
    fun `Given map is called with a List of ProgramFhirResourceConfiguration it maps it to Map of AllowedReference, which has no Version to BlurFunction`() {
        // Given
        val programResources = listOf(
            FhirResourceConfiguration(
                url = "somewhere over the rainbow",
                fhirBlur = QuestionnaireResponseBlur(
                    targetTimeZone = "does not matter",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    questionnaireResponseItemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
        )

        // When
        val result = FhirResourceBlurMapper.map(programResources)

        // Then
        assertEquals(
            actual = result,
            expected = mapOf(
                programResources.first().url to programResources.first().fhirBlur
            )
        )
    }

    @Test
    fun `Given map is called with a List of ProgramFhirResourceConfiguration it maps it to Map of AllowedReference, which contains Versions to BlurFunction`() {
        // Given
        val programResources = listOf(
            FhirResourceConfiguration(
                url = "somewhere over the rainbow",
                versions = listOf("0.0.0", "0.1.0", "1.42.23"),
                fhirBlur = QuestionnaireResponseBlur(
                    targetTimeZone = "does not matter",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    questionnaireResponseItemBlurs = listOf(
                        QuestionnaireResponseItemBlur(
                            linkId = "23",
                            function = BlurFunction.END_OF_DAY
                        )
                    )
                )
            ),
        )

        // When
        val result = FhirResourceBlurMapper.map(programResources)

        // Then
        assertEquals(
            actual = result,
            expected = mapOf(
                "${programResources.first().url}|${programResources.first().versions!!.first()}" to
                    programResources.first().fhirBlur,
                "${programResources.first().url}|${programResources.first().versions!![1]}" to
                    programResources.first().fhirBlur,
                "${programResources.first().url}|${programResources.first().versions!![2]}" to
                    programResources.first().fhirBlur,
            )
        )
    }
}
