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

package care.data4life.datadonation.donation.fhir.model

import care.data4life.hl7.fhir.stu3.model.FhirResource
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json

internal object FhirSerializer {

    val parser = Json {
        encodeDefaults = false
        isLenient = false
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = false
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        classDiscriminator = "resourceType"

        serializersModule = FhirSerializerModule.module()
    }

    fun toFhir(resource: String): FhirResource {
        return parser.decodeFromString(
            PolymorphicSerializer(FhirResource::class),
            resource
        )
    }
}
