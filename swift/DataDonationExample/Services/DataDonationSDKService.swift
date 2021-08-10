//
//  DataDonationSDKService.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 29.07.21.
//

import Foundation
import Data4LifeDataDonationSDK

final class DataDonationSDKService {

    private let userSessionProvider: UserSessionTokenProvider
    private let client: DataDonationClient!
    init(userSessionProvider: UserSessionTokenProvider) {
        self.userSessionProvider = userSessionProvider
        self.client = Client.Factory().getInstance(environment: .staging, userSession: userSessionProvider)
    }

    private var currentJob: KotlinJob?
}

extension DataDonationSDKService {

    private static var testKey = "test-key"

    func fetchUserConsents() {
        let flow = client.fetchAllUserConsents()
        currentJob = flow.subscribe { consents in
            print(consents)
        } onError: { error in
            print(error)
        } onComplete: {
            print("completed")
        }.asStruct
    }

    func createUserConsent(key: String = DataDonationSDKService.testKey) {
        let flow = client.createUserConsent(consentDocumentKey: key, consentDocumentVersion: "1.0.0")
        currentJob = flow.subscribe { consent in
            print(consent)
        } onError: { error in
            print(error)
        } onComplete: {
            print("completed")
        }.asStruct
    }

    func revokeUserConsent(key: String = DataDonationSDKService.testKey) {
        let flow = client.revokeUserConsent(consentDocumentKey: key)
        currentJob = flow.subscribe(onEach: { unit in
            print("successfully revoked")
        }, onError: { error in
            print(error)
        }, onComplete: {
            print("completed")
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
