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

package care.data4life.datadonation.error

import care.data4life.sdk.lang.D4LRuntimeException

sealed class CoreRuntimeError(
    message: String?,
    cause: Throwable?
) : D4LRuntimeException(message = message, cause = cause) {
    class InternalFailure(message: String? = null) : CoreRuntimeError(message = message ?: "Internal failure", cause = null)
    class RequestValidationFailure(message: String) : CoreRuntimeError(message = message, cause = null)
    class ResponseTransformFailure : CoreRuntimeError(message = "Unexpected Response", cause = null)
    class MissingCredentials(message: String? = null, cause: Throwable? = null) : CoreRuntimeError(cause = cause, message = message)
    class MissingSession(cause: Throwable? = null) : CoreRuntimeError(cause = cause, message = null)
}
