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

package care.data4life.datadonation.donation.donorkeystorage

import care.data4life.datadonation.Annotations
import care.data4life.datadonation.DonationDataContract
import care.data4life.datadonation.RecordId
import care.data4life.datadonation.donation.donorkeystorage.DonorKeyStorageRepositoryContract.Companion.DATA_DONATION_ANNOTATION
import care.data4life.datadonation.donation.donorkeystorage.model.Donor
import care.data4life.datadonation.donation.donorkeystorage.model.NewDonor
import care.data4life.datadonation.mock.ResourceLoader
import care.data4life.datadonation.mock.fixture.DonorIdentityFixture
import care.data4life.datadonation.mock.stub.donation.donorkeystorage.DonorKeyStorageProviderStub
import care.data4life.sdk.util.coroutine.CoroutineScopeFactory
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import care.data4life.sdk.util.test.coroutine.runWithContextBlockingTest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DonorKeyStorageRepositoryTest {
    private val testScope = CoroutineScopeFactory.createScope("testDonorKeyScope")

    @Test
    fun `It fulfils DonorKeyStorageRepository`() {
        val repo: Any = DonorKeyStorageRepository(
            DonorKeyStorageProviderStub(),
            Json,
            testScope
        )

        assertTrue(repo is DonorKeyStorageRepositoryContract)
    }

    @Test
    fun `Given load is called with a ProgramName, it delegates the call to its Provider and propagtes its Error`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"
        val error = RuntimeException()
        val provider = DonorKeyStorageProviderStub()

        val capturedAnnotations = Channel<Annotations>()
        provider.whenLoad = { delegatedAnnotations, _, _, onError ->
            launch {
                capturedAnnotations.send(delegatedAnnotations)
            }
            onError(error)
        }

        // When
        runBlockingTest {
            val failure = assertFailsWith<DonorKeyStorageError.KeyLoadingError> {
                DonorKeyStorageRepository(
                    provider,
                    Json,
                    testScope
                ).load(programName)
            }

            // Then
            assertSame(
                actual = failure.cause,
                expected = error
            )
            assertEquals(
                actual = capturedAnnotations.receive(),
                expected = listOf(
                    "program:$programName",
                    DATA_DONATION_ANNOTATION
                )
            )
        }
    }

    @Test
    fun `Given load is called with a ProgramName, it delegates the call to its Provider and propagtes its Result, if nothing was found`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"

        val provider = DonorKeyStorageProviderStub()

        val capturedAnnotations = Channel<Annotations>()
        provider.whenLoad = { delegatedAnnotations, _, onNotFound, _ ->
            launch {
                capturedAnnotations.send(delegatedAnnotations)
            }
            onNotFound()
        }

        // When
        runBlockingTest {
            val result = DonorKeyStorageRepository(
                provider,
                Json,
                testScope
            ).load(programName)

            // Then
            assertNull(result)
            assertEquals(
                actual = capturedAnnotations.receive(),
                expected = listOf(
                    "program:$programName",
                    DATA_DONATION_ANNOTATION
                )
            )
        }
    }

    @Test
    fun `Given load is called with a ProgramName, it delegates the call to its Provider and propagtes its Result`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"

        val recordId = "ABC"
        val data = ResourceLoader.loader.load("/fixture/donation/ExampleDonorIdentity.json")

        val provider = DonorKeyStorageProviderStub()

        val capturedAnnotations = Channel<Annotations>()
        provider.whenLoad = { delegatedAnnotations, onSuccess, _, _ ->
            launch {
                capturedAnnotations.send(delegatedAnnotations)
            }
            onSuccess(recordId, data)
        }

        // When
        runBlockingTest {
            val result = DonorKeyStorageRepository(
                provider,
                Json,
                testScope
            ).load(programName)

            // Then
            assertEquals(
                actual = result,
                expected = Donor(
                    recordId = recordId,
                    donorIdentity = DonorIdentityFixture.sampleIdentity,
                    programName = programName
                )
            )
            assertEquals(
                actual = capturedAnnotations.receive(),
                expected = listOf(
                    "program:$programName",
                    DATA_DONATION_ANNOTATION
                )
            )
        }
    }

    @Test
    fun `Given save is called with a Donor, it delegates the call to its Provider and propagtes its Error`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"
        val recordId = "ABC"
        val data = DonorIdentityFixture.sampleIdentity
        val donor = NewDonor(
            recordId = recordId,
            donorIdentity = data,
            programName = programName
        )

        val error = RuntimeException()
        val provider = DonorKeyStorageProviderStub()

        val capturedDonorRecord = Channel<DonationDataContract.DonorRecord>()
        provider.whenSave = { delegatedDonorRecord, _, onError ->
            launch {
                capturedDonorRecord.send(delegatedDonorRecord)
            }
            onError(error)
        }

        // When
        runBlockingTest {
            val failure = assertFailsWith<DonorKeyStorageError.KeySavingError> {
                DonorKeyStorageRepository(
                    provider,
                    Json,
                    testScope
                ).save(donor)
            }

            assertSame(
                actual = failure.cause,
                expected = error
            )

            val donorRecord = capturedDonorRecord.receive()
            assertEquals(
                actual = donorRecord.recordId,
                expected = donor.recordId
            )
            assertEquals(
                actual = donorRecord.data,
                expected = "{\"t\":\"${data.keyType}\",\"priv\":\"${data.privateKey}\",\"pub\":\"${data.publicKey}\",\"v\":${data.version},\"scope\":\"${data.scope}\"}"
            )
            assertEquals(
                actual = donorRecord.annotations,
                expected = listOf(
                    "program:$programName",
                    DATA_DONATION_ANNOTATION
                )
            )
        }
    }

    @Test
    fun `Given save is called with a Donor, it delegates the call to its Provider and propagtes its Result`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"
        val recordId = "ABC"
        val data = DonorIdentityFixture.sampleIdentity
        val donor = NewDonor(
            recordId = recordId,
            donorIdentity = data,
            programName = programName
        )

        val provider = DonorKeyStorageProviderStub()

        val capturedDonorRecord = Channel<DonationDataContract.DonorRecord>()
        provider.whenSave = { delegatedDonorRecord, onSuccess, _ ->
            launch {
                capturedDonorRecord.send(delegatedDonorRecord)
            }
            onSuccess()
        }

        // When
        runBlockingTest {
            val result = DonorKeyStorageRepository(
                provider,
                Json,
                testScope
            ).save(donor)

            // Then
            assertSame(
                actual = result,
                expected = Unit
            )

            val donorRecord = capturedDonorRecord.receive()
            assertEquals(
                actual = donorRecord.recordId,
                expected = donor.recordId
            )
            assertEquals(
                actual = donorRecord.data,
                expected = "{\"t\":\"${data.keyType}\",\"priv\":\"${data.privateKey}\",\"pub\":\"${data.publicKey}\",\"v\":${data.version},\"scope\":\"${data.scope}\"}"
            )
            assertEquals(
                actual = donorRecord.annotations,
                expected = listOf(
                    "program:$programName",
                    DATA_DONATION_ANNOTATION
                )
            )
        }
    }

    @Test
    fun `Given delete is called with a Donor, it delegates the call to its Provider and propagtes its Error`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"
        val recordId = "ABC"
        val donor = Donor(
            recordId = recordId,
            donorIdentity = DonorIdentityFixture.sampleIdentity,
            programName = programName
        )

        val error = RuntimeException()
        val provider = DonorKeyStorageProviderStub()

        val capturedRecordId = Channel<RecordId>()
        provider.whenDelete = { delegatedRecordId, _, onError ->
            launch {
                capturedRecordId.send(delegatedRecordId)
            }
            onError(error)
        }

        // When
        runBlockingTest {
            val failure = assertFailsWith<DonorKeyStorageError.KeyDeletionError> {
                DonorKeyStorageRepository(
                    provider,
                    Json,
                    testScope
                ).delete(donor)
            }

            assertSame(
                actual = failure.cause,
                expected = error
            )

            assertEquals(
                actual = capturedRecordId.receive(),
                expected = donor.recordId
            )
        }
    }

    @Test
    fun `Given delete is called with a Donor, it delegates the call to its Provider and propagtes its Result`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val programName = "potato"
        val recordId = "ABC"
        val donor = Donor(
            recordId = recordId,
            donorIdentity = DonorIdentityFixture.sampleIdentity,
            programName = programName
        )

        val provider = DonorKeyStorageProviderStub()

        val capturedRecordId = Channel<RecordId>()
        provider.whenDelete = { delegatedRecordId, onSuccess, _ ->
            launch {
                capturedRecordId.send(delegatedRecordId)
            }
            onSuccess()
        }

        // When
        runBlockingTest {
            val result = DonorKeyStorageRepository(
                provider,
                Json,
                testScope
            ).delete(donor)

            // Then
            assertSame(
                actual = result,
                expected = Unit
            )

            assertEquals(
                actual = capturedRecordId.receive(),
                expected = donor.recordId
            )
        }
    }
}
