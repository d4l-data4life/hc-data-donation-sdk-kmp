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
import care.data4life.sdk.datadonation.LibraryConfig
import care.data4life.sdk.datadonation.dependency.Dependency
import care.data4life.sdk.datadonation.dependency.Version

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization")

    // Android
    id("com.android.library")

    // Publish
    id("care.data4life.sdk.datadonation.publishing-config")
}

group = LibraryConfig.group

kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
    }

    ios {
        binaries {
            framework {
                baseName = LibraryConfig.iOS.packageName
            }
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.serialization.InternalSerializationApi")
                useExperimentalAnnotation("kotlinx.serialization.ExperimentalSerializationApi")
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
                useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            }
        }
        commonMain {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibCommon)

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.stately.freeze)

                implementation(Dependency.multiplatform.serialization.common)

                // D4L
                implementation(Dependency.d4l.sdkFlow)
                implementation(Dependency.d4l.sdkError)
            }
        }
        commonTest {
        }

        val androidMain by getting {
            dependencies {
            }
        }
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest.get())
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.coroutines.common) {
                    version {
                        strictly(Version.kotlinCoroutines)
                    }
                }
            }
        }
        val iosTest by getting {
            dependencies {
                dependsOn(commonTest.get())
            }
        }
    }
}

android {
    compileSdkVersion(LibraryConfig.android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(LibraryConfig.android.minSdkVersion)
        targetSdkVersion(LibraryConfig.android.targetSdkVersion)

        versionCode = 1
        versionName = "${project.version}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(
            mapOf(
                "clearPackageData" to "true"
            )
        )
    }

    resourcePrefix(LibraryConfig.android.resourcePrefix + "api_")

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
