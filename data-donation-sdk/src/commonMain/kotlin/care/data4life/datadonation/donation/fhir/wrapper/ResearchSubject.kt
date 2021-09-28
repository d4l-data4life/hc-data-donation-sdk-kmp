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

package care.data4life.datadonation.donation.fhir.wrapper

internal class Fhir3ResearchSubjectWrapper(
    private val researchSubject: Fhir3ResearchSubject
) : CompatibilityWrapperContract.ResearchSubject<Fhir3ResearchSubject, Fhir3Period, Fhir3DateTime> {
    override val studyIdentifierSystem: String?
        get() = researchSubject.study.identifier?.system
    override val studyIdentifierValue: String?
        get() = researchSubject.study.identifier?.value
    override val individualIdentifierSystem: String?
        get() = researchSubject.individual.identifier?.system
    override val period: CompatibilityWrapperContract.Period<Fhir3Period, Fhir3DateTime>?
        get() {
            return if (researchSubject.period is Fhir3Period) {
                Fhir3PeriodWrapper(researchSubject.period!!)
            } else {
                null
            }
        }

    override fun copy(
        period: CompatibilityWrapperContract.Period<Fhir3Period, Fhir3DateTime>?
    ): CompatibilityWrapperContract.ResearchSubject<Fhir3ResearchSubject, Fhir3Period, Fhir3DateTime> {
        return Fhir3ResearchSubjectWrapper(
            researchSubject.copy(
                period = period?.unwrap()
            )
        )
    }

    override fun unwrap(): Fhir3ResearchSubject {
        return researchSubject
    }
}

internal class Fhir4ResearchSubjectWrapper(
    private val researchSubject: Fhir4ResearchSubject
) : CompatibilityWrapperContract.ResearchSubject<Fhir4ResearchSubject, Fhir4Period, Fhir4DateTime> {
    override val studyIdentifierSystem: String?
        get() = researchSubject.study.identifier?.system
    override val studyIdentifierValue: String?
        get() = researchSubject.study.identifier?.value
    override val individualIdentifierSystem: String?
        get() = researchSubject.individual.identifier?.system
    override val period: CompatibilityWrapperContract.Period<Fhir4Period, Fhir4DateTime>?
        get() {
            return if (researchSubject.period is Fhir4Period) {
                Fhir4PeriodWrapper(researchSubject.period!!)
            } else {
                null
            }
        }

    override fun copy(
        period: CompatibilityWrapperContract.Period<Fhir4Period, Fhir4DateTime>?
    ): CompatibilityWrapperContract.ResearchSubject<Fhir4ResearchSubject, Fhir4Period, Fhir4DateTime> {
        return Fhir4ResearchSubjectWrapper(
            researchSubject.copy(
                period = period?.unwrap()
            )
        )
    }

    override fun unwrap(): Fhir4ResearchSubject {
        return researchSubject
    }
}
