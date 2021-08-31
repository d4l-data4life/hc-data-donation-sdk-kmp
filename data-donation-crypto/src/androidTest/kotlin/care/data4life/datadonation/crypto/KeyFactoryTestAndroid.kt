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

package care.data4life.datadonation.crypto

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KeyFactoryTestAndroid {
    @Test
    fun `Given createKeyPair is called, it propagates Errors as CryptoErrors`() {
        // Given
        mockkObject(CryptoKeyFactory)

        val nestedError = RuntimeException()
        every {
            CryptoKeyFactory.generateAsymmetricKeyPair()
        } throws nestedError

        // Then
        val error = assertFailsWith<CryptoError.MalFormedKeyGeneration> {
            // When
            KeyFactory.createKeyPair()
        }

        assertEquals(
            actual = error.message,
            expected = "Invalid KeyGeneration Configuration."
        )

        unmockkObject(CryptoKeyFactory)
    }
}