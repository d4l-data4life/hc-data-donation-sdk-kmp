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

import care.data4life.datadonation.donation.fhir.wrapper.CompatibilityWrapperContract.QuestionnaireResponseItemAnswer

data class Fhir3QuestionnaireResponseItemAnswerListIteratorWrapper(
    private val iterator: ListIterator<Fhir3QuestionnaireResponseItemAnswer>
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswerListIterator<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun hasPrevious(): Boolean {
        return iterator.hasPrevious()
    }

    override fun next(): QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemAnswerWrapper(
            iterator.next()
        )
    }

    override fun nextIndex(): Int {
        return iterator.nextIndex()
    }

    override fun previous(): QuestionnaireResponseItemAnswer<Fhir3QuestionnaireResponseItem, Fhir3QuestionnaireResponseItemAnswer, Fhir3DateTime> {
        return Fhir3QuestionnaireResponseItemAnswerWrapper(
            iterator.previous()
        )
    }

    override fun previousIndex(): Int {
        return iterator.previousIndex()
    }

    override fun unwrap(): ListIterator<Fhir3QuestionnaireResponseItemAnswer> {
        return iterator
    }
}

data class Fhir4QuestionnaireResponseItemAnswerListIteratorWrapper(
    private val iterator: ListIterator<Fhir4QuestionnaireResponseItemAnswer>
) : CompatibilityWrapperContract.QuestionnaireResponseItemAnswerListIterator<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun hasPrevious(): Boolean {
        return iterator.hasPrevious()
    }

    override fun next(): QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemAnswerWrapper(
            iterator.next()
        )
    }

    override fun nextIndex(): Int {
        return iterator.nextIndex()
    }

    override fun previous(): QuestionnaireResponseItemAnswer<Fhir4QuestionnaireResponseItem, Fhir4QuestionnaireResponseItemAnswer, Fhir4DateTime> {
        return Fhir4QuestionnaireResponseItemAnswerWrapper(
            iterator.previous()
        )
    }

    override fun previousIndex(): Int {
        return iterator.previousIndex()
    }

    override fun unwrap(): ListIterator<Fhir4QuestionnaireResponseItemAnswer> {
        return iterator
    }
}
