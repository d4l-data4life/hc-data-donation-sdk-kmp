/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@file:OptIn(ExperimentalStdlibApi::class,ExperimentalUnsignedTypes::class)
package care.data4life.datadonation.encryption

import io.ktor.utils.io.core.toByteArray
import kotlin.contracts.contract

/**
 * Asn1 DSL builder and encoder.
 * Most types are not supported yet.
 * References:
 * http://luca.ntop.org/Teaching/Appunti/asn1.html
 * https://tools.ietf.org/html/rfc5208
 *
 *
 * Sample usage(for RsaSsaPrivate key proto from Tink):
 *
 *  "RSAPublicKey" sequence {
 *               "version" integer byteArrayOf(0)
 *               "modulus" integer publicKey.n
 *               "publicExponent" integer publicKey.e
 *               "privateExponent" integer d
 *               "prime1" integer p
 *               "prime2" integer q
 *               "exponent1" integer dp
 *               "exponent2" integer dq
 *               "crtCoefficient" integer crt
 *           }
 *
 */
class Asn1 constructor(private val root: List<SEQUENCE>){

    val encoded by lazy(LazyThreadSafetyMode.NONE) {
        root.map { it.encode() }.fold(ubyteArrayOf()) { a, b-> a+b }
    }

    /**
     * Encode an element as a bytearray representing TLV(Type-Length-Value)
     */
    private fun TLV<*>.encode(): UByteArray = when (val tlv = this) {
        is INTEGER -> {
            val arr = if(tlv.value[0] and 0x80u == 0x80u.toUByte()) {
                //Strip away the sign
                //This is necessary for Golang decoder, because it expects signed integers
                //And RSA-PSS Schema generally contains unsigned integers
                ubyteArrayOf(0u) + tlv.value
            } else {
                tlv.value
            }
            ubyteArrayOf(
                tlv.type(),
                *arr.size.toDerSize(),
                *arr)
        }
        is SEQUENCE -> {
            val encoded = tlv.value.map { it.encode() }.fold(ubyteArrayOf()) { a,b ->
                a+b
            }
            ubyteArrayOf(
                tlv.type(),
                *encoded.size.toDerSize(),
                *encoded)
        }
        is OCTET_STRING -> {
            val encoded = tlv.value.encode()
            ubyteArrayOf(
                tlv.type(),
                *encoded.size.toDerSize(),
                *encoded
            )
        }
        is OBJECT_IDENTIFIER -> {
            val ident = tlv.value.map { intToUInt32ByteArray(it) }.reduce { a, b ->  a + b }
            ubyteArrayOf(
                tlv.type(),
                *ident.size.toDerSize(),
                *ident.asUByteArray()
            )
        }
        is BIT_STRING -> {
            val encoded = tlv.value.encode()
            ubyteArrayOf(
                tlv.type(),
                *(encoded.size+1).toDerSize(),
                0u,
                *encoded
            )
        }
        is RAW_TLV -> {
            tlv.value.asUByteArray()
        }
    }

    /**
     * ASN1 "Length octets" - http://luca.ntop.org/Teaching/Appunti/asn1.html
     */
    private fun Int.toDerSize(): UByteArray {
        val size = intToUInt32ByteArray(this).asUByteArray()
        return if (size.size==1&&size[0]<= 127u) {
            //short form
            size
        } else {
            //long form
            ubyteArrayOf((128+size.size).toUByte(),*size)
        }
    }

    private fun intToUInt32ByteArray(value: Int): ByteArray {
        val bytes = ByteArray(4)
        bytes[3] = (value and 0xFFFF).toByte()
        bytes[2] = ((value ushr 8) and 0xFFFF).toByte()
        bytes[1] = ((value ushr 16) and 0xFFFF).toByte()
        bytes[0] = ((value ushr 24) and 0xFFFF).toByte()
        return bytes.sliceArray(bytes.indexOfFirst { it!=0.toByte() } .. 3)
    }

}


@DslMarker
annotation class Asn1Dsl

sealed class TLV<T : Any> {
    abstract val value: T
    abstract fun type(): UByte
}

/**
 * Already encoded TLV value
 */
class RAW_TLV(override val value: ByteArray):TLV<ByteArray>() {
    override fun type(): UByte = throw NotImplementedError()
}

class OBJECT_IDENTIFIER(override val  value: List<Int>) : TLV<List<Int>>() {
    override fun type(): UByte = 6u
}

class INTEGER(override val value: UByteArray) : TLV<UByteArray>() {
    override fun type(): UByte = 2u
}

class OCTET_STRING() : TLV<TLV<*>>() {
    override fun type(): UByte = 4u

    @Asn1Dsl
    infix fun String.sequence(builder: SEQUENCE.() -> Unit) {
        value = SEQUENCE().apply(builder)
    }

    @Asn1Dsl
    fun raw(bytes:ByteArray) {
        value = RAW_TLV(bytes)
    }

    override lateinit var value: TLV<*>
        private set
}

class BIT_STRING() : TLV<TLV<*>>() {
    override fun type(): UByte = 3u

    @Asn1Dsl
    infix fun String.sequence(builder: SEQUENCE.() -> Unit) {
        value = SEQUENCE().apply(builder)
    }

    @Asn1Dsl
    fun raw(bytes:ByteArray) {
        value = RAW_TLV(bytes)
    }

    override lateinit var value: TLV<*>
        private set
}

class SEQUENCE() : TLV<List<TLV<out Any>>>() {
    override val value: List<TLV<out Any>> = mutableListOf()


    @Asn1Dsl
    infix fun String.sequence(builder: SEQUENCE.() -> Unit) {
        (value as MutableList) += SEQUENCE().apply(builder)
    }

    @Asn1Dsl
    infix fun String.integer(unsigned: UByteArray) {
        (value as MutableList) += INTEGER(unsigned)
    }

    @Asn1Dsl
    infix fun String.integer(unsigned: ByteArray) {
        integer(unsigned.asUByteArray())
    }

    @Asn1Dsl
    infix fun String.octet_string(octetBuilder: OCTET_STRING.()->Unit) {
        (value as MutableList) += OCTET_STRING().apply(octetBuilder)
    }


    @Asn1Dsl
    infix fun String.bit_string(octetBuilder: BIT_STRING.()->Unit) {
        (value as MutableList) += BIT_STRING().apply(octetBuilder)
    }

    @Asn1Dsl
    infix fun String.object_identifier(identifier: List<Int>) {
        (value as MutableList) += OBJECT_IDENTIFIER(identifier)
    }

    override fun type(): UByte = 48u
}

@Asn1Dsl
infix fun String.sequence(builder: SEQUENCE.() -> Unit) =
    Asn1(listOf(SEQUENCE().apply(builder)))



interface Asn1Exportable {
    fun toAsn1():Asn1
}
