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

import care.data4life.gradle.datadonation.config.LibraryConfig
import care.data4life.gradle.datadonation.dependency.Dependency

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")
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

    iosArm64 {
        val platform = "iphoneos"
        val libraryName = "DataDonationCryptoObjC"
        val libraryPath = "$rootDir/$libraryName/build/Build/Products/Release-$platform"
        val frameworksPath = "$libraryPath"

        compilations.getByName("main") {
            cinterops.create("DataDonationCryptoObjC") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":DataDonationCryptoObjC:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/DataDonationCryptoObjC.def")
                includeDirs(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        compilations.getByName("test") {
            cinterops.create("DataDonationCryptoObjC") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":DataDonationCryptoObjC:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/DataDonationCryptoObjC.def")
                includeDirs.headerFilterOnly(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        binaries.all {
            linkerOpts(
                "-rpath", "$frameworksPath",
                "-L$libraryPath", "-l$libraryName",
                "-F$frameworksPath", "-framework", "Data4LifeCrypto"
            )
        }
    }

    iosX64 {
        val platform = "iphonesimulator"
        val libraryName = "DataDonationCryptoObjC"
        val libraryPath = "$rootDir/$libraryName/build/Build/Products/Release-$platform"
        val frameworksPath = "$libraryPath"

        compilations.getByName("main") {
            cinterops.create("DataDonationCryptoObjC") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":DataDonationCryptoObjC:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/DataDonationCryptoObjC.def")
                includeDirs(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        compilations.getByName("test") {
            cinterops.create("DataDonationCryptoObjC") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":DataDonationCryptoObjC:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/DataDonationCryptoObjC.def")
                includeDirs(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        binaries.all {
            linkerOpts(
                "-rpath", "$frameworksPath",
                "-L$libraryPath", "-l$libraryName",
                "-F$frameworksPath", "-framework", "Data4LifeCrypto"
            )
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            }
        }
        commonMain {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibCommon)

                implementation(Dependency.multiplatform.koin.commonCore)

                implementation(Dependency.multiplatform.stately.freeze)
                implementation(Dependency.multiplatform.stately.isolate)

                // D4L
                implementation(Dependency.d4l.sdkUtil)
                implementation(Dependency.d4l.sdkError)
            }
        }
        commonTest {
            kotlin.srcDir("src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependency.multiplatform.kotlin.testCommon)
                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
                implementation(Dependency.multiplatform.koin.test)

                // D4L
                implementation(Dependency.d4l.sdkTestUtil)
                implementation(Dependency.d4l.sdkTestCoroutineUtil)
            }
        }

        val androidMain by getting {
            dependencies {
                //DI
                implementation(Dependency.jvm.slf4jNop)
                implementation(Dependency.jvm.slf4jApi)
            }
        }
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest.get())

                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
                implementation(Dependency.androidTest.robolectric)
            }
        }

        val iosMain by getting {
            dependencies {
                // D4L
                implementation(Dependency.d4l.sdkObjcUtil)
            }
        }
        val iosTest by getting {
            dependencies {
                dependsOn(commonTest.get())
                implementation(Dependency.d4l.sdkObjcUtil)
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

    resourcePrefix(
        "${LibraryConfig.android.resourcePrefix}_crypto_"
    )

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

val templatesPath = "${projectDir}/src/commonTest/resources/template"
val configPath = "${projectDir}/src-gen/commonTest/kotlin/care/data4life/datadonation/test/config"

val provideTestConfig: Task by tasks.creating {
    doFirst {
        val templates = File(templatesPath)
        val configs = File(configPath)

        val config = File(templates, "TestConfig.tmpl")
            .readText()
            .replace("PROJECT_DIR", projectDir.toPath().toAbsolutePath().toString())

        if (!configs.exists()) {
            if(!configs.mkdir()) {
                System.err.println("The script not able to create the config directory")
            }
        }
        File(configPath, "TestConfig.kt").writeText(config)
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class.java) {
    if (this.name.contains("Test")) {
        this.dependsOn(provideTestConfig)
    }
}
