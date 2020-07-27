import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")

    // Android
    id("com.android.library")

    // DB
}

version = LibraryConfig.version
group = LibraryConfig.group


kotlin {
    android("android") {
        publishLibraryVariants("release")
    }

    // Revert to just ios() when gradle plugin can properly resolve it
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    targets.getByName<KotlinNativeTarget>("ios").compilations["main"].kotlinOptions.freeCompilerArgs +=
        listOf("-Xobjc-generics", "-Xg0")

    sourceSets {
        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                useExperimentalAnnotation("kotlinx.serialization.InternalSerializationApi")
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

                implementation(Dependency.Multiplatform.protobuff.common)
                implementation(Dependency.Multiplatform.protobuff.common_runtime)
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
                implementation (Dependency.android.tink)

                implementation(Dependency.Multiplatform.protobuff.android)
            }
        }
        val androidTest by getting {
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
                implementation(Dependency.Multiplatform.protobuff.native)
            }
        }
        val iosTest by getting {
            dependencies {
                dependsOn(commonTest.get())
            }
        }

        configure(listOf(targets["ios"])) {
            compilations["main"].kotlinOptions.freeCompilerArgs = mutableListOf(
                "-include-binary", "$projectDir/Pods/Tink/Frameworks/Tink.framework/Tink.a"
            )
            compilations.getByName("main") {
                this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

                val tink by cinterops.creating {
                    packageName("google.tink")
                    defFile = file("$projectDir/src/iosMain/cinterop/Tink.def")
                    header("$projectDir/Pods/Tink/Frameworks/Tink.framework/Headers/Tink.h")
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

