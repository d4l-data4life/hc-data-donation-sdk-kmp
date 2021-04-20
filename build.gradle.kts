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
        gradlePluginPortal()
        mavenCentral()
        google()
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

        gitHub(project)

        d4l()

        // Only because of koin 3.0.1-alpha-2, replacemt https://insert-koin.io/docs/setup/v3
        maven(url = "https://dl.bintray.com/touchlabpublic/kotlin") {
            content {
                includeGroup("org.koin")
            }
        }
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.8.3"
    distributionType = Wrapper.DistributionType.ALL
}
