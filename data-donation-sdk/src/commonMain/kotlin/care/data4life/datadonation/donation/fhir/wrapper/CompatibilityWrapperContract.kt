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

import care.data4life.hl7.fhir.FhirVersion
import care.data4life.hl7.fhir.common.datetime.XsDateTime

internal typealias Fhir3DateTime = care.data4life.hl7.fhir.stu3.primitive.DateTime
internal typealias Fhir3QuestionnaireResponse = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
internal typealias Fhir3QuestionnaireResponseItem = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
internal typealias Fhir3QuestionnaireResponseItemAnswer = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
internal typealias Fhir3Period = care.data4life.hl7.fhir.stu3.model.Period
internal typealias Fhir3ResearchSubject = care.data4life.hl7.fhir.stu3.model.ResearchSubject

internal typealias Fhir4DateTime = care.data4life.hl7.fhir.r4.primitive.DateTime
internal typealias Fhir4QuestionnaireResponse = care.data4life.hl7.fhir.r4.model.QuestionnaireResponse
internal typealias Fhir4QuestionnaireResponseItem = care.data4life.hl7.fhir.r4.model.QuestionnaireResponseItem
internal typealias Fhir4QuestionnaireResponseItemAnswer = care.data4life.hl7.fhir.r4.model.QuestionnaireResponseItemAnswer
internal typealias Fhir4Period = care.data4life.hl7.fhir.r4.model.Period
internal typealias Fhir4ResearchSubject = care.data4life.hl7.fhir.r4.model.ResearchSubject

interface CompatibilityWrapperContract {
    interface Stripper<WrappedValue : FhirVersion> {
        fun unwrap(): WrappedValue
    }

    interface FhirWrapper

    interface DateTime<DateValue : FhirVersion> : FhirWrapper, Stripper<DateValue> {
        val value: XsDateTime

        fun copy(
            value: XsDateTime
        ): DateTime<DateValue>
    }

    interface QuestionnaireResponseItemAnswer<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        FhirWrapper, Stripper<AnswerValue> {
        val valueString: String?
        val valueDateTime: DateTime<DateValue>?
        val item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?

        fun copy(
            valueString: String?,
            valueDateTime: DateTime<DateValue>?,
            item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?
        ): QuestionnaireResponseItemAnswer<ItemValue, AnswerValue, DateValue>
    }

    interface QuestionnaireResponseItemAnswerListIterator<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        ListIterator<QuestionnaireResponseItemAnswer<ItemValue, AnswerValue, DateValue>> {

        fun unwrap(): ListIterator<AnswerValue>
    }

    interface QuestionnaireResponseItemAnswerList<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        List<QuestionnaireResponseItemAnswer<ItemValue, AnswerValue, DateValue>> {

        fun unwrap(): List<AnswerValue>
    }

    interface QuestionnaireResponseItem<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        FhirWrapper, Stripper<ItemValue> {
        val linkId: String
        val item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?
        val answer: QuestionnaireResponseItemAnswerList<ItemValue, AnswerValue, DateValue>?

        fun copy(
            item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?,
            answer: QuestionnaireResponseItemAnswerList<ItemValue, AnswerValue, DateValue>?
        ): QuestionnaireResponseItem<ItemValue, AnswerValue, DateValue>
    }

    interface QuestionnaireResponseItemListIterator<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        ListIterator<QuestionnaireResponseItem<ItemValue, AnswerValue, DateValue>> {

        fun unwrap(): ListIterator<ItemValue>
    }

    interface QuestionnaireResponseItemList<ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        List<QuestionnaireResponseItem<ItemValue, AnswerValue, DateValue>> {

        fun unwrap(): List<ItemValue>
    }

    interface QuestionnaireResponse<ResponseValue : FhirVersion, ItemValue : FhirVersion, AnswerValue : FhirVersion, DateValue : FhirVersion> :
        FhirWrapper, Stripper<ResponseValue> {
        val questionnaireReference: String?
        val item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?
        val authored: DateTime<DateValue>?

        fun copy(
            item: QuestionnaireResponseItemList<ItemValue, AnswerValue, DateValue>?,
            authored: DateTime<DateValue>?
        ): QuestionnaireResponse<ResponseValue, ItemValue, AnswerValue, DateValue>
    }

    interface Period<PeriodValue : FhirVersion, DateValue : FhirVersion> :
        Stripper<PeriodValue> {
        val start: DateTime<DateValue>?
        val end: DateTime<DateValue>?

        fun copy(
            start: DateTime<DateValue>?,
            end: DateTime<DateValue>?
        ): Period<PeriodValue, DateValue>
    }

    interface ResearchSubject<ResearchSubjectValue : FhirVersion, PeriodValue : FhirVersion, DateValue : FhirVersion> :
        Stripper<ResearchSubjectValue> {
        val studyIdentifierSystem: String?
        val studyIdentifierValue: String?
        val individualIdentifierSystem: String?

        val period: Period<PeriodValue, DateValue>?

        fun copy(
            period: Period<PeriodValue, DateValue>?
        ): ResearchSubject<ResearchSubjectValue, PeriodValue, DateValue>
    }
}
