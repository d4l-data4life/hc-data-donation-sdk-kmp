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

package care.data4life.datadonation.mock.spy

import care.data4life.sdk.flow.D4LSDKFlow
import care.data4life.sdk.flow.D4LSDKFlowFactoryContract
import care.data4life.sdk.util.coroutine.DomainErrorMapperContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class D4LFlowFactorySpy : D4LSDKFlowFactoryContract {
    private lateinit var internalScope: CoroutineScope
    val capturedScope: CoroutineScope
        get() = internalScope

    private lateinit var internalFlow: Flow<*>
    val capturedFlow: Flow<*>
        get() = internalFlow

    private lateinit var internalErrorMapper: DomainErrorMapperContract
    val capturedErrorMapper: DomainErrorMapperContract
        get() = internalErrorMapper

    override fun <T : Any> getInstance(
        defaultScope: CoroutineScope,
        internalFlow: Flow<T>,
        domainErrorMapper: DomainErrorMapperContract
    ): D4LSDKFlow<T> {
        this.internalScope = defaultScope
        this.internalFlow = internalFlow
        this.internalErrorMapper = domainErrorMapper

        return D4LSDKFlow.getInstance(defaultScope, internalFlow, domainErrorMapper)
    }
}
