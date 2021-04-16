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

import com.moowork.gradle.node.yarn.YarnTask

plugins {
    // https://github.com/node-gradle/gradle-node-plugin
    id("com.github.node-gradle.node") version "2.2.3"
}

// https://github.com/node-gradle/gradle-node-plugin/blob/master/docs/node.md
node {
    // https://nodejs.org
    version = "13.12.0"

    // https://yarnpkg.com/package/yarn
    yarnVersion = "1.22.4"

    download = true
}

// Add required dependencies here
val yarnInstall by tasks.registering(YarnTask::class) {
    description = "Installs dependencies to the project"

    setYarnCommand("add")

    setArgs(
        setOf(
            "@antora/cli@2.2.0",
            "@antora/site-generator-default@2.2.0",
            "@asciidoctor/core@2.1.1",
            // https://github.com/Mogztter/asciidoctor-kroki
            "asciidoctor-kroki@0.7.0",
            // https://github.com/tapio/live-server
            "live-server@1.2.1",
            "broken-link-checker@0.7.8",

            // other 3rd dependencies
            "mkdirp@1.0.3"
        )
    )
}

val yarnOutdated by tasks.registering(YarnTask::class) {
    description = "Checks for outdated yarn dependencies"
    setYarnCommand("outdated")
}


val buildDocs by tasks.registering(YarnTask::class) {
    dependsOn(yarnInstall)

    description = "Executes Antora to build the site"

    setYarnCommand("run")

    inputs.files(project.fileTree("modules"), "antora.yml", "site-local.yml")
    outputs.dir("${project.buildDir}/public/")

    setArgs(
        setOf(
            "antora",
            "--html-url-extension-style=indexify",
            "--attribute", "project-version=${project.version}",
            "site.yml"
        )
    )
}


val serveDocs by tasks.registering(YarnTask::class) {
    dependsOn(yarnInstall)

    description = "Serves the build site"

    setYarnCommand("run")

    setArgs(
        setOf(
            "live-server",
            "build/public",
            "--ignore=_"
        )
    )
}

