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

package care.data4life.datadonation.donation.donationservice

import care.data4life.datadonation.donation.donationservice.DonationServiceContract.ApiService.Companion.DONATE
import care.data4life.datadonation.donation.donationservice.DonationServiceContract.ApiService.Companion.REGISTER
import care.data4life.datadonation.donation.donationservice.DonationServiceContract.ApiService.Companion.REVOKE
import care.data4life.datadonation.donation.donationservice.DonationServiceContract.ApiService.Companion.ROUTE
import care.data4life.datadonation.donation.donationservice.DonationServiceContract.ApiService.Companion.TOKEN
import care.data4life.datadonation.donation.donationservice.model.DeletionProof
import care.data4life.datadonation.networking.HttpRuntimeError
import care.data4life.datadonation.networking.Networking
import care.data4life.datadonation.networking.receive
import care.data4life.datadonation.networking.runForNoContent
import io.ktor.client.request.forms.MultiPartFormDataContent

internal class DonationServiceApiService(
    private val requestBuilderFactory: Networking.RequestBuilderFactory,
    private val errorHandler: DonationServiceContract.ApiService.ErrorHandler
) : DonationServiceContract.ApiService {
    override suspend fun fetchToken(): Token {
        val path = ROUTE.toMutableList().also {
            it.add(TOKEN)
        }

        val request = requestBuilderFactory
            .create()
            .prepare(
                Networking.Method.GET,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchToken(error)
        }
    }

    override suspend fun register(encryptedJSON: EncryptedJSON) {
        val path = ROUTE.toMutableList().also {
            it.add(REGISTER)
        }

        val request = requestBuilderFactory
            .create()
            .setBody(encryptedJSON)
            .prepare(
                Networking.Method.PUT,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchToken(error)
        }
    }

    override suspend fun donate(donations: MultiPartFormDataContent) {
        val path = ROUTE.toMutableList().also {
            it.add(DONATE)
        }

        val request = requestBuilderFactory
            .create()
            .setBody(donations)
            .prepare(
                Networking.Method.POST,
                path
            )

        try {
            runForNoContent(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchToken(error)
        }
    }

    override suspend fun revoke(donation: MultiPartFormDataContent): DeletionProof {
        val path = ROUTE.toMutableList().also {
            it.add(REVOKE)
        }

        val request = requestBuilderFactory
            .create()
            .setBody(donation)
            .prepare(
                Networking.Method.POST,
                path
            )

        return try {
            receive(request)
        } catch (error: HttpRuntimeError) {
            throw errorHandler.handleFetchToken(error)
        }
    }
}
