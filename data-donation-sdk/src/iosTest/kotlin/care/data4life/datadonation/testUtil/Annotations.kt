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

package care.data4life.datadonation.testUtil

import kotlin.reflect.KClass

abstract class AbstractNoop
abstract class NoopParent<T>: AbstractNoop()
class NoopFramework
open class NoopBlock : NoopParent<NoopFramework>()
open class NoopSandBox : NoopBlock()
class NoopRunner: NoopSandBox()
annotation class NoopRunWith(val value: KClass<out AbstractRunner>)

actual typealias AbstractRunner = AbstractNoop
actual typealias FrameworkMethod = NoopFramework
actual typealias ParentRunner<T> = NoopParent<T>
actual typealias BlockClassRunner = NoopBlock
actual typealias SandboxTestRunner = NoopSandBox
actual typealias Runner = NoopRunner
actual typealias RunWith = NoopRunWith
