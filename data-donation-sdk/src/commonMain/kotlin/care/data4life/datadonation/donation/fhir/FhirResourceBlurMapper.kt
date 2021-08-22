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

import care.data4life.datadonation.donation.fhir.FhirContract.FhirResourceBlurMapper.Companion.REFERENCE_SEPARATOR
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceBlur
import care.data4life.datadonation.donation.program.model.ProgramFhirResourceConfiguration

internal object FhirResourceBlurMapper : FhirContract.FhirResourceBlurMapper {
    private fun assembleReferences(
        baseUrl: String,
        versions: List<String>?
    ): List<String> {
        return if (versions is List<*>) {
            versions.map { version -> "$baseUrl${REFERENCE_SEPARATOR}$version" }
        } else {
            listOf(baseUrl)
        }
    }

    private fun addEntriesForFhirResourceConfiguration(
        referenceMap: MutableMap<AllowedReference, ProgramFhirResourceBlur?>,
        programFhirResourceConfigurations: ProgramFhirResourceConfiguration
    ) {
        assembleReferences(
            programFhirResourceConfigurations.url,
            programFhirResourceConfigurations.versions
        ).forEach { reference ->
            referenceMap[reference] = programFhirResourceConfigurations.fhirBlur
        }
    }

    override fun map(
        fhirResourceConfigurations: List<ProgramFhirResourceConfiguration>
    ): Map<AllowedReference, ProgramFhirResourceBlur?> {
        val referenceMap = mutableMapOf<AllowedReference, ProgramFhirResourceBlur?>()

        fhirResourceConfigurations.forEach { fhirResourcesConfiguration ->
            addEntriesForFhirResourceConfiguration(
                referenceMap,
                fhirResourcesConfiguration
            )
        }

        return referenceMap
    }
}
