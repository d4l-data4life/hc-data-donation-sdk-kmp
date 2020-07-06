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

