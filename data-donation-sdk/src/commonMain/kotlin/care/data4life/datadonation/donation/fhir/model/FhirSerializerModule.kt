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

import care.data4life.hl7.fhir.stu3.model.FhirHelper
import care.data4life.hl7.fhir.stu3.model.FhirResource
import care.data4life.hl7.fhir.stu3.model.FhirStu3
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic

internal object FhirSerializerModule {

    fun module(): SerializersModule {
        val collector = FhirModuleCollector()
        FhirHelper.FhirSerializationModule.module().dumpTo(collector)

        val parentModule = SerializersModule {
            polymorphic(FhirStu3::class) {
                collector.stu3Mapping.forEach { (clazz, serializer) ->
                    subclass(clazz, serializer)
                }
            }

            polymorphic(FhirResource::class) {
                collector.elementMapping.forEach { (clazz, serializer) ->
                    subclass(clazz, serializer)
                }
            }
        }

        return SerializersModule {
            polymorphic(FhirResource::class) {
                subclass(
                    DataDonationQuestionnaireResponse::class,
                    DataDonationQuestionnaireResponse.serializer()
                )
            }
        }.plus(parentModule)
    }
}
