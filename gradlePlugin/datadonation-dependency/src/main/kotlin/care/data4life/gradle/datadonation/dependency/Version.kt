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

package care.data4life.gradle.datadonation.dependency

object Version {

    // Gradle Plugin
    object GradlePlugin {
        const val kotlin = Version.kotlin

        const val kotlinSerialization = Version.kotlinSerialization

        // Android
        // https://developer.android.com/studio
        const val android = "7.0.2"
    }

    // Kotlin
    // https://github.com/JetBrains/kotlin
    const val kotlin = "1.5.21"

    // https://github.com/Kotlin/kotlinx.coroutines
    const val kotlinCoroutines = "1.5.1-native-mt"

    // https://github.com/touchlab/Stately
    const val stately = "1.1.6"

    // https://github.com/Kotlin/kotlinx.serialization
    const val kotlinSerialization = "1.2.2"

    // https://github.com/Kotlin/kotlinx-datetime
    const val dateTime = "0.2.1"

    // https://developer.android.com/studio/write/java8-support
    const val androidDesugar = "1.0.9"

    // https://github.com/JakeWharton/ThreeTenABP/
    const val threeTenABP = "1.3.1"

    // AndroidX
    object AndroidX {
        /**
         * [AndroidX](https://developer.android.com/jetpack/androidx)
         */
        const val core = "1.1.0"
        const val ktx = "1.6.0"
        const val appCompat = "1.3.1"
        const val browser = "1.2.0"

        const val constraintLayout = "2.0.1"
    }

    const val androidXLifecycle = "2.1.0"
    const val androidXNavigation = "2.2.0"

    const val androidXTest = "1.4.1"
    const val androidXEspresso = "3.4.0"
    const val androidXUiAutomator = "2.2.0"

    // Test
    const val robolectric = "4.6.1"

    // Material
    const val material = "1.4.0"

    // Injection
    // https://github.com/InsertKoinIO/koin
    const val koin = "3.0.2"

    // http://www.slf4j.org/
    const val slf4j = "1.7.31"

    // Network
    // https://ktor.io/
    const val ktor = "1.6.2"

    // https://github.com/benasher44/uuid
    const val uuid = "0.3.1"

    // Encryption
    const val bouncyCastle = "1.64"

    // https://github.com/d4l-data4life/hc-fhir-sdk-kmp
    const val fhir = "0.2.0-bump-updates-SNAPSHOT"

    // https://github.com/d4l-data4life/hc-util-sdk-kmp
    const val sdkUtil = "1.11.0"

    // https://github.com/d4l-data4life/hc-objc-util-sdk-kmp
    const val sdkObjcUtil = "0.2.0"

    // https://github.com/d4l-data4life/hc-util-coroutine-sdk-kmp
    const val sdkCoroutineUtil = "0.1.0-SNAPSHOT"

    // https://github.com/d4l-data4life/hc-result-sdk-kmp
    const val sdkResult = "0.3.0"

    // https://github.com/d4l-data4life/hc-test-util-sdk-kmp
    const val sdkTestUtil = "0.3.0"

    // Junit Test
    const val testJUnit = "4.13.2"
}
