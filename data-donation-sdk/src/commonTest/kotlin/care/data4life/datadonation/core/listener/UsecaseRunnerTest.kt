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

package care.data4life.datadonation.core.listener

import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.mock.MockException
import care.data4life.datadonation.mock.stub.CallbackStub
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import care.data4life.datadonation.mock.stub.ResultListenerStub
import care.data4life.sdk.util.test.runBlockingTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class UsecaseRunnerTest {
    @Test
    fun `It fulfils UsecaseRunner`() {
        val runner: Any = UsecaseRunner(ClientConfigurationStub())

        assertTrue(runner is ListenerInternalContract.UsecaseRunner)
    }

    @Test
    fun `Given run is called with a ResultListener and a Usecase, it invokes in async context the Usecase and delegates the result to the Listener`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<String>()
        val usecase = UsecaseStub<String>()

        val runner = UsecaseRunner(config)

        val result = "potato"
        val capturedResult = Channel<String>()

        usecase.exec = { result }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnSuccess = { delegatedResult ->
            launch {
                capturedResult.send(delegatedResult)
            }
        }

        // When
        runner.run(listener, usecase)

        // Then
        assertEquals(
            actual = capturedResult.receive(),
            expected = result
        )
    }

    @Test
    fun `Given run is called with a ResultListener and a Usecase, it invokes in async context the Usecase while failing and delegates the Exception to the Listener`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<String>()
        val usecase = UsecaseStub<String>()

        val runner = UsecaseRunner(config)

        val exception = RuntimeException("tomato")
        val capturedException = Channel<Exception>()

        usecase.exec = { throw exception }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnError = { delegatedException ->
            launch {
                capturedException.send(delegatedException)
            }
        }

        // When
        runner.run(listener, usecase)

        // Then
        assertSame(
            actual = capturedException.receive(),
            expected = exception
        )
    }

    @Test
    fun `Given run is called with a Callback and a Usecase, it invokes in async context the Usecase and calls to the Callback`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = CallbackStub()
        val usecase = UsecaseStub<Unit>()

        val runner = UsecaseRunner(config)

        val wasExecuted = Channel<Boolean>()
        val capturedCall = Channel<Boolean>()

        usecase.exec = {
            launch {
                wasExecuted.send(true)
            }
        }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnSuccess = {
            launch {
                capturedCall.send(true)
            }
        }

        // When
        runner.run(listener, usecase)

        // Then
        assertTrue(wasExecuted.receive())
        assertTrue(capturedCall.receive())
    }

    @Test
    fun `Given run is called with a Callback and a Usecase, it invokes in async context the Usecase while failing and delegates the Exception to the Callback`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = CallbackStub()
        val usecase = UsecaseStub<Unit>()

        val runner = UsecaseRunner(config)

        val exception = RuntimeException("tomato")
        val capturedException = Channel<Exception>()

        usecase.exec = { throw exception }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnError = { delegatedExecption ->
            launch {
                capturedException.send(delegatedExecption)
            }
        }

        // When
        runner.run(listener, usecase)

        // Then
        assertSame(
            actual = capturedException.receive(),
            expected = exception
        )
    }

    private class UsecaseStub<ReturnType> : UsecaseContract.Usecase<ReturnType> {
        var exec: (() -> ReturnType)? = null

        override suspend fun execute(): ReturnType {
            return exec?.invoke() ?: throw MockException()
        }
    }

    // NEW
    @Test
    fun `Given run is called with a ResultListener, a Usecase and its corresponding Parameter, it invokes in async context the Usecase and delegates the result to the Listener`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<String>()
        val usecase = NewUsecaseStub<String, String>()

        val runner = TaskRunner(config)

        val parameter = "potato"
        val result = "soup"

        val capturedParameter = Channel<String>()
        val capturedResult = Channel<String>()

        usecase.exec = { delegatedParameter ->
            launch { capturedParameter.send(delegatedParameter) }
            result
        }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnSuccess = { delegatedResult ->
            launch { capturedResult.send(delegatedResult) }
        }

        // When
        val runnerResult = runner.run(listener, usecase, parameter)

        // Then
        assertEquals(
            expected = Unit,
            actual = runnerResult
        )
        assertEquals(
            actual = capturedParameter.receive(),
            expected = parameter
        )
        assertEquals(
            actual = capturedResult.receive(),
            expected = result
        )
    }

    @Test
    fun `Given run is called with a ResultListener, a Usecase and its corresponding Parameter, it invokes in async context the Usecase while failing and delegates the Exception to the Listener`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = ResultListenerStub<String>()
        val usecase = NewUsecaseStub<String, String>()

        val runner = TaskRunner(config)

        val parameter = "potato"

        val exception = RuntimeException("tomato")
        val capturedException = Channel<Exception>()

        usecase.exec = { throw exception }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnError = { delegatedException ->
            launch {
                capturedException.send(delegatedException)
            }
        }

        // When
        val result = runner.run(listener, usecase, parameter)

        // Then
        assertEquals(
            expected = Unit,
            actual = result
        )
        assertSame(
            actual = capturedException.receive(),
            expected = exception
        )
    }

    @Test
    fun `Given run is called with a Callback, a Usecase and its corresponding Parameter, it invokes in async context the Usecase and calls to the Callback`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = CallbackStub()
        val usecase = NewUsecaseStub<String, Unit>()

        val runner = TaskRunner(config)

        val parameter = "potato"

        val capturedParameter = Channel<String>()
        val capturedCall = Channel<Boolean>()

        usecase.exec = { delegatedParameter ->
            launch {
                capturedParameter.send(delegatedParameter)
            }
        }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnSuccess = {
            launch {
                capturedCall.send(true)
            }
        }

        // When
        val result = runner.run(listener, usecase, parameter)

        // Then
        assertEquals(
            expected = Unit,
            actual = result
        )
        assertEquals(
            actual = capturedParameter.receive(),
            expected = parameter
        )
        assertTrue(capturedCall.receive())
    }

    @Test
    fun `Given run is called with a Callback and, a Usecase and its corresponding Parameter, it invokes in async context the Usecase while failing and delegates the Exception to the Callback`() = runBlockingTest {
        // Given
        val config = ClientConfigurationStub()
        val listener = CallbackStub()
        val usecase = NewUsecaseStub<String, Unit>()

        val runner = TaskRunner(config)

        val parameter = "potato"

        val exception = RuntimeException("tomato")
        val capturedException = Channel<Exception>()

        usecase.exec = { throw exception }

        config.whenGetCoroutineScope = { CoroutineScope(this.coroutineContext) }
        listener.whenOnError = { delegatedExecption ->
            launch {
                capturedException.send(delegatedExecption)
            }
        }

        // When
        val result = runner.run(listener, usecase, parameter)

        // Then
        assertEquals(
            expected = Unit,
            actual = result
        )
        assertSame(
            actual = capturedException.receive(),
            expected = exception
        )
    }

    private class NewUsecaseStub<Parameter : Any, ReturnType : Any> : UsecaseContract.NewUsecase<Parameter, ReturnType> {
        var exec: ((Parameter) -> ReturnType)? = null

        override suspend fun execute(parameter: Parameter): ReturnType {
            return exec?.invoke(parameter) ?: throw MockException()
        }
    }
}
