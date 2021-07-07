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

package care.data4life.datadonation.internal.domain.mock

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.internal.domain.repository.RepositoryInternalContract
import care.data4life.datadonation.mock.MockException

class MockConsentDocumentRepository : RepositoryInternalContract.ConsentDocumentRepository {
    var whenFetchConsentDocuments: ((language: String?, version: Int?, consentKey: String) -> List<ConsentDocument>)? = null

    override suspend fun fetchConsentDocuments(
        language: String?,
        version: Int?,
        consentKey: String
    ): List<ConsentDocument> {
        return whenFetchConsentDocuments?.invoke(language, version, consentKey) ?: throw MockException()
    }
}
