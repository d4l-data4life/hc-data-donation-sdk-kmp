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
    kotlinMultiplatform()
    kotlinSerialization()

    // Android
    androidLibrary()

    // Swift
    swiftPackage()

    // Publish
    id("scripts.publishing-config")
}

group = LibraryConfig.group

val swiftPackageName = "Data4LifeDataDonationSDK"
val swiftModuleName = "d4l_data_donation_sdk_kmp" to "Data4LifeDataDonationSDK"
val swiftBundleIdentifier = "care.data4life.datadonation.d4l_data_donation_sdk_kmp" to "care.data4life.sdk.d4l-data-donation-sdk"
val swiftTargetDirectory = File(rootDir, "/ios-framework/Data4LifeDataDonationSDK")

val uselessSwiftProtocols = mapOf(
    "ConsentDataContract" to "D4l_data_donation_sdk_kmpConsentDataContract",
    "DataDonationSDKPublicAPI" to "D4l_data_donation_sdk_kmpDataDonationSDKPublicAPI"
)
val swiftNameMapping = mapOf(
    "DataDonationSDKPublicAPIUserSessionTokenProvider" to "UserSessionTokenProvider"
)


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

                implementation(Dependency.multiplatform.koin.commonCore)

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.stately.isolate)

                implementation(Dependency.multiplatform.ktor.commonCore)
                implementation(Dependency.multiplatform.ktor.logger)
                implementation(Dependency.multiplatform.ktor.commonJson)
                implementation(Dependency.multiplatform.ktor.commonSerialization)

                implementation(Dependency.multiplatform.serialization.common)

                implementation(Dependency.multiplatform.dateTime)

                // D4L
                implementation(Dependency.d4l.fhir)
                implementation(Dependency.d4l.sdkUtil)
                implementation(Dependency.d4l.sdkUtilCoroutine)
            }
        }
        commonTest {
            kotlin.srcDir("src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependency.multiplatform.kotlin.testCommon)
                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
                implementation(Dependency.multiplatform.koin.test)
                implementation(Dependency.multiplatform.ktor.mock)

                // D4L
                implementation(Dependency.d4l.sdkTestUtil)
                implementation(Dependency.d4l.sdkTestCoroutineUtil)
                implementation(Dependency.d4l.sdkTestKtorUtil)
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
                implementation(Dependency.d4l.sdkUtil)
            }
        }
        val iosTest by getting {
            dependencies {
                dependsOn(commonTest.get())
            }
        }
    }

    multiplatformSwiftPackage {
        swiftToolsVersion("5.4")
        packageName(swiftPackageName)
        zipFileName(swiftPackageName)
        outputDirectory(swiftTargetDirectory)
        distributionMode { local() }
        targetPlatforms {
            iOS { v("13") }
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


project.afterEvaluate {
    val swiftPackageCleaner by tasks.register("cleanXCFramework") {
        group = "Multiplatform-swift-package"
        description = "Custom cleaner tasks to avoid empty protocols"

        dependsOn(tasks.getByName("createXCFramework"))
        doFirst {
            // File operation
            project.fileTree(swiftTargetDirectory).forEach { file ->
                if (file.absolutePath.endsWith(".h")) {
                    var source = file.readText(Charsets.UTF_8)
                    uselessSwiftProtocols.forEach { (swiftName, objectiveCInterfaceName) ->
                        source = source.replace(
                            "__attribute__((swift_name(\"$swiftName\")))\n" +
                                "@protocol $objectiveCInterfaceName\n" +
                                "@required\n" +
                                "@end;\n",
                            "// removed $swiftName\n"
                        )
                    }

                    swiftNameMapping.forEach { (originalName, newName) ->
                        source = source.replace(
                            "__attribute__((swift_name(\"$originalName\")))",
                            "__attribute__((swift_name(\"$newName\")))"
                        )
                    }
                    file.writeText(source, Charsets.UTF_8)
                }

                if (file.absolutePath.endsWith(".modulemap") || file.absolutePath.endsWith(".plist")) {
                    val source = file.readText(Charsets.UTF_8)
                        .replace(swiftBundleIdentifier.first, swiftBundleIdentifier.second)
                        .replace(swiftModuleName.first, swiftModuleName.second)

                    file.writeText(source, Charsets.UTF_8)
                }

                if (file.absolutePath.endsWith("${swiftModuleName.first}.h")) {
                    val newFile = File(
                        file.absolutePath.substringBeforeLast("${swiftModuleName.first}.h") +
                            "${swiftModuleName.second}.h"
                    )
                    file.copyTo(newFile, true)
                    file.delete()
                }

                if (file.absolutePath.endsWith(swiftModuleName.first)) {
                    val newFile = File(
                        file.absolutePath.substringBeforeLast(swiftModuleName.first) +
                            swiftModuleName.second
                    )
                    file.copyTo(newFile, true)
                    file.delete()
                }
            }

            // Dir operations
            project.fileTree(swiftTargetDirectory).forEach { folder ->
                if (folder.absolutePath.endsWith(swiftModuleName.second)) {
                    val newFolder = File(
                        folder.parentFile.absolutePath.replace(swiftModuleName.first, swiftModuleName.second)
                    )
                    folder.parentFile.renameTo(newFolder)
                }
            }
        }
    }

    tasks.getByName("createSwiftPackage") {
        dependsOn(swiftPackageCleaner)
    }
}

