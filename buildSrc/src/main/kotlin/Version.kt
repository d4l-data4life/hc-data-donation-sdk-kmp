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

object Version {

    // Gradle Plugin
    object GradlePlugin {
        const val kotlin = Version.kotlin

        // Android
        // https://developer.android.com/studio
        const val android = "4.1.0"
    }

    // Kotlin
    // https://github.com/JetBrains/kotlin
    const val kotlin = "1.4.32"

    // https://github.com/Kotlin/kotlinx.coroutines
    const val kotlinCoroutines = "1.4.2-native-mt"
    const val kotlinCoroutinesIO = "0.1.16"

    // https://github.com/Kotlin/kotlinx.serialization
    const val kotlinSerialization = "1.0.1"

    // https://github.com/touchlab/Stately
    const val stately = "1.1.0"

    // https://github.com/Kotlin/kotlinx-datetime
    const val dateTime = "0.1.1"

    // https://developer.android.com/studio/write/java8-support
    const val androidDesugar = "1.0.4"

    const val hcSDKAndroid = "8c5268e858"
    const val hcSdkHelperAndroid = "1.2.2"
    const val hcSdkUtilsAndroid = "1.3.1"
    const val fhirAndroid = "0.3.0"

    // https://github.com/JakeWharton/ThreeTenABP/
    const val threeTenABP = "1.1.1"

    // AndroidX
    const val androidX = "1.1.0"
    const val androidXKtx = "1.2.0"
    const val androidXAppCompat = "1.1.0"
    const val androidXBrowser = "1.2.0"

    const val androidXConstraintLayout = "2.0.0-beta4"

    const val androidXLifecycle = "2.1.0"
    const val androidXNavigation = "2.2.0"

    const val androidXTest = "1.1.1"
    const val androidXEspresso = "3.2.0"
    const val androidXUiAutomator = "2.2.0"

    // Material
    const val material = "1.1.0-rc01"

    // Injection
    // https://github.com/InsertKoinIO/koin
    const val koin = "3.0.1"

    // Network
    // https://ktor.io/
    const val ktor = "1.4.2"

    // https://github.com/benasher44/uuid
    const val uuid = "0.2.3"

    // Encryption
    const val bouncyCastle = "1.64"

    // https://github.com/d4l-data4life/hc-fhir-sdk-kmp
    const val fhir = "0.1.0"

    // Junit Test
    const val testJUnit = "4.13"
}
