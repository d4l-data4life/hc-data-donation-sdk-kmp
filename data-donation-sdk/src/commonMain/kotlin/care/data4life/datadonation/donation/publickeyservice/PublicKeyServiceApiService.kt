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

package care.data4life.datadonation.donation.publickeyservice

import care.data4life.datadonation.donation.publickeyservice.PublicKeyServiceContract.ApiService.Companion.ROUTE
import care.data4life.datadonation.donation.publickeyservice.model.RawKeys
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.head
import care.data4life.datadonation.networking.receive
import io.ktor.http.Headers

internal class PublicKeyServiceApiService(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: PublicKeyServiceContract.ApiService.ErrorHandler
) : PublicKeyServiceContract.ApiService {
    override suspend fun fetchLatestUpdate(): Headers {
        val request = requestBuilderFactory
            .create()
            .prepare(
                Networking.Method.HEAD,
                ROUTE
            )

        return try {
            head(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchLatestUpdate(error)
        }
    }

    override suspend fun fetchPublicKeys(): RawKeys {
        val request = requestBuilderFactory
            .create()
            .prepare(
                Networking.Method.GET,
                ROUTE
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchPublicKeys(error)
        }
    }
}
