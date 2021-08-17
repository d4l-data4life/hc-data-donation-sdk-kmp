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

package care.data4life.datadonation.consent.userconsent

import care.data4life.sdk.lang.D4LRuntimeException

sealed class UserConsentError(
    open val httpStatus: Int
) : D4LRuntimeException() {
    class UnexpectedFailure(override val httpStatus: Int) : UserConsentError(httpStatus)
    class BadRequest : UserConsentError(400)
    class Unauthorized : UserConsentError(401)
    class Forbidden : UserConsentError(403)
    class NotFound : UserConsentError(404)
    class DocumentConflict : UserConsentError(409)
    class UnprocessableEntity : UserConsentError(422)
    class TooManyRequests : UserConsentError(429)
    class InternalServer : UserConsentError(500)
}
