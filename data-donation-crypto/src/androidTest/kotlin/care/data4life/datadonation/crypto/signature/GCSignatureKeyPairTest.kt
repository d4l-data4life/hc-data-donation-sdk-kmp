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

import care.data4life.datadonation.crypto.D4LCryptoProtocol
import care.data4life.sdk.crypto.GCRSAKeyAlgorithm
import care.data4life.sdk.crypto.KeyOptions
import care.data4life.sdk.crypto.KeyVersion
import org.junit.Test
import java.security.Signature
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GCSignatureKeyPairTest {
    private val gcKeyPair = D4LCryptoProtocol.generateAsymKeyPair(
        algorithm = GCRSAKeyAlgorithm(),
        options = KeyOptions(
            keySize = KeyVersion.VERSION_1.asymmetricKeySize
        )
    )

    @Test
    fun `Given fromGCKeyPair is called with a GCKeyPair and a GCSignatureAlgorithm, it creates a GCSignatureKeyPair`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()
        val keyPair: Any = GCSignatureKeyPair.fromGCKeyPair(
            keyPair = gcKeyPair,
            algorithm = algo
        )

        // Then
        assertTrue(keyPair is GCSignatureKeyPair)
        assertSame(
            actual = keyPair.algorithm,
            expected = algo
        )
        assertSame(
            actual = keyPair.privateKey,
            expected = gcKeyPair.privateKey
        )
        assertSame(
            actual = keyPair.publicKey,
            expected = gcKeyPair.publicKey
        )
        assertEquals(
            actual = keyPair.keyVersion,
            expected = gcKeyPair.keyVersion
        )
    }

    @Test
    fun `Given toGCKeyPair is called with a GCRSAKeyAlgorithm, it creates a GCKeyPair`() {
        // When
        val algo = GCRSAKeyAlgorithm()
        val gcKeyPair = GCSignatureKeyPair.fromGCKeyPair(
            keyPair = gcKeyPair,
            algorithm = GCSignatureAlgorithm.createSaltedKey()
        ).toGCKeyPair(algo)

        // Then
        assertSame(
            actual = gcKeyPair.privateKey,
            expected = this.gcKeyPair.privateKey
        )
        assertSame(
            actual = gcKeyPair.publicKey,
            expected = this.gcKeyPair.publicKey
        )
        assertEquals(
            actual = gcKeyPair.algorithm.transformation,
            expected = algo.transformation
        )
        assertEquals(
            actual = gcKeyPair.keyVersion,
            expected = this.gcKeyPair.keyVersion
        )
    }

    @Test
    fun `Given toSigningSignature is called it creates Signature for signing`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()
        val signer: Any = GCSignatureKeyPair.fromGCKeyPair(
            keyPair = gcKeyPair,
            algorithm = algo
        ).toSigningSignature()

        // Then
        assertTrue(signer is Signature)
        assertEquals(
            actual = algo.transformation,
            expected = signer.algorithm
        )
    }

    @Test
    fun `Given toVerificationSignature is called it creates Signature for verification`() {
        // When
        val algo = GCSignatureAlgorithm.createSaltedKey()
        val signer: Any = GCSignatureKeyPair.fromGCKeyPair(
            keyPair = gcKeyPair,
            algorithm = algo
        ).toVerificationSignature()

        // Then
        assertTrue(signer is Signature)
        assertEquals(
            actual = algo.transformation,
            expected = signer.algorithm
        )
    }
}
