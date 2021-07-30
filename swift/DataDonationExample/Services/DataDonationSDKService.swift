//
//  DataDonationSDKService.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 29.07.21.
//

import Foundation
import Data4LifeDataDonationSDK

final class DataDonationSDKService {

    private let userSessionProvider: DataDonationSDKPublicAPIUserSessionTokenProvider
    private let client: DataDonationSDKPublicAPIDataDonationClient!

    init(userSessionProvider: DataDonationSDKPublicAPIUserSessionTokenProvider) {
        self.userSessionProvider = userSessionProvider
        self.client = Client.Factory().getInstance(environment: .staging, userSession: userSessionProvider)
    }
}

extension DataDonationSDKService {
    func createUserConsent() {
        let flow = client.createUserConsent(consentDocumentKey: "key", consentDocumentVersion: 1)
    }
}
