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

plugins {
    // Android
    androidLibrary()

    kotlinMultiplatform()
    kotlinSerialization()

    // Publish
    id("scripts.publishing-config")
}

group = LibraryConfig.group

kotlin {
    android("android") {
        publishLibraryVariants("release", "debug")
    }

    ios {
        binaries {
            framework {
                baseName = LibraryConfig.name
            }
        }
    }

    iosArm64 {
        val platform = "iphoneos"
        compilations.getByName("main") {
            cinterops.create("iOSCryptoDD") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":iOSCryptoDD:build${platform.capitalize()}")
                includeDirs.headerFilterOnly("$rootDir/iOSCryptoDD/build/Release-$platform/include")
            }
        }
    }

    iosX64 {
        val platform = "iphonesimulator"
        compilations.getByName("main") {
            cinterops.create("iOSCryptoDD") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":iOSCryptoDD:build${platform.capitalize()}")
                includeDirs.headerFilterOnly("$rootDir/iOSCryptoDD/build/Release-$platform/include")
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
                implementation(Dependency.multiplatform.stately)

                implementation(Dependency.multiplatform.koin.commonCore)

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.ktor.commonCore)
                implementation(Dependency.multiplatform.ktor.logger)
                implementation(Dependency.multiplatform.ktor.commonJson)
                implementation(Dependency.multiplatform.ktor.commonSerialization)

                implementation(Dependency.multiplatform.serialization.common)
                implementation(Dependency.multiplatform.serialization.protobuf)

                implementation(Dependency.multiplatform.dateTime)

                implementation(Dependency.multiplatform.uuid)

                // D4L
                implementation(Dependency.d4l.fhir)
                implementation(Dependency.d4l.util)
            }
        }
        commonTest {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testCommon)
                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
                implementation(Dependency.multiplatform.koin.test)
                implementation(Dependency.multiplatform.ktor.mock)

                // D4L
                implementation(Dependency.d4l.testUtil)
            }
        }

        val androidMain by getting {
            dependencies {
                //Kotlin
                implementation(Dependency.multiplatform.kotlin.stdlibAndroid)
                implementation(Dependency.multiplatform.coroutines.android)

                //DI
                implementation(Dependency.multiplatform.koin.android)
                implementation(Dependency.jvm.slf4jNop)
                implementation(Dependency.jvm.slf4jApi)

                //
                implementation(Dependency.android.threeTenABP)
                implementation(Dependency.multiplatform.ktor.androidCore)
                implementation(Dependency.multiplatform.ktor.androidSerialization)
                implementation(Dependency.android.bouncyCastle)
                implementation(Dependency.multiplatform.serialization.android)
                implementation(Dependency.multiplatform.serialization.protobuf)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
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
                implementation(Dependency.multiplatform.serialization.common)
                implementation(Dependency.multiplatform.serialization.protobuf)
                implementation(Dependency.multiplatform.ktor.iosCore)
                implementation(Dependency.multiplatform.ktor.ios)

                // D4L
                implementation(Dependency.d4l.util)
            }
        }
        val iosTest by getting {
            dependencies {
                dependsOn(commonTest.get())
            }
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + "-Xallow-result-return-type"
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
