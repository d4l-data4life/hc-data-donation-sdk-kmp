import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")

    // Android
    id("com.android.library")

    // iOS
    id("co.touchlab.native.cocoapods")

    // DB
}

version = LibraryConfig.version
group = LibraryConfig.group


kotlin {
    android("android") {
        publishLibraryVariants("release")
    }
    jvm("jvm")

    // Revert to just ios() when gradle plugin can properly resolve it
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    targets.getByName<KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
        listOf("-Xobjc-generics", "-Xg0")//TODO this does not seems to work

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.stdlibCommon)
                implementation(Dependency.Multiplatform.stately)
                implementation(Dependency.Multiplatform.multiplatformSettings)

                implementation(Dependency.Multiplatform.koin.core)

                implementation(Dependency.Multiplatform.coroutines.common)

                implementation(Dependency.Multiplatform.ktor.commonCore)
                implementation(Dependency.Multiplatform.ktor.commonJson)
                implementation(Dependency.Multiplatform.ktor.commonSerialization)

            }
        }
        commonTest {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.testCommon)
                implementation(Dependency.Multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val androidMain by getting {
            dependencies {
                //Kotlin
                implementation(Dependency.Multiplatform.kotlin.stdlibAndroid)
                implementation(Dependency.Multiplatform.coroutines.android)

                //SDK
                implementation(Dependency.android.d4l.hcSDK) {
                    // exclude the threetenbp dependency from the `sdk` module
                    exclude(group = "org.threeten", module = "threetenbp")
                    // temporal exclude for modularization
                    exclude(
                        group = "com.github.gesundheitscloud.hc-sdk-android",
                        module = "crypto-jvm"
                    )
                    exclude(
                        group = "com.github.gesundheitscloud.hc-sdk-android",
                        module = "auth-jvm"
                    )
                    exclude(
                        group = "com.github.gesundheitscloud.hc-sdk-android",
                        module = "sdk-jvm"
                    )
                    exclude(
                        group = "com.github.gesundheitscloud.hc-sdk-android",
                        module = "securestore-jvm"
                    )
                    exclude(
                        group = "com.github.gesundheitscloud.hc-sdk-android",
                        module = "util-jvm"
                    )
                }
                implementation(Dependency.android.d4l.hcSdkCrypto)
                implementation(Dependency.android.d4l.hcSdkSecurestore)
                implementation(Dependency.android.d4l.hcSdkHelper)
                implementation(Dependency.android.d4l.fhir)
                //DI
                implementation(Dependency.Multiplatform.koin.android_ext)

                //
                implementation(Dependency.android.threeTenABP)
                implementation(Dependency.Multiplatform.ktor.androidSerialization)

            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.testJvm)
                implementation(Dependency.Multiplatform.kotlin.testJvmJunit)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.stdlibJdk8)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.testJvm)
                implementation(Dependency.Multiplatform.kotlin.testJvmJunit)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Dependency.Multiplatform.coroutines.native) {
                    version {
                        strictly("1.3.5-native-mt")
                    }
                }
                implementation(Dependency.Multiplatform.ktor.iosSerialization)


            }
        }
        val iosTest by getting {
            dependencies {

            }
        }
    }

    cocoapodsext {
        frameworkName = "Common"
        summary = "Common library for the kotlin multi platforn template"
        homepage = "https://github.com/gesundheitscloud/d4l-kotlin-multiplatform-template"
        isStatic = false
    }
}

android {
    compileSdkVersion(LibraryConfig.android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(LibraryConfig.android.minSdkVersion)
        targetSdkVersion(LibraryConfig.android.targetSdkVersion)

        versionCode = LibraryConfig.android.versionCode
        versionName = LibraryConfig.android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(
            mapOf(
                "clearPackageData" to "true"
            )
        )
    }

    resourcePrefix(LibraryConfig.android.resourcePrefix)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(setOf("src/androidMain/kotlin"))
            res.setSrcDirs(setOf("src/androidMain/res"))
        }

        getByName("test") {
            java.setSrcDirs(setOf("src/androidTest/kotlin"))
            res.setSrcDirs(setOf("src/androidTest/res"))
        }
    }
}

