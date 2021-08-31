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

package care.data4life.datadonation.crypto.signature

import org.junit.Test
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PSSParameterSpec
import kotlin.test.assertEquals

class GCSignatureAlgorithmTest {
    @Test
    fun `Given createUnsaltedKey is called it creates a GCSignatureAlgorithm with Salt of length 0`() {
        // When
        val algo = GCSignatureAlgorithm.createUnsaltedKey()

        // Then
        assertEquals(
            actual = algo.salt.length,
            expected = 0
        )
    }

    @Test
    fun `Given createSaltedKey is called it creates a GCSignatureAlgorithm with Salt of length 32`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()

        // Then
        assertEquals(
            actual = algo.salt.length,
            expected = 32
        )
    }

    @Test
    fun `It has a AlgorithmParameterSpec`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()

        // Then
        assertEquals(
            actual = algo.spec.toString(),
            expected = PSSParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                32,
                1
            ).toString()
        )
    }

    @Test
    fun `It has a transformation`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()

        // Then
        assertEquals(
            actual = algo.transformation,
            expected = "SHA256withRSA/PSS"
        )
    }
}
