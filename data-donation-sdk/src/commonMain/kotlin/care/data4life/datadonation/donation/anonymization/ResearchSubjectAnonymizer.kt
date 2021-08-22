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
import care.data4life.hl7.fhir.stu3.model.Period
import care.data4life.hl7.fhir.stu3.model.ResearchSubject

internal class ResearchSubjectAnonymizer(
    private val dateTimeSmearer: AnonymizationContract.DateTimeSmearer
) : AnonymizationContract.ResearchSubjectAnonymizer {
    private fun blurPeriod(
        period: Period,
        rule: BlurRule
    ): Period {
        return period.copy(
            start = period.start?.copy(
                value = dateTimeSmearer.blur(
                    period.start?.value!!,
                    rule.targetTimeZone,
                    rule.researchSubject!!
                )
            ),
            end = period.end?.copy(
                value = dateTimeSmearer.blur(
                    period.end?.value!!,
                    rule.targetTimeZone,
                    rule.researchSubject!!
                )
            )
        )
    }

    private fun periodIsBlurable(
        researchSubject: ResearchSubject,
        rule: BlurRule?
    ): Boolean {
        return rule?.researchSubject is BlurFunction &&
            researchSubject.period is Period
    }

    override fun anonymize(
        researchSubject: ResearchSubject,
        rule: BlurRule?
    ): ResearchSubject {
        return if (periodIsBlurable(researchSubject, rule)) {
            researchSubject.copy(
                period = blurPeriod(
                    researchSubject.period!!,
                    rule!!
                )
            )
        } else {
            researchSubject
        }
    }
}
