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
import care.data4life.hl7.fhir.stu3.model.FhirStu3
import care.data4life.hl7.fhir.stu3.model.QuestionnaireResponse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModuleCollector
import kotlin.reflect.KClass

internal class FhirModuleCollector : SerializersModuleCollector {
    val stu3Mapping = mutableMapOf<KClass<FhirStu3>, KSerializer<FhirStu3>>()
    val elementMapping = mutableMapOf<KClass<FhirResource>, KSerializer<FhirResource>>()

    override fun <T : Any> contextual(kClass: KClass<T>, serializer: KSerializer<T>) {
        throw IllegalStateException()
    }

    override fun <Base : Any, Sub : Base> polymorphic(
        baseClass: KClass<Base>,
        actualClass: KClass<Sub>,
        actualSerializer: KSerializer<Sub>
    ) {
        if (baseClass == FhirStu3::class) {
            stu3Mapping[actualClass as KClass<FhirStu3>] = actualSerializer as KSerializer<FhirStu3>
        } else {
            if (actualClass != QuestionnaireResponse::class) {
                elementMapping[actualClass as KClass<FhirResource>] = actualSerializer as KSerializer<FhirResource>
            }
        }
    }

    override fun <Base : Any> polymorphicDefault(
        baseClass: KClass<Base>,
        defaultSerializerProvider: (className: String?) -> DeserializationStrategy<out Base>?
    ) {
        throw IllegalStateException()
    }
}
