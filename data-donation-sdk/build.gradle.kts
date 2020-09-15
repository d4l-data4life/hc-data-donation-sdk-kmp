plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")

    // Android
    id("com.android.library")

    // Publish
    id("maven-publish")
}

version = LibraryConfig.version
group = LibraryConfig.group


kotlin {

    android("android") {
        publishLibraryVariants("release")
    }

    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
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
                useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
                useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            }
        }
        commonMain {
            dependencies {
                implementation(Dependency.Multiplatform.kotlin.stdlibCommon)
                implementation(Dependency.Multiplatform.stately)

                implementation(Dependency.Multiplatform.koin.core)

                implementation(Dependency.Multiplatform.coroutines.common)

                implementation(Dependency.Multiplatform.ktor.commonCore)
                implementation(Dependency.Multiplatform.ktor.commonJson)
                implementation(Dependency.Multiplatform.ktor.commonSerialization)

                implementation(Dependency.Multiplatform.serialization.common)
                implementation(Dependency.Multiplatform.serialization.protobuf)
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

                //DI
                implementation(Dependency.Multiplatform.koin.android_ext)

                //
                implementation(Dependency.android.threeTenABP)
                implementation(Dependency.Multiplatform.ktor.androidSerialization)
                implementation(Dependency.android.tink)
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


         configure(listOf(targets.asMap["ios"]!!)) {

            compilations["main"].kotlinOptions.freeCompilerArgs += mutableListOf(
                "-include-binary", "$projectDir/Pods/Tink/Frameworks/Tink.framework/Tink.a"
            )
            compilations.getByName("main") {

                this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

                val tink by cinterops.creating {
                    packageName("google.tink")
                    defFile = file("$projectDir/src/iosMain/cinterop/Tink.def")
                    header("$projectDir/Pods/Tink/Frameworks/Tink.framework/Headers/Tink.h")
                }

                val cryptoSwift by cinterops.creating {
                    packageName("crypto.swift")
                    defFile = file("$projectDir/src/iosMain/cinterop/CryptoSwiftWrapper.def")
                    headers("$projectDir/native/iOSCryptoDD/build/iOSCryptoDD.framework/Headers/iOSCryptoDD.h",
                        "$projectDir/native/iOSCryptoDD/build/iOSCryptoDD.framework/Headers/iOSCryptoDD-Swift.h")
                    includeDirs("$projectDir/native/iOSCryptoDD/build/iOSCryptoDD.framework/","$projectDir/native/iOSCryptoDD/")
                }
            }
        }
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

publishing {
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/gesundheitscloud/data-donation-sdk-native")
            credentials {
                username =
                    (project.findProperty("gpr.user") ?: System.getenv("USERNAME"))?.toString()
                password = (project.findProperty("gpr.key") ?: System.getenv("TOKEN"))?.toString()
            }
        }

        publications {
            all {
                if (this is MavenPublication) {
                    groupId = LibraryConfig.githubGroup
                    artifactId = LibraryConfig.artifactId
                    version = LibraryConfig.version

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
                            artifactId = "${project.name}-iosArm64"
                        }
                        "iosX64" -> {
                            artifactId = "${project.name}-iosX64"
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
