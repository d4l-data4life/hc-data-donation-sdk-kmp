/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

version = AppConfig.version
group = AppConfig.group

android {
    compileSdkVersion(AppConfig.android.compileSdkVersion)

    defaultConfig {
        applicationId = AppConfig.android.applicationId

        minSdkVersion(AppConfig.android.minSdkVersion)
        targetSdkVersion(AppConfig.android.targetSdkVersion)

        versionCode = AppConfig.android.versionCode
        versionName = AppConfig.android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(
            mapOf(
                "clearPackageData" to "true"
            )
        )

        manifestPlaceholders = mapOf(
            "clientId" to "31be119e-3782-4db8-a24a-1490eea27ed3#android",
            "clientSecret" to "androidsupersecret",
            "environment" to "development",
            "redirectScheme" to "de.gesundheitscloud.31be119e-3782-4db8-a24a-1490eea27ed3",
            "debug" to true
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(Dependency.kotlin.stdLib)
    implementation(Dependency.android.androidXCoreKtx)
    implementation(Dependency.android.androidXAppCompat)
    implementation(Dependency.android.androidXConstraintLayout)
    implementation(Dependency.Multiplatform.coroutines.android)

    implementation(Dependency.Multiplatform.koin.core)
    implementation(Dependency.Multiplatform.koin.android_vm)
    implementation(Dependency.Multiplatform.koin.android_ext)

    implementation(Dependency.Multiplatform.ktor.androidCore)

    testImplementation(Dependency.test.junit)
    androidTestImplementation(Dependency.androidTest.junit)
    androidTestImplementation(Dependency.androidTest.espressoCore)
}
