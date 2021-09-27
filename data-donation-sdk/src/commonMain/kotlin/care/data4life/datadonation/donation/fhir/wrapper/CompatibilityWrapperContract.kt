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

internal typealias Fhir3QuestionnaireResponse = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
internal typealias Fhir3QuestionnaireResponseItem = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItem
internal typealias Fhir3QuestionnaireResponseItemAnswer = care.data4life.hl7.fhir.stu3.model.QuestionnaireResponseItemAnswer
internal typealias Fhir3DateTime = care.data4life.hl7.fhir.stu3.primitive.DateTime

internal typealias Fhir4QuestionnaireResponse = care.data4life.hl7.fhir.r4.model.QuestionnaireResponse
internal typealias Fhir4QuestionnaireResponseItem = care.data4life.hl7.fhir.r4.model.QuestionnaireResponseItem
internal typealias Fhir4QuestionnaireResponseItemAnswer = care.data4life.hl7.fhir.r4.model.QuestionnaireResponseItemAnswer
internal typealias Fhir4DateTime = care.data4life.hl7.fhir.r4.primitive.DateTime

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
}
