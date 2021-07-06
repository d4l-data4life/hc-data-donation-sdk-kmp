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
                implementation(Dependency.Multiplatform.kotlin.stdlibCommon)
                implementation(Dependency.Multiplatform.stately)

                implementation(Dependency.Multiplatform.koin.commonCore)

                implementation(Dependency.Multiplatform.coroutines.common)

                implementation(Dependency.Multiplatform.ktor.commonCore)
                implementation(Dependency.Multiplatform.ktor.logger)
                implementation(Dependency.Multiplatform.ktor.commonJson)
                implementation(Dependency.Multiplatform.ktor.commonSerialization)

                implementation(Dependency.Multiplatform.serialization.common)
                implementation(Dependency.Multiplatform.serialization.protobuf)

                implementation(Dependency.Multiplatform.dateTime)

                implementation(Dependency.Multiplatform.Fhir.common)

                implementation(Dependency.Multiplatform.uuid)
            }
        }
        commonTest {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.testCommon)
                implementation(Dependency.Multiplatform.kotlin.testCommonAnnotations)
                implementation(Dependency.Multiplatform.koin.test)
                implementation(Dependency.Multiplatform.ktor.mock)
            }
        }

        val androidMain by getting {
            dependencies {
                //Kotlin
                implementation(Dependency.Multiplatform.kotlin.stdlibAndroid)
                implementation(Dependency.Multiplatform.coroutines.android)

                //DI
                implementation(Dependency.Multiplatform.koin.android)
                implementation(Dependency.Java.slf4jSimple)
                implementation(Dependency.Java.slf4jApi)

                //
                implementation(Dependency.android.threeTenABP)
                implementation(Dependency.Multiplatform.ktor.androidCore)
                implementation(Dependency.Multiplatform.ktor.androidSerialization)
                implementation(Dependency.android.bouncyCastle)
                implementation(Dependency.Multiplatform.serialization.android)
                implementation(Dependency.Multiplatform.serialization.protobuf)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.testJvm)
                implementation(Dependency.Multiplatform.kotlin.testJvmJunit)
                dependsOn(commonTest.get())
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Dependency.Multiplatform.coroutines.common) {
                    version {
                        strictly(Version.kotlinCoroutines)
                    }
                }
                implementation(Dependency.Multiplatform.serialization.common)
                implementation(Dependency.Multiplatform.serialization.protobuf)
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
