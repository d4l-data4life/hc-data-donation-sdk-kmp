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

package care.data4life.datadonation.consentdocument

import care.data4life.datadonation.ConsentDataContract
import care.data4life.datadonation.networking.HttpRuntimeError

internal interface ConsentDocumentContract {
    interface ApiService {
        suspend fun fetchConsentDocuments(
            accessToken: String,
            version: String?,
            language: String?,
            consentDocumentKey: String
        ): List<ConsentDataContract.ConsentDocument>

        companion object {
            val PATH = listOf("consent", "api", "v1", "consentDocuments")
        }

        interface ErrorHandler {
            fun handleFetchConsentDocuments(error: HttpRuntimeError): ConsentDocumentServiceError
        }
    }

    interface ConsentDocumentRepository {
        suspend fun fetchConsentDocuments(
            language: String?,
            version: String?,
            consentDocumentKey: String
        ): List<ConsentDataContract.ConsentDocument>
    }
}
