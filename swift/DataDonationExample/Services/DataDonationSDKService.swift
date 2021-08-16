//
//  DataDonationSDKService.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 29.07.21.
//

import Foundation
import Data4LifeDataDonationSDK

final class DataDonationSDKService {

    private let userSessionProvider: UserSessionTokenProviderProtocol
    private let client: DataDonationClientProtocol!
    init(userSessionProvider: UserSessionTokenProviderProtocol) {
        self.userSessionProvider = userSessionProvider
        self.client = Client.Factory().getInstance(environment: .staging, userSession: userSessionProvider)
    }

    private var currentJob: KotlinJob?
}

extension DataDonationSDKService {

    private static var testKey = "d4l.ecov"

    func fetchUserConsents(_ completion: @escaping (Result<[UserConsent], Error>) -> Void) {
        let flow = client.fetchAllUserConsents()
        currentJob = flow.subscribe { consents in
            let consents = consents.array(of: UserConsent.self)
            completion(.success(consents))
        } onError: { error in
            completion(.failure(error))
        } onComplete: {
            print("fetch completed")
        }.asStruct
    }

    func createUserConsent(key: String = DataDonationSDKService.testKey, completion: @escaping (Result<UserConsentProtocol, Error>) -> Void) {
        let flow = client.createUserConsent(consentDocumentKey: key, consentDocumentVersion: "1.0.0")
        currentJob = flow.subscribe { consent in
            completion(.success(consent))
        } onError: { error in
            completion(.failure(error))
        } onComplete: {
            print("fetch completed")
        }.asStruct
    }

    func revokeUserConsent(key: String = DataDonationSDKService.testKey, completion: @escaping (Result<Void, Error>) -> Void)  {
        let flow = client.revokeUserConsent(consentDocumentKey: key)
        currentJob = flow.subscribe(onEach: { unit in
            completion(.success(()))
        }, onError: { error in
            completion(.failure(error))
        }, onComplete: {
            print("fetch completed")
        }).asStruct
    }

    func cancelCurrentJob() {
        currentJob?.cancel()
    }
}


struct KotlinJob {
    private let coreJob: Kotlinx_coroutines_coreJob
    init(coreJob: Kotlinx_coroutines_coreJob) {
        self.coreJob = coreJob
    }

    func cancel() {
        coreJob.cancel(cause: nil)
    }
}

extension Kotlinx_coroutines_coreJob {
    var asStruct: KotlinJob {
        KotlinJob(coreJob: self)
    }
}

extension NSArray {
    func array<T>(of type: T.Type = T.self) -> [T] {
        guard let array = Array(self) as? [T] else {
            fatalError("Cant convert NSArray into [\(type)]")
        }
        return array
    }
}
