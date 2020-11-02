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

object LibraryConfig {
    const val version = "0.0.15-RC1"
    const val group = "care.data4life.datadonation.common"
    const val githubGroup = "com.github.gesundheitscloud"
    const val artifactId = "data-donation-sdk-native"
    const val versionCode = 1
    const val name = "gesundheitscloud/$artifactId"
    const val host = "github.com"
    const val url = "https://$host/$name"
    const val inceptionYear = "2020"

    // DEVELOPER
    const val developerId = "gesundheitscloud"
    const val developerName = "D4L data4life gGmbH"
    const val developerEmail = "mobile@data4life.care"

    // LICENSE
    const val licenseName = ""
    const val licenseUrl = "$url/blob/main/LICENSE"
    const val licenseDistribution = "repo"

    // SCM
    const val scmUrl = "git://$host/$name.git"
    const val scmConnection = "scm:$scmUrl"
    const val scmDeveloperConnection = "$scmConnection"

    val android = AndroidLibraryConfig

    object AndroidLibraryConfig {
        const val minSdkVersion = 23
        const val compileSdkVersion = 29
        const val targetSdkVersion = 29

        const val versionCode = LibraryConfig.versionCode
        const val versionName = LibraryConfig.version

        const val resourcePrefix = "d4l_data_donation_"
    }
}
