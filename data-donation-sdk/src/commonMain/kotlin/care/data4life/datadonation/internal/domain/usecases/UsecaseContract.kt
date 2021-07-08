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

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.core.model.KeyPair
import care.data4life.datadonation.core.model.UserConsent
import care.data4life.hl7.fhir.stu3.model.FhirResource

interface UsecaseContract {
    interface NewUsecase<Parameter : Any, ReturnType : Any> {
        suspend fun execute(parameter: Parameter): ReturnType
    }

    interface Usecase<ReturnType> {
        suspend fun execute(): ReturnType
    }

    interface UsecaseFactory<Parameter : Any, ReturnType : Any> {
        fun withParams(parameter: Parameter): Usecase<ReturnType>
    }

    interface FetchUserConsents : NewUsecase<FetchUserConsents.FetchUserConsentsParameter, List<UserConsent>> {
        interface FetchUserConsentsParameter {
            val consentKey: String?
        }
    }

    interface FetchConsentDocumentsParameter {
        val version: Int?
        val language: String?
        val consentKey: String
    }

    interface FetchConsentDocuments : UsecaseFactory<FetchConsentDocumentsParameter, List<ConsentDocument>>

    interface RevokeUserConsentParameter {
        val consentKey: String
    }

    interface RevokeUserConsent : UsecaseFactory<RevokeUserConsentParameter, Unit>

    interface CreateUserConsentParameter {
        val keyPair: KeyPair?
        val consentKey: String
        val version: Int
    }

    interface CreateUserConsent : UsecaseFactory<CreateUserConsentParameter, UserConsent>

    interface RedactSensitiveInformation : UsecaseFactory<List<FhirResource>, List<FhirResource>> {
        companion object {
            const val REDACTED = "REDACTED"
        }
    }
}
