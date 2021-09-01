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
import care.data4life.datadonation.mock.stub.donation.program.ProgramRepositoryStub
import care.data4life.datadonation.mock.stub.session.UserSessionTokenRepositoryStub
import care.data4life.datadonation.networking.AccessToken
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ProgramControllerTest {
    @Test
    fun `It fulfils ProgramController`() {
        val controller: Any = ProgramController(
            ProgramRepositoryStub(),
            UserSessionTokenRepositoryStub()
        )

        assertTrue(controller is ProgramContract.Controller)
    }

    @Test
    fun `Given fetchProgram is called with a ProgramName, it retrieves a Program from its repository`() = runBlockingTest {
        // Given
        val accessToken = "potato"
        val programName = "tomato"
        val program = ProgramFixture.sampleProgram

        val session = UserSessionTokenRepositoryStub()

        session.whenSessionToken = { accessToken }

        val repository = ProgramRepositoryStub()

        var capturedToken: AccessToken? = null
        var capturedProgramName: String? = null
        repository.whenFetchProgram = { delegatedToken, delegatedProgramName ->
            capturedToken = delegatedToken
            capturedProgramName = delegatedProgramName

            program
        }

        // When
        val result = ProgramController(repository, session).fetchProgram(programName)

        // Then
        assertSame(
            actual = result,
            expected = program
        )

        assertEquals(
            actual = capturedToken,
            expected = accessToken
        )
        assertEquals(
            actual = capturedProgramName,
            expected = programName
        )
    }
}
