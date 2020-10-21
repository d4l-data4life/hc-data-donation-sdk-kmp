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

package care.data4life.datadonation.internal.utils

import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlin.experimental.and
import kotlin.native.concurrent.SharedImmutable

interface Base64Encoder {
    fun encode(src: ByteArray): String
    fun decode(src: ByteArray, charset: Charset): String
}

object Base64Factory {
    fun createEncoder(): Base64Encoder = CommonBase64Encoder
}

object CommonBase64Encoder : Base64Encoder {

    override fun encode(src: ByteArray): String = src.encodeBase64()

    override fun decode(src: ByteArray, charset: Charset): String = TODO("Not yet implemented")

}

private const val BASE64_ALPHABET =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private const val BASE64_MASK: Byte = 0x3f
private const val BASE64_PAD = '='

@SharedImmutable
private val BASE64_INVERSE_ALPHABET = IntArray(256) {
    BASE64_ALPHABET.indexOf(it.toChar())
}

//region encode
fun ByteArray.encodeBase64(): String = buildPacket {
    writeFully(this@encodeBase64)
}.encodeBase64()

fun String.encodeBase64(): String = buildPacket {
    writeText(this@encodeBase64)
}.encodeBase64()

fun ByteReadPacket.encodeBase64(): String = buildString {
    val data = ByteArray(3)
    while (remaining > 0) {
        val read = readAvailable(data)
        data.clearFrom(read)

        val padSize = (data.size - read) * 8 / 6
        val chunk = ((data[0].toInt() and 0xFF) shl 16) or
                ((data[1].toInt() and 0xFF) shl 8) or
                (data[2].toInt() and 0xFF)

        for (index in data.size downTo padSize) {
            val char = (chunk shr (6 * index)) and BASE64_MASK.toInt()
            append(char.toBase64())
        }

        repeat(padSize) { append(BASE64_PAD) }
    }
}

//endregion
//region decode
fun String.decodeBase64String(charset: Charset): String =
    String(decodeBase64Bytes(), charset = charset)

fun String.decodeBase64Bytes(): ByteArray = buildPacket {
    writeText(dropLastWhile { it == BASE64_PAD })
}.decodeBase64Bytes().readBytes()

fun ByteReadPacket.decodeBase64Bytes(): Input = buildPacket {
    val data = ByteArray(4)

    while (remaining > 0) {
        val read = readAvailable(data)

        val chunk = data.foldIndexed(0) { index, result, current ->
            result or (current.fromBase64().toInt() shl ((3 - index) * 6))
        }

        for (index in data.size - 2 downTo (data.size - read)) {
            val origin = (chunk shr (8 * index)) and 0xff
            writeByte(origin.toByte())
        }
    }
}

internal fun ByteArray.clearFrom(from: Int) {
    (from until size).forEach { this[it] = 0 }
}

internal fun Int.toBase64(): Char = BASE64_ALPHABET[this]
internal fun Byte.fromBase64(): Byte =
    BASE64_INVERSE_ALPHABET[toInt() and 0xff].toByte() and BASE64_MASK



