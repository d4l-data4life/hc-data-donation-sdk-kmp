//  Copyright (c) 2020 D4L data4life gGmbH
//  All rights reserved.
//
//  D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
//  including any intellectual property rights that subsist in the SDK.
//
//  The SDK and its documentation may be accessed and used for viewing/review purposes only.
//  Any usage of the SDK for other purposes, including usage for the development of
//  applications/third-party applications shall require the conclusion of a license agreement
//  between you and D4L.
//
//  If you are interested in licensing the SDK for your own applications/third-party
//  applications and/or if you’d like to contribute to the development of the SDK, please
//  contact D4L by email to help@data4life.care.
//

import Foundation
import Data4LifeDataDonationSDK

final class DataDonationSDKService {

    private let userSessionProvider: UserSessionTokenProviderProtocol
    private let client: DataDonationClientProtocol!
    init(userSessionProvider: UserSessionTokenProviderProtocol) {
        self.userSessionProvider = userSessionProvider
        self.client = Client.Factory().getInstance(
            environment: .staging,
            userSession: userSessionProvider,
            coroutineScope: nil
        )
    }

    private var currentJob: KotlinJob?
}

extension DataDonationSDKService {

    private static var testKey = "d4l.ecov"

    func fetchUserConsents(_ completion: @escaping (Result<[UserConsentProtocol], Error>) -> Void) {
        let flow = client.fetchAllUserConsents()
        currentJob = flow.subscribe { consents in
            let consents = consents.array(of: UserConsentProtocol.self)
            completion(.success(consents))
        } onError: { error in
            completion(.failure(error))
        } onComplete: {
            print("fetch completed")
        }
    }

    func createUserConsent(key: String = DataDonationSDKService.testKey, completion: @escaping (Result<UserConsentProtocol, Error>) -> Void) {
        let flow = client.createUserConsent(consentDocumentKey: key, consentDocumentVersion: "1.0.0")
        currentJob = flow.subscribe { consent in
            completion(.success(consent))
        } onError: { error in
            completion(.failure(error))
        } onComplete: {
            print("fetch completed")
        }
    }

    func revokeUserConsent(key: String = DataDonationSDKService.testKey, completion: @escaping (Result<Void, Error>) -> Void)  {
        let flow = client.revokeUserConsent(consentDocumentKey: key)
        currentJob = flow.subscribe(onEach: { unit in
            completion(.success(()))
        }, onError: { error in
            completion(.failure(error))
        }, onComplete: {
            print("fetch completed")
        })
    }

    func cancelCurrentJob() {
        currentJob?.cancel(cause: KotlinCancellationError(message: "User canceled job"))
    }
}
