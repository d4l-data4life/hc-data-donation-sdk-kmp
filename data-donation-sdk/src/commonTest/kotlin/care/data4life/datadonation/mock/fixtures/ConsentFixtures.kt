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

package care.data4life.datadonation.mock.fixtures

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.ModelContract
import care.data4life.datadonation.core.model.UserConsent
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ConsentFixtures {
    val sampleConsentDocument = ConsentDocument(
        key = "exampleKey",
        version = 42,
        processor = "anyProcessor",
        description = "this is an example document",
        recipient = "sombody",
        language = "zxx-Hant-CN-x-private1-private2",
        text = "ExampleText",
        requiresToken = false,
        studyId = "exampleId",
        programName = "example"
    )

    val sampleUserConsent = UserConsent(
        consentDocumentKey = "soup",
        consentDocumentVersion = "23",
        accountId = "123e4567-e89b-12d3-a456-426614174000",
        event = ModelContract.ConsentEvent.Consent,
        createdAt = "1970-01-01T00:01:30Z"
    )
}
