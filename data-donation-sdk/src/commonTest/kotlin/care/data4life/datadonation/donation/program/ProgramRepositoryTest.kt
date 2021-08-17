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

package care.data4life.datadonation.donation.program

import care.data4life.datadonation.mock.fixture.ProgramFixture
import care.data4life.datadonation.mock.stub.donation.program.ProgramApiServiceStub
import care.data4life.datadonation.networking.AccessToken
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ProgramRepositoryTest {
    @Test
    fun `It fulfils ProgramRepository`() {
        val repo: Any = ProgramRepository(ProgramApiServiceStub())

        assertTrue(repo is ProgramContract.ProgramRepository)
    }

    @Test
    fun `Given fetchProgram is called with a AccessToken and a StudyName it delegates its call to the ProgramApiService`() = runBlockingTest {
        // Given
        val accessToken = "tomato"
        val studyName = "soup"

        val program = ProgramFixture.sampleProgram

        val capturedAccessToken = Channel<AccessToken>()
        val capturedStudyName = Channel<String>()

        val apiService = ProgramApiServiceStub()

        apiService.whenFetchProgram = { delegatedAccessToken, delegatedStudyName ->
            launch {
                capturedAccessToken.send(delegatedAccessToken)
                capturedStudyName.send(delegatedStudyName)
            }

            program
        }

        // When
        val result = ProgramRepository(apiService).fetchProgram(
            accessToken,
            studyName
        )

        // Then
        assertSame(
            actual = result,
            expected = program
        )
        assertEquals(
            actual = capturedAccessToken.receive(),
            expected = accessToken
        )
        assertEquals(
            actual = capturedStudyName.receive(),
            expected = studyName
        )
    }
}
