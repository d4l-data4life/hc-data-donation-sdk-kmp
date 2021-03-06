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

package care.data4life.gradle.datadonation.config

object LibraryConfig {
    const val group = "care.data4life.datadonation"
    const val name = "hc-data-donation-sdk-kmp"

    const val githubOwner = "d4l-data4life"
    const val githubRepository = "hc-data-donation-sdk-kmp"

    val publish = PublishConfig

    object PublishConfig {
        const val name = LibraryConfig.name
        const val groupId = "care.data4life.hc-data-donation-sdk-kmp"

        const val description = "Kotlin multi platform library implementing Data Donation on the Data4life ecosystem."
        const val year = "2021"

        // URL
        const val host = "github.com"
        const val path = "$githubOwner/$githubRepository"

        const val url = "https://$host/$path"

        // DEVELOPER
        const val developerId = "d4l-data4life"
        const val developerName = "D4L data4life gGmbH"
        const val developerEmail = "mobile@data4life.care"

        // LICENSE
        const val licenseName = "Private"
        const val licenseUrl = "$url/blob/main/LICENSE"
        const val licenseDistribution = "repo"

        // SCM
        const val scmUrl = "git://$host/$path.git"
        const val scmConnection = "scm:$scmUrl"
        const val scmDeveloperConnection = scmConnection
    }

    val android = AndroidLibraryConfig

    object AndroidLibraryConfig {
        const val minSdkVersion = 23
        const val compileSdkVersion = 30
        const val targetSdkVersion = 30

        const val resourcePrefix = "d4l_data_donation_"
    }

    val iOS = SwiftLibraryConfig
    object SwiftLibraryConfig {
        const val packageName = "Data4LifeDataDonationSDK"
        const val targetVersion = "13"
        const val toolVersion = "5.4"
    }
}
