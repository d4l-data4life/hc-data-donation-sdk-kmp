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
    swiftPackage()

    // SwiftPackage
    swiftPackage(version = "2.0.3")

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
                baseName = LibraryConfig.iOS.packageName
            }
        }
    }

    multiplatformSwiftPackage {
        swiftToolsVersion(LibraryConfig.iOS.toolVersion)
        packageName(LibraryConfig.iOS.packageName)
        zipFileName(LibraryConfig.iOS.packageName)
        outputDirectory(
            File(rootDir, "${File.separator}swift${File.separator}${LibraryConfig.iOS.packageName}")
        )
        distributionMode { local() }
        targetPlatforms {
            iOS { v(LibraryConfig.iOS.targetVersion) }
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
                //DI
                implementation(Dependency.jvm.slf4jNop)
                implementation(Dependency.jvm.slf4jApi)

                //Ktor
                implementation(Dependency.multiplatform.ktor.androidCore)
            }
        }
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest.get())

                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
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

val uselessSwiftProtocols = listOf(
    "ConsentDataContract",
    "DataDonationSDKPublicAPI"
)
val referencePrefix = "DLDDSDK"
val swiftNameReplacements = emptyMap<String, String>()

project.afterEvaluate {
    val swiftTargetDirectory = File(rootDir, "${File.separator}swift${File.separator}${LibraryConfig.iOS.packageName}")

    val swiftPackageCleaner by tasks.register("cleanXCFramework") {
        group = "Multiplatform-swift-package"
        description = "Custom cleaner tasks to avoid empty protocols and to provide better names for Swift"

        dependsOn(tasks.getByName("createXCFramework"))
        doFirst {
            project.fileTree(swiftTargetDirectory).forEach { file ->
                if (file.absolutePath.endsWith(".h")) {
                    var source = file.readText(Charsets.UTF_8)
                    uselessSwiftProtocols.forEach { protocolName ->
                        var replacementBarrier = source.contains("__attribute__((swift_name(\"$protocolName\")))")
                        source = source.replace(
                            "__attribute__((swift_name(\"$protocolName\")))\n" +
                                "@protocol $referencePrefix$protocolName\n" +
                                "@required\n" +
                                "@end;",
                            "// removed $protocolName"
                        )
                        replacementBarrier = replacementBarrier && !source.contains("__attribute__((swift_name(\"$protocolName\")))")

                        if(replacementBarrier) {
                            source = source.replace(
                                Regex("__attribute__\\(\\(swift_name\\(\"$protocolName([A-Z][a-zA-Z]+)\"\\)\\)\\)"),
                                "__attribute__((swift_name(\"$1\"))) //$protocolName$1 -> $1"
                            )
                        }
                    }

                    swiftNameReplacements.forEach { (originalName, newName) ->
                        source = source.replace(
                            "__attribute__((swift_name(\"$originalName\")))",
                            "__attribute__((swift_name(\"$newName\")))"
                        )
                    }

                    file.writeText(source, Charsets.UTF_8)
                }
            }
        }
    }

    tasks.getByName("createSwiftPackage") {
        dependsOn(swiftPackageCleaner)
    }
}
