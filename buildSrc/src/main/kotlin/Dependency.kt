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

import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.exclude

object Dependency {

    object Kotlin {
        val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
        val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}"
    }

    val jvm = JVM

    object JVM {
        val slf4jApi = "org.slf4j:slf4j-api:${Version.slf4j}"
        val slf4jNop = "org.slf4j:slf4j-nop:${Version.slf4j}"
    }

    val d4l = D4L
    object D4L {
        const val sdkUtil = "care.data4life.hc-util-sdk-kmp:util:${Version.sdkUtil}"
        const val sdkUtilCoroutine = "care.data4life.hc-util-sdk-kmp:util-coroutine:${Version.sdkUtil}"
        const val sdkTestUtil = "care.data4life.hc-util-sdk-kmp:util-test:${Version.sdkUtil}"
        const val sdkTestCoroutineUtil = "care.data4life.hc-util-sdk-kmp:util-coroutine-test:${Version.sdkUtil}"
        const val sdkTestKtorUtil = "care.data4life.hc-util-sdk-kmp:util-ktor-test:${Version.sdkUtil}"
        const val fhir = "care.data4life.hc-fhir-sdk-kmp:fhir:${Version.fhir}"
    }

    val multiplatform = Multiplatform
    object Multiplatform {
        val kotlin = Kotlin

        object Kotlin {
            val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlin}"
            val stdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"
            val stdlibJs = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            val stdlibNative = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            val stdlibAndroid = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"

            val testCommon = "org.jetbrains.kotlin:kotlin-test-common:${Version.kotlin}"
            val testCommonAnnotations =
                "org.jetbrains.kotlin:kotlin-test-annotations-common:${Version.kotlin}"
            val testJvm = "org.jetbrains.kotlin:kotlin-test:${Version.kotlin}"
            val testJvmJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
        }

        val coroutines = Coroutines

        object Coroutines {
            // https://github.com/Kotlin/kotlinx.coroutines
            val common =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlinCoroutines}"
            val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlinCoroutines}"
            val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.kotlinCoroutines}"
        }

        val ktor = Ktor

        object Ktor {
            // https://ktor.io/
            val commonCore = "io.ktor:ktor-client-core:${Version.ktor}"
            val commonJson = "io.ktor:ktor-client-json:${Version.ktor}"
            val jvmCore = "io.ktor:ktor-client-core-jvm:${Version.ktor}"
            val androidCore = "io.ktor:ktor-client-android:${Version.ktor}"
            val jvmJson = "io.ktor:ktor-client-json-jvm:${Version.ktor}"
            val ios = "io.ktor:ktor-client-ios:${Version.ktor}"
            val iosCore = "io.ktor:ktor-client-core:${Version.ktor}"
            val iosJson = "io.ktor:ktor-client-json-native:${Version.ktor}"
            val commonSerialization = "io.ktor:ktor-client-serialization:${Version.ktor}"
            val androidSerialization = "io.ktor:ktor-client-serialization-jvm:${Version.ktor}"
            val iosSerialization = "io.ktor:ktor-client-serialization-native:${Version.ktor}"

            // Logger
            val logger = "io.ktor:ktor-client-logging:${Version.ktor}"

            // Testing
            val mock = "io.ktor:ktor-client-mock:${Version.ktor}"
            val jvmMock = "io.ktor:ktor-client-mock-jvm:${Version.ktor}"
            val nativeMock = "io.ktor:ktor-client-mock-native:${Version.ktor}"
        }

        val serialization = Serialization

        object Serialization {
            // https://github.com/Kotlin/kotlinx.serialization
            const val common =
                "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.kotlinSerialization}"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${Version.kotlinSerialization}"
            const val protobuf =
                "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${Version.kotlinSerialization}"
        }

        val koin = Koin

        object Koin {
            // https://github.com/InsertKoinIO/koin
            const val commonCore = "io.insert-koin:koin-core:${Version.koin}"
            const val test = "io.insert-koin:koin-test:${Version.koin}"
            const val test_junit4 = "io.insert-koin:koin-test-junit4:${Version.koin}"
            const val android = "io.insert-koin:koin-android:${Version.koin}"
            const val android_ext = "io.insert-koin:koin-android-ext:${Version.koin}"
        }

        val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.dateTime}"

        val uuid = "com.benasher44:uuid:${Version.uuid}"
    }

    val test = Test

    object Test {
        const val junit = "junit:junit:${Version.testJUnit}"
    }

    val android = Android

    object Android {
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.androidDesugar}"

        const val threeTenABP = "com.jakewharton.threetenabp:threetenabp:${Version.threeTenABP}"

        const val bouncyCastle = "org.bouncycastle:bcprov-jdk15on:${Version.bouncyCastle}"
    }

    val androidTest = AndroidTest

    object AndroidTest {
        const val core = "androidx.test:core:${Version.androidXTest}"
        const val runner = "androidx.test:runner:${Version.androidXTest}"
        const val rules = "androidx.test:rules:${Version.androidXTest}"

        const val junit = "androidx.test.ext:junit:${Version.androidXTest}"

        const val espressoCore = "androidx.test.espresso:espresso-core:${Version.androidXEspresso}"
        const val espressoIntents =
            "androidx.test.espresso:espresso-intents:${Version.androidXEspresso}"
        const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.androidXEspresso}"

        const val uiAutomator =
            "androidx.test.uiautomator:uiautomator:${Version.androidXUiAutomator}"
    }

    // Use for ktor dependency on ion
    val coroutinesExcludeNative: ExternalModuleDependency.() -> Unit = {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-native")
    }
}
