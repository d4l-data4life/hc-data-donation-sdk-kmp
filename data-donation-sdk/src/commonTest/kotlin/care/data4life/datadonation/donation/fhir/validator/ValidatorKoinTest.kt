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

package care.data4life.datadonation.donation.fhir.validator

import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ValidatorKoinTest {
    @BeforeTest
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `Given resolveValidatorKoinModule is called it creates a Module, which contains a QuestionnaireResponseValidator`() {
        // When
        val koin = koinApplication {
            modules(resolveResourceValidatorKoinModule())
        }

        // Then
        val repo: FhirResourceValidatorContract.QuestionnaireResponseValidator = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveValidatorKoinModule is called it creates a Module, which contains a ObservationValidator`() {
        // When
        val koin = koinApplication {
            modules(resolveResourceValidatorKoinModule())
        }

        // Then
        val repo: FhirResourceValidatorContract.ObservationValidator = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveValidatorKoinModule is called it creates a Module, which contains a ResearchSubjectValidator`() {
        // When
        val koin = koinApplication {
            modules(resolveResourceValidatorKoinModule())
        }

        // Then
        val repo: FhirResourceValidatorContract.ResearchSubjectValidator = koin.koin.get()
        assertNotNull(repo)
    }

    @Test
    fun `Given resolveValidatorKoinModule is called it creates a Module, which contains a ResourceValidator`() {
        // When
        val koin = koinApplication {
            modules(resolveResourceValidatorKoinModule())
        }

        // Then
        val repo: FhirResourceValidatorContract.ResourceValidator = koin.koin.get()
        assertNotNull(repo)
    }
}
