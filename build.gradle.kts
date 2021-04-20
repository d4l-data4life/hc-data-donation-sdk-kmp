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

buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath(GradlePlugin.kotlin)
        classpath(GradlePlugin.android)
    }
}

plugins {
    kotlinMultiplatform(false)
    kotlinSerialization(false)

    id("scripts.dependency-updates")
    id("scripts.download-scripts")
    id("scripts.versioning")
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()

        maven("https://kotlin.bintray.com/kotlin")
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://jitpack.io")
        maven(url = "https://dl.bintray.com/touchlabpublic/kotlin")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://dl.bintray.com/korlibs/korlibs")
        maven("https://raw.github.com/d4l-data4life/maven-repository/main/features")
        maven("https://raw.github.com/d4l-data4life/maven-repository/main/snapshots")
        maven("https://raw.github.com/d4l-data4life/maven-repository/main/releases")
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.8.3"
    distributionType = Wrapper.DistributionType.ALL
}
