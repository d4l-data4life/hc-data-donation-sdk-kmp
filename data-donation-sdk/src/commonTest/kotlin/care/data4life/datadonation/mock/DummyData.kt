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

package care.data4life.datadonation.mock

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.ModelContract.ConsentEvent
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.datadonation.internal.data.model.ConsentSignature
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DummyData {
    val timestamp = "2020-07-06T10:18:12.601Z"

    val userConsent = UserConsent(
        "key",
        "1.0.0",
        "a486a4db-a850-4b1d-9c84-99aa027f1000",
        ConsentEvent.Consent,
        timestamp
    )

    val consentSignature = ConsentSignature("Hello world!")

    val consentDocument = ConsentDocument("", 1, "", "", "", "en", "", true, "", "")
}
