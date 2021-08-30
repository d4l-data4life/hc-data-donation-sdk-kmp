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

package care.data4life.datadonation.crypto.util

import care.data4life.datadonation.crypto.mock.ResourceLoader
import care.data4life.sdk.util.Base64
import care.data4life.sdk.util.test.annotation.RobolectricTestRunner
import care.data4life.sdk.util.test.annotation.RunWithRobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@AndroidOnly // TODO: REMOVE!!! As soon as iOS is done with it's crypto
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class CryptoVerificationTest {
    @Test
    fun `Given decode was called with a Thing and a Key, it fails if the Thing has the wrong version`() {
        // Given
        val thing = ResourceLoader.loader.load("/fixture/crypto/ExampleHybridEncryptedMessageV1.txt")
        val key = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // When
        val error = assertFailsWith<RuntimeException> {
            CryptoVerification.decrypt(
                Base64.decode(thing),
                key
            )
        }

        // Then
        assertEquals(
            actual = error.message,
            expected = "Unsupported Protocol Version 1."
        )
    }

    @Test
    fun `Given decode was called with a Thing and a Key, decrypts a given hybrid encrypted Thing`() {
        // Given
        val thing = ResourceLoader.loader.load("/fixture/crypto/ExampleHybridEncryptedMessageV2.txt")
        val expected = "{\"a\":\"Hello World!\"}"

        val key = ResourceLoader.loader.load("/fixture/crypto/DonationServicePrivateKey.txt")

        // When
        val actual = CryptoVerification.decrypt(
            Base64.decode(thing),
            key
        )

        // Then
        assertEquals(
            actual = actual.decodeToString(),
            expected = expected
        )
    }
}
