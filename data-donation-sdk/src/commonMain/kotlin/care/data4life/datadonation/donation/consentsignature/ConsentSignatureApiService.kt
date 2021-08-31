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

package care.data4life.datadonation.donation.consentsignature

import care.data4life.datadonation.donation.consentsignature.model.SignedDeletionMessage
import care.data4life.datadonation.donation.model.ConsentSignature
import care.data4life.datadonation.donation.model.ConsentSigningRequest
import care.data4life.datadonation.networking.AccessToken
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.receive
import io.ktor.client.statement.HttpStatement

internal class ConsentSignatureApiService(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: ConsentSignatureContract.ApiService.ErrorHandler
) : ConsentSignatureContract.ApiService {
    private fun buildPath(consentDocumentKey: String): List<String> {
        return ConsentSignatureContract.ApiService.ROUTE.toMutableList().also {
            it.add(consentDocumentKey)
            it.add(ConsentSignatureContract.ApiService.SIGNATURES)
        }
    }

    private fun prepareRequest(
        accessToken: AccessToken,
        path: List<String>,
        method: Networking.Method,
        payload: ConsentSigningRequest
    ): HttpStatement {
        return requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(payload)
            .prepare(method, path)
    }

    override suspend fun enableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        signingRequest: SignatureRequest
    ): ConsentSignature {
        val path = buildPath(consentDocumentKey)

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(signingRequest)
            .prepare(
                Networking.Method.POST,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleEnableSigning(error)
        }
    }

    override suspend fun sign(
        accessToken: AccessToken,
        consentDocumentKey: String,
        signingRequest: ConsentSigningRequest
    ): ConsentSignature {
        val path = buildPath(consentDocumentKey)

        val request = prepareRequest(
            accessToken,
            path,
            Networking.Method.PUT,
            signingRequest
        )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleSigning(error)
        }
    }

    override suspend fun disableSigning(
        accessToken: AccessToken,
        consentDocumentKey: String,
        deletionRequest: SignedDeletionMessage
    ) {
        val path = buildPath(consentDocumentKey)

        val request = requestBuilderFactory
            .create()
            .setAccessToken(accessToken)
            .useJsonContentType()
            .setBody(deletionRequest)
            .prepare(
                Networking.Method.DELETE,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleDisableSigning(error)
        }
    }
}
