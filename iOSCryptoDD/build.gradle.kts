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

listOf("iphoneos", "iphonesimulator").forEach { sdk ->
    tasks.create<Exec>("build${sdk.capitalize()}") {
        group = "build"

        commandLine(
            "xcodebuild",
            "-project", "iOSCryptoDD.xcodeproj",
            "-scheme", "iOSCryptoDD",
            "-configuration", "Release",
            "BITCODE_GENERATION_MODE=bitcode",
            "OTHER_CFLAGS=\"-fembed-bitcode\"",
            "-sdk", sdk,
            "-arch" , if(sdk == "iphoneos") "arm64" else "x86_64"
        )
        workingDir(projectDir)

        inputs.files(
            fileTree("$projectDir/iOSCryptoDD.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/iOSCryptoDD")
        )
        outputs.files(
            fileTree("$projectDir/build/Release-${sdk}")
        )
    }
}

tasks.create<Delete>("clean") {
    group = "build"

    delete("$projectDir/build")
}