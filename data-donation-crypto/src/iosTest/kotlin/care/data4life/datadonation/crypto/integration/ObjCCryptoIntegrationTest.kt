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

package care.data4life.datadonation.crypto.integration

import care.data4life.sdk.util.objc.NSErrorFactory
import kotlinx.cinterop.*
import objc.datadonation.crypto.*
import platform.Foundation.NSError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ObjCCryptoIntegrationTest {

    private val keychainStore = DataDonationCryptoObjCFactory.dataDonationKeychainStore()
    private val keyIdentifier = "kmp.test.donor.identity"

    @Test
    fun `It imports and creates a library object without linking problems`() {
        assertNotNull(keychainStore)
    }

    @Test
    fun `It imports and runs a method from the library without linking problems`() {

        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            keychainStore.fetchKeyPairAsBase64With(keyIdentifier, errorRef.ptr)
            val error = errorRef.value
            assertNotNull(error)
        }
    }

    @Test
    fun `It gets a "could not generate Key pair" error when generating a key due to missing host app`() {

        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            keychainStore.fetchKeyPairAsBase64With(keyIdentifier, errorRef.ptr)

            val error = errorRef.value
            val expectedError = NSErrorFactory.create(
                code = 2, // DataDonationCryptoObjCError.couldNotGenerateKeyPair
                domain = DataDonationCryptoObjCErrorDomain,
                localizedDescription = "non-important",
                kotlinError = Throwable("non-important")
            )

            assertEquals(
                error!!.code,
                expectedError.code
            )

            assertEquals(
                error.domain,
                expectedError.domain
            )
        }
    }
}
