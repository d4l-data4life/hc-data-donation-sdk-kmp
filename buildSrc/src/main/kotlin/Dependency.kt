import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.exclude

object Dependency {

    object GradlePlugin {
        const val android = "com.android.tools.build:gradle:${Version.androidGradlePlugin}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
        const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Version.kotlin}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin.plugin.serialization:${Version.kotlin}"
        const val cocoapodsext = "co.touchlab:kotlinnativecocoapods:${Version.cocoapodsext}"

        const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Version.sqlDelight}"
    }

    val kotlin = Kotlin

    object Kotlin {
        val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
        val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}"
    }

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

        val coroutinesIO = CoroutinesIO

        object CoroutinesIO {
            //https://github.com/Kotlin/kotlinx-io
            val common =
                "org.jetbrains.kotlinx:kotlinx-coroutines-io:${Version.kotlinCoroutinesIO}"
            val java =
                "org.jetbrains.kotlinx:kotlinx-coroutines-io-jvm:${Version.kotlinCoroutinesIO}"
            val native =
                "org.jetbrains.kotlinx:kotlinx-coroutines-io-native:${Version.kotlinCoroutinesIO}"
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
            val iosCore = "io.ktor:ktor-client-core-native:${Version.ktor}"
            val iosJson = "io.ktor:ktor-client-json-native:${Version.ktor}"
            val commonSerialization = "io.ktor:ktor-client-serialization:${Version.ktor}"
            val androidSerialization = "io.ktor:ktor-client-serialization-jvm:${Version.ktor}"
            val iosSerialization = "io.ktor:ktor-client-serialization-native:${Version.ktor}"

            //Logger
            val logger = "io.ktor:ktor-client-logging:${Version.ktor}"

            //Testing
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

        val sqlDelight = SqlDelight

        object SqlDelight {
            // https://github.com/cashapp/sqldelight
            const val jdk = "com.squareup.sqldelight:sqlite-driver:${Version.sqlDelight}"
            const val js = "com.squareup.sqldelight:sqljs-driver:${Version.sqlDelight}"
            const val native = "com.squareup.sqldelight:native-driver:${Version.sqlDelight}"
            const val android = "com.squareup.sqldelight:android-driver:${Version.sqlDelight}"
            const val ios = "com.squareup.sqldelight:ios-driver:${Version.sqlDelight}"
        }

        val koin = Koin

        object Koin {
            // https://github.com/InsertKoinIO/koin
            const val core = "org.koin:koin-core:${Version.koin}"
            const val coreExt = "org.koin:koin-core-ext:${Version.koin}"
            const val test = "org.koin:koin-test:${Version.koin}"
            const val android_scope = "org.koin:koin-androidx-scope:${Version.koin}"
            const val android_vm = "org.koin:koin-androidx-viewmodel:${Version.koin}"
            const val android_fragment = "org.koin:koin-androidx-fragment:${Version.koin}"
            const val android_ext = "org.koin:koin-androidx-ext:${Version.koin}"
            const val tor = "org.koin:koin-tor:${Version.koin}"
        }

        val stately = "co.touchlab:stately-common:${Version.stately}"
        val multiplatformSettings =
            "com.russhwolf:multiplatform-settings:${Version.multiplatformSettings}"


        val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.dateTime}"

        object Fhir {
            const val common = "care.data4life.hc-fhir-sdk-kmp:fhir:${Version.fhir}"
            const val jvm = "care.data4life.hc-fhir-sdk-kmp:fhir-jvm:${Version.fhir}"
            const val android = "care.data4life.hc-fhir-sdk-kmp:fhir-android:${Version.fhir}"
            const val iosArm64 = "care.data4life.hc-fhir-sdk-kmp:fhir-iosarm64:${Version.fhir}"
            const val iosX64 = "care.data4life.hc-fhir-sdk-kmp:fhir-iosx64:${Version.fhir}"
        }

    }

    val test = Test

    object Test {
        const val junit = "junit:junit:${Version.testJUnit}"
    }

    val android = Android

    object Android {
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.androidDesugar}"

        // AndroidX
        const val androidXCoreKtx = "androidx.core:core-ktx:${Version.androidXKtx}"
        const val androidXAppCompat = "androidx.appcompat:appcompat:${Version.androidXAppCompat}"
        const val androidXBrowser = "androidx.browser:browser:${Version.androidXBrowser}"
        const val androidXConstraintLayout =
            "androidx.constraintlayout:constraintlayout:${Version.androidXConstraintLayout}"

        // AndroidX - Lifecylce
        const val androidXLifecylceCommonJava8 =
            "androidx.lifecycle:lifecycle-common-java8:${Version.androidXLifecycle}"
        const val androidXLifecylceExtensions =
            "androidx.lifecycle:lifecycle-extensions:${Version.androidXLifecycle}"

        // AndroidX - Navigation
        const val androidXNavigationFragmentKtx =
            "androidx.navigation:navigation-fragment-ktx:${Version.androidXNavigation}"
        const val androidXNavigationUiKtx =
            "androidx.navigation:navigation-ui-ktx:${Version.androidXNavigation}"

        // Material
        const val material = "com.google.android.material:material:${Version.material}"

        val d4l = D4L

        object D4L {
            const val hcSDK =
                "com.github.gesundheitscloud.hc-sdk-android:sdk-android:${Version.hcSDKAndroid}"
            const val hcSdkSecurestore =
                "com.github.gesundheitscloud.hc-sdk-android:securestore-android:${Version.hcSDKAndroid}"
            const val hcSdkCrypto =
                "com.github.gesundheitscloud.hc-sdk-android:crypto-android:${Version.hcSDKAndroid}"
            const val hcSdkHelper =
                "com.github.gesundheitscloud.sdk-fhir-helper-multiplatform:fhir-helper-android:${Version.hcSdkHelperAndroid}"
            const val hcSdkUtils =
                "de.gesundheitscloud.sdk-util-multiplatform:util-android:${Version.hcSdkUtilsAndroid}"
            const val fhir = "de.gesundheitscloud:hc-fhir-android:${Version.fhirAndroid}"
        }

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

    //Use for ktor dependency on ion
    val coroutinesExcludeNative: ExternalModuleDependency.() -> Unit = {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-native")
    }
}
