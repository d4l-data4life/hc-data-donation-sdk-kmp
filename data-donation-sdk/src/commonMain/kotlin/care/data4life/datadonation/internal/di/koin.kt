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

package care.data4life.datadonation.internal.di

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.model.ModelContract.Environment
import care.data4life.datadonation.internal.data.service.networking.plugin.resolveKtorPlugins
import care.data4life.datadonation.internal.data.service.networking.resolveNetworking
import care.data4life.datadonation.internal.data.service.resolveServiceModule
import care.data4life.datadonation.internal.domain.repository.resolveRepositoryModule
import care.data4life.datadonation.internal.domain.usecases.*
import care.data4life.datadonation.internal.runner.CredentialProvider
import care.data4life.datadonation.internal.runner.ScopeProvider
import care.data4life.datadonation.internal.runner.UserSessionTokenProvider
import care.data4life.datadonation.internal.runner.resolveUsecaseRunnerModule
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

internal fun initKoin(configuration: Contract.Configuration): KoinApplication {
    return koinApplication {
        modules(
            resolveRootModule(configuration),
            resolveNetworking(),
            resolveKtorPlugins(),
            resolveUsecaseRunnerModule(),
            resolveUsecaseModule(),
            resolveRepositoryModule(),
            resolveServiceModule()
        )
    }
}

internal fun resolveRootModule(configuration: Contract.Configuration): Module {
    return module {
        single {
            configuration
        } binds arrayOf(
            Contract.Configuration::class,
            ScopeProvider::class,
            CredentialProvider::class,
            UserSessionTokenProvider::class
        )

        single<Environment> { configuration.getEnvironment() }

        single<Clock> { Clock.System }
    }
}
