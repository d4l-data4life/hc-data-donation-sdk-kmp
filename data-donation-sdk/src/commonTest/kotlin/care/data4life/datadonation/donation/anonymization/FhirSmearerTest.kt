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
import care.data4life.datadonation.donation.program.model.ProgramAnonymization
import care.data4life.datadonation.donation.program.model.ProgramAnonymizationBlur
import care.data4life.datadonation.donation.program.model.ProgramDonationConfiguration
import care.data4life.datadonation.donation.program.model.ProgramResource
import care.data4life.datadonation.mock.stub.donation.anonymization.BlurRuleResolverStub
import care.data4life.datadonation.mock.stub.donation.anonymization.DateTimeSmearerStub
import care.data4life.hl7.fhir.common.datetime.XsDate
import care.data4life.hl7.fhir.common.datetime.XsDateTime
import care.data4life.hl7.fhir.stu3.codesystem.QuestionnaireResponseStatus
import care.data4life.hl7.fhir.stu3.model.DomainResource
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import care.data4life.hl7.fhir.stu3.primitive.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class FhirSmearerTest {
    private val programConfig = ProgramDonationConfiguration(
        consentKey = "xxx",
        resources = listOf(),
        delay = 23.0,
        studyID = "id"
    )

    @Test
    fun `It fulfils FhirDateTimeSmearer`() {
        val smearer: Any = FhirSmearer(
            BlurRuleResolverStub(),
            DateTimeSmearerStub()
        )

        assertTrue(smearer is AnonymizationContract.FhirSmearer)
    }

    @Test
    fun `Given blurFhirResource is called with a arbitrary FhirResource and a ProgramDonationConfiguration it reflects the Resource`() {
        // Given
        val resource = DomainResource()

        // When
        val result = FhirSmearer(
            BlurRuleResolverStub(),
            DateTimeSmearerStub()
        ).blurFhirResource(resource, programConfig)

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given blurFhirResource is called with a QuestionaireResponse and a ProgramDonationConfiguration it reflects the QuestionaireResponse, if no Blur was resolved`() {
        // Given
        val resource = DomainResource()

        val blurResolver = BlurRuleResolverStub()

        blurResolver.whenResolveBlurRule = { _, _, _ -> null }

        // When
        val result = FhirSmearer(
            blurResolver,
            DateTimeSmearerStub()
        ).blurFhirResource(resource, programConfig)

        // Then
        assertSame(
            actual = result,
            expected = resource
        )
    }

    @Test
    fun `Given blurFhirResource is called with a QuestionaireResponse and a ProgramDonationConfiguration it ignores the authored field, if no BlurFunction was provided for the field`() {
        // Given
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val blurResolver = BlurRuleResolverStub()

        val programConfig = programConfig.copy(
            resources = listOf(ProgramResource(url = "123")),
            anonymization = ProgramAnonymization(
                blur = ProgramAnonymizationBlur(location = "abc")
            )
        )

        val rule = BlurRule(
            location = "somewhere"
        )

        var capturedFhirResource: FhirResource? = null
        var capturedProgramAnonymizationBlur: ProgramAnonymizationBlur? = null
        var capturedProgramResources: List<ProgramResource>? = null

        blurResolver.whenResolveBlurRule = { delegatedFhirResource, delegatedProgramAnonymizationBlur, delegatedProgramResources ->
            capturedFhirResource = delegatedFhirResource
            capturedProgramAnonymizationBlur = delegatedProgramAnonymizationBlur
            capturedProgramResources = delegatedProgramResources

            rule
        }

        // When
        val result = FhirSmearer(
            blurResolver,
            DateTimeSmearerStub()
        ).blurFhirResource(resource, programConfig)

        // Then
        assertSame(
            actual = result,
            expected = resource
        )

        assertSame(
            actual = capturedFhirResource,
            expected = resource
        )
        assertSame(
            actual = capturedProgramAnonymizationBlur,
            expected = programConfig.anonymization?.blur
        )
        assertSame(
            actual = capturedProgramResources,
            expected = programConfig.resources
        )
    }

    @Test
    fun `Given blurFhirResource is called with a QuestionaireResponse and a ProgramDonationConfiguration it ignores the authored field, if it does not exists`() {
        // Given
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val blurResolver = BlurRuleResolverStub()

        val programConfig = programConfig.copy(
            resources = listOf(ProgramResource(url = "123")),
            anonymization = ProgramAnonymization(
                blur = ProgramAnonymizationBlur(
                    location = "abc",
                    authored = BlurFunction.START_OF_WEEK
                )
            )
        )

        val rule = BlurRule(
            location = "somewhere"
        )

        var capturedFhirResource: FhirResource? = null
        var capturedProgramAnonymizationBlur: ProgramAnonymizationBlur? = null
        var capturedProgramResources: List<ProgramResource>? = null

        blurResolver.whenResolveBlurRule = { delegatedFhirResource, delegatedProgramAnonymizationBlur, delegatedProgramResources ->
            capturedFhirResource = delegatedFhirResource
            capturedProgramAnonymizationBlur = delegatedProgramAnonymizationBlur
            capturedProgramResources = delegatedProgramResources

            rule
        }

        // When
        val result = FhirSmearer(
            blurResolver,
            DateTimeSmearerStub()
        ).blurFhirResource(resource, programConfig)

        // Then
        assertSame(
            actual = result,
            expected = resource
        )

        assertSame(
            actual = capturedFhirResource,
            expected = resource
        )
        assertSame(
            actual = capturedProgramAnonymizationBlur,
            expected = programConfig.anonymization?.blur
        )
        assertSame(
            actual = capturedProgramResources,
            expected = programConfig.resources
        )
    }

    @Test
    fun `Given blurFhirResource is called with a QuestionaireResponse and a ProgramDonationConfiguration it blurs the authored field`() {
        // Given
        val bluredAuthoredDate = XsDateTime(
            date = XsDate(2022, 1, 23)
        )
        val resource = QuestionnaireResponse(
            status = QuestionnaireResponseStatus.AMENDED,
            authored = DateTime(
                value = XsDateTime(
                    date = XsDate(2022, 1, 1)
                )
            )
        )

        val blurResolver = BlurRuleResolverStub()
        val dateTimeSmearer = DateTimeSmearerStub()

        val programConfig = programConfig.copy(
            resources = listOf(ProgramResource(url = "123")),
            anonymization = ProgramAnonymization(
                blur = ProgramAnonymizationBlur(
                    location = "abc",
                    authored = BlurFunction.START_OF_WEEK
                )
            )
        )

        val rule = BlurRule(
            location = "somewhere",
            authored = BlurFunction.END_OF_DAY
        )

        var capturedFhirResource: FhirResource? = null
        var capturedProgramAnonymizationBlur: ProgramAnonymizationBlur? = null
        var capturedProgramResources: List<ProgramResource>? = null

        blurResolver.whenResolveBlurRule = { delegatedFhirResource, delegatedProgramAnonymizationBlur, delegatedProgramResources ->
            capturedFhirResource = delegatedFhirResource
            capturedProgramAnonymizationBlur = delegatedProgramAnonymizationBlur
            capturedProgramResources = delegatedProgramResources

            rule
        }

        var capturedDateTime: XsDateTime? = null
        var capturedLocation: String? = null
        var capturedBlurFunction: BlurFunction? = null

        dateTimeSmearer.whenBlur = { delegatedDateTime, delegatedLocation, delegatedBlurFunction ->
            capturedDateTime = delegatedDateTime
            capturedLocation = delegatedLocation
            capturedBlurFunction = delegatedBlurFunction

            bluredAuthoredDate
        }

        // When
        val result = FhirSmearer(
            blurResolver,
            dateTimeSmearer
        ).blurFhirResource(resource, programConfig)

        // Then
        assertEquals(
            actual = result,
            expected = resource.copy(
                authored = resource.authored!!.copy(
                    value = bluredAuthoredDate
                )
            )
        )

        assertSame(
            actual = capturedFhirResource,
            expected = resource
        )
        assertSame(
            actual = capturedProgramAnonymizationBlur,
            expected = programConfig.anonymization?.blur
        )
        assertSame(
            actual = capturedProgramResources,
            expected = programConfig.resources
        )

        assertSame(
            actual = capturedDateTime,
            expected = resource.authored!!.value
        )
        assertEquals(
            actual = capturedLocation,
            expected = rule.location
        )
        assertSame(
            actual = capturedBlurFunction,
            expected = rule.authored
        )
    }
}
