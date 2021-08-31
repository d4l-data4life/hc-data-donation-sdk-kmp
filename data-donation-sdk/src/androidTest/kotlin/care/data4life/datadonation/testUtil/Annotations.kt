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

import org.junit.runners.BlockJUnit4ClassRunner
import org.robolectric.RobolectricTestRunner

actual typealias AbstractRunner = org.junit.runner.Runner
actual typealias ParentRunner<T> = org.junit.runners.ParentRunner<T>
actual typealias FrameworkMethod = org.junit.runners.model.FrameworkMethod
actual typealias BlockClassRunner = BlockJUnit4ClassRunner
actual typealias SandboxTestRunner = org.robolectric.internal.SandboxTestRunner
actual typealias RobolectricTestRunner = RobolectricTestRunner
actual typealias RunWith = org.junit.runner.RunWith
