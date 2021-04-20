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

    // Publish
    id("maven-publish")
}

group = LibraryConfig.group


kotlin {
    android("android") {
        publishLibraryVariants("release")
    }

    ios {
        binaries {
            framework()
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf("-Xallow-result-return-type")
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon> {
        kotlinOptions {
            freeCompilerArgs += listOf("-Xallow-result-return-type")
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf("-Xallow-result-return-type")
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

                implementation(Dependency.Multiplatform.koin.core)

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
                implementation(Dependency.Multiplatform.koin.android_ext)

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

        configure(listOf(targets["iosArm64"], targets["iosX64"])) {
            this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

            compilations["main"].kotlinOptions.freeCompilerArgs += mutableListOf(
                "-include-binary",
                "$projectDir/native/crypto/libcrypto.a"
            )
            compilations.getByName("main") {
                this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

                val iOSDCryptoDD by cinterops.creating {
                    packageName("crypto.dd")
                    // Path to .def file
                    defFile("src/iosMain/cinterop/iOSCryptoDD.def")

                    // Directories for header search (an analogue of the -I<path> compiler option)
                    includeDirs("$projectDir/native/crypto/")
                }
            }

            binaries.all {
                linkerOpts(
                    "-L$projectDir/native/crypto",
                    "-lcrypto",
                    "-L/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/iphonesimulator",
                    "-L/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift-5.0/iphonesimulator",
                    "-rpath", "/usr/lib/swift"
                )
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


publishing {
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/gesundheitscloud/data-donation-sdk-native")
            credentials {
                username =
                    (project.findProperty("gpr.user") ?: System.getenv("USERNAME"))?.toString()
                password =
                    (project.findProperty("gpr.key") ?: System.getenv("TOKEN"))?.toString()
            }
        }

        publications {
            all {
                if (this is MavenPublication) {
                    groupId = LibraryConfig.githubGroup
                    artifactId = LibraryConfig.artifactId

                    when (name) {
                        "androidRelease" -> {
                            artifactId = "${project.name}-android"
                        }
                        "metadata" -> {
                            artifactId = "${project.name}-metadata"
                        }
                        "jvm" -> {
                            artifactId = "${project.name}-jvm"
                        }
                        "iosArm64" -> {
                            artifactId = "${project.name}-iosarm64"
                        }
                        "iosX64" -> {
                            artifactId = "${project.name}-iosx64"
                        }
                        else -> {
                            artifactId = "${project.name}-common"
                        }
                    }

                    pom {
                        name.set(LibraryConfig.name)
                        url.set(LibraryConfig.url)
                        inceptionYear.set(LibraryConfig.inceptionYear)
                        licenses {
                            license {
                                name.set(LibraryConfig.licenseName)
                                url.set(LibraryConfig.licenseUrl)
                                distribution.set(LibraryConfig.licenseDistribution)
                            }
                        }
                        developers {
                            developer {
                                id.set(LibraryConfig.developerId)
                                name.set(LibraryConfig.developerName)
                                email.set(LibraryConfig.developerEmail)
                            }
                        }

                        scm {
                            connection.set(LibraryConfig.scmConnection)
                            developerConnection.set(LibraryConfig.scmDeveloperConnection)
                            url.set(LibraryConfig.scmUrl)
                        }
                    }
                }
            }
        }
    }
}
