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

package care.data4life.datadonation.internal.data.storage

import care.data4life.datadonation.Contract
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CredentialsDataStorageTest {
    private val provider = ClientConfigurationStub()

    @BeforeTest
    fun setup() {
        provider.clear()
    }

    @Test
    fun `It fulfils CredentialsDataStorage`() {
        val store: Any = CredentialsDataStorage(ClientConfigurationStub())

        assertTrue(store is StorageContract.CredentialsDataStorage)
    }

    @Test
    fun `Given getDataDonationPublicKey it returns the delegated DataDonationKey`() {
        // Given
        val provider = ClientConfigurationStub()
        val key = "potato"

        provider.whenGetServicePublicKey = { service ->
            if (service == Contract.Service.DD) {
                key
            } else {
                throw RuntimeException("This should not happen")
            }
        }

        val store = CredentialsDataStorage(provider)

        // When
        val result = store.getDataDonationPublicKey()

        // Then
        assertEquals(
            actual = result,
            expected = key
        )
    }

    @Test
    fun `Given getAnalyticsPlatformPublicKey it returns the delegated AnalyticsPlatformKey`() {
        // Given
        val provider = ClientConfigurationStub()
        val key = "tomato"

        provider.whenGetServicePublicKey = { service ->
            if (service == Contract.Service.ALP) {
                key
            } else {
                throw RuntimeException("This should not happen")
            }
        }

        val store = CredentialsDataStorage(provider)

        // When
        val result = store.getAnalyticsPlatformPublicKey()

        // Then
        assertEquals(
            actual = result,
            expected = key
        )
    }
}
