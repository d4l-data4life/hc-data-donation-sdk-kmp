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

package care.data4life.datadonation.mock.stub.crypto

import care.data4life.datadonation.crypto.CryptoContract
import care.data4life.datadonation.mock.MockContract
import care.data4life.datadonation.mock.MockException

class CryptoServiceStub : CryptoContract.Service, MockContract.Stub {
    var whenEncrypt: ((ByteArray, String) -> ByteArray)? = null
    var whenSign: ((ByteArray, String, Int) -> ByteArray)? = null

    override fun encrypt(payload: ByteArray, publicKey: String): ByteArray {
        return whenEncrypt?.invoke(payload, publicKey) ?: throw MockException()
    }

    override fun sign(payload: ByteArray, privateKey: String, saltLength: Int): ByteArray {
        return whenSign?.invoke(payload, privateKey, saltLength) ?: throw MockException()
    }

    override fun clear() {
        whenEncrypt = null
        whenSign = null
    }
}
