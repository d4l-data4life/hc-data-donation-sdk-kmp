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

package care.data4life.datadonation

import care.data4life.sdk.util.coroutine.CoroutineScopeFactory
import care.data4life.sdk.util.test.coroutine.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultPipeTest {
    private val scope = CoroutineScopeFactory.createScope("testPipe")

    @Test
    fun `It fulfils Pipe`() {
        val pipe: Any = ResultPipe<Any, Throwable>(scope)

        assertTrue(pipe is DataDonationSDK.Pipe<*, *>)
    }

    @Test
    fun `Given onSuccess is called and receive it retuns a Success`() {
        val expected = "Something"
        val pipe = ResultPipe<String, Throwable>(scope)

        pipe.onSuccess(expected)

        runBlockingTest {
            val result = pipe.receive()

            assertTrue(result is Result.Success<*, *>)

            assertEquals(
                actual = result.value!!,
                expected = expected
            )
        }
    }

    @Test
    fun `Given onError is called and receive it retuns a Success`() {
        val error = RuntimeException("Something")
        val pipe = ResultPipe<String, Throwable>(scope)

        pipe.onError(error)

        runBlockingTest {
            val result = pipe.receive()

            assertTrue(result is Result.Error<*, *>)

            assertEquals(
                actual = result.error!!,
                expected = error
            )
        }
    }
}
