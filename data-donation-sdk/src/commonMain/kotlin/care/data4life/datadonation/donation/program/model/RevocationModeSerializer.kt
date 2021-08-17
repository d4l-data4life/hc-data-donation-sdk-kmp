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

package care.data4life.datadonation.donation.program.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object RevocationModeSerializer : KSerializer<ProgramModelContract.RevocationMode> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RevocationMode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ProgramModelContract.RevocationMode) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ProgramModelContract.RevocationMode {
        val value = decoder.decodeString().toUpperCase()
        return ProgramModelContract.RevocationMode.valueOf(value)
    }
}
