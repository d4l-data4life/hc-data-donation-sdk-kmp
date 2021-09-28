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

internal class Fhir3CodingListIteratorWrapper(
    private val iterator: ListIterator<Fhir3Coding>
) : CompatibilityWrapperContract.CodingListIterator<Fhir3Coding> {
    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun hasPrevious(): Boolean {
        return iterator.hasPrevious()
    }

    override fun next(): CompatibilityWrapperContract.Coding {
        return Fhir3CodingWrapper(iterator.next())
    }

    override fun nextIndex(): Int {
        return iterator.nextIndex()
    }

    override fun previous(): CompatibilityWrapperContract.Coding {
        return Fhir3CodingWrapper(iterator.previous())
    }

    override fun previousIndex(): Int {
        return iterator.previousIndex()
    }

    override fun unwrap(): ListIterator<Fhir3Coding> {
        return iterator
    }
}

internal class Fhir4CodingListIteratorWrapper(
    private val iterator: ListIterator<Fhir4Coding>
) : CompatibilityWrapperContract.CodingListIterator<Fhir4Coding> {
    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun hasPrevious(): Boolean {
        return iterator.hasPrevious()
    }

    override fun next(): CompatibilityWrapperContract.Coding {
        return Fhir4CodingWrapper(iterator.next())
    }

    override fun nextIndex(): Int {
        return iterator.nextIndex()
    }

    override fun previous(): CompatibilityWrapperContract.Coding {
        return Fhir4CodingWrapper(iterator.previous())
    }

    override fun previousIndex(): Int {
        return iterator.previousIndex()
    }

    override fun unwrap(): ListIterator<Fhir4Coding> {
        return iterator
    }
}
