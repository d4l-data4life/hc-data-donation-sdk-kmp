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

import care.data4life.datadonation.crypto.util.AndroidOnly
import care.data4life.datadonation.crypto.util.CryptoVerification
import care.data4life.sdk.util.test.annotation.RobolectricTestRunner
import care.data4life.sdk.util.test.annotation.RunWithRobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertTrue

@AndroidOnly // TODO: Remove once iOS s ready
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class KeyFactoryTest {
    @Test
    fun `It fulfils the Crypto KeyFactory`() {
        val factory: Any = KeyFactory

        assertTrue(factory is CryptoContract.KeyFactory)
    }

    @Test
    fun `Given createKeyPair is called, it generates a KeyPair`() {
        // When
        val pair = KeyFactory.createKeyPair()

        // Then
        assertTrue(CryptoVerification.isPublicKey(pair.publicKey))
        assertTrue(CryptoVerification.isPrivateKey(pair.privateKey))
    }
}
