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

import care.data4life.datadonation.donation.fhir.anonymization.model.ResearchSubjectBlurRule
import care.data4life.datadonation.donation.program.model.BlurFunctionReference
import care.data4life.datadonation.donation.program.model.ProgramBlur

internal object ResearchSubjectBlurRuleResolver : AnonymizationContract.ResearchSubjectBlurRuleResolver {
    private fun fulfilsRequirements(
        programRule: ProgramBlur?
    ): Boolean {
        return programRule?.targetTimeZone is String &&
            programRule.researchSubjectBlurFunctionReference is BlurFunctionReference
    }

    private fun resolveRuleSets(
        programRule: ProgramBlur?
    ): ResearchSubjectBlurRule? {
        return if (fulfilsRequirements(programRule)) {
            ResearchSubjectBlurRule(
                targetTimeZone = programRule!!.targetTimeZone,
                researchSubject = programRule.researchSubjectBlurFunctionReference!!,
            )
        } else {
            null
        }
    }

    override fun resolveBlurRule(
        programRule: ProgramBlur?
    ): ResearchSubjectBlurRule? {
        return resolveRuleSets(programRule)
    }
}
