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

import care.data4life.datadonation.ConsentDataContract.ConsentDocument
import care.data4life.datadonation.ConsentDataContract.UserConsent
import care.data4life.hl7.fhir.stu3.model.FhirResource

internal interface UsecaseContract {
    interface Usecase<Parameter : Any, ReturnType : Any> {
        suspend fun execute(parameter: Parameter): ReturnType
    }

    interface FetchUserConsents : Usecase<FetchUserConsents.Parameter, List<UserConsent>> {
        interface Parameter {
            val consentDocumentKey: String?
        }
    }

    interface FetchConsentDocuments : Usecase<FetchConsentDocuments.Parameter, List<ConsentDocument>> {
        interface Parameter {
            val version: Int?
            val language: String?
            val consentDocumentKey: String
        }
    }

    interface RevokeUserConsent : Usecase<RevokeUserConsent.Parameter, Unit> {
        interface Parameter {
            val consentDocumentKey: String
        }
    }

    interface CreateUserConsent : Usecase<CreateUserConsent.Parameter, UserConsent> {
        interface Parameter {
            val consentDocumentKey: String
            val version: Int
        }
    }

    interface RedactSensitiveInformation : Usecase<List<FhirResource>, List<FhirResource>> {
        companion object {
            const val REDACTED = "REDACTED"
        }
    }
}
