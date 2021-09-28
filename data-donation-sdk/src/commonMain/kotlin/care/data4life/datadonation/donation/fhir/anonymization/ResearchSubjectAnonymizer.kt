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

import care.data4life.datadonation.donation.fhir.anonymization.model.BlurModelContract.ResearchSubjectBlurRule
import care.data4life.hl7.fhir.stu3.model.Period
import care.data4life.hl7.fhir.stu3.model.ResearchSubject

internal class ResearchSubjectAnonymizer(
    private val dateTimeConcealer: AnonymizationContract.DateTimeConcealer
) : AnonymizationContract.ResearchSubjectAnonymizer {
    private fun blurPeriod(
        period: Period,
        rule: ResearchSubjectBlurRule
    ): Period {
        return period.copy(
            start = period.start?.copy(
                value = dateTimeConcealer.blur(
                    period.start?.value!!,
                    rule.targetTimeZone,
                    rule.researchSubject
                )
            ),
            end = period.end?.copy(
                value = dateTimeConcealer.blur(
                    period.end?.value!!,
                    rule.targetTimeZone,
                    rule.researchSubject
                )
            )
        )
    }

    private fun isConcealablePeriod(
        researchSubject: ResearchSubject,
        rule: ResearchSubjectBlurRule?
    ): Boolean {
        return rule is ResearchSubjectBlurRule &&
            researchSubject.period is Period
    }

    override fun anonymize(
        researchSubject: ResearchSubject,
        rule: ResearchSubjectBlurRule?
    ): ResearchSubject {
        return if (isConcealablePeriod(researchSubject, rule)) {
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
