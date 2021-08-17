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

package care.data4life.datadonation.consent.consentdocument

import care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract.ApiService.Companion.ROUTE
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.receive
import care.data4life.datadonation.userconsent.model.ConsentDocument
import care.data4life.datadonation.ConsentDataContract.ConsentDocument as ConsentDocumentContract

internal class ConsentDocumentApiService constructor(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract.ApiService.ErrorHandler,
) : care.data4life.datadonation.consent.consentdocument.ConsentDocumentContract.ApiService {
    override suspend fun fetchConsentDocuments(
        accessToken: String,
        consentDocumentKey: String,
        version: String?,
        language: String?,
    ): List<ConsentDocumentContract> {
        val parameter = mapOf(
            "key" to consentDocumentKey,
            "version" to version,
            "language" to language
        )

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .setParameter(parameter)
            .prepare(
                Networking.Method.GET,
                ROUTE
            )

        return try {
            receive<List<ConsentDocument>>(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchConsentDocuments(error)
        }
    }
}
