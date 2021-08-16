//
//  ServiceContainer.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 29.07.21.
//

import Foundation
import Data4LifeDataDonationSDK

final class ServiceContainer {

    private(set) var sdkService: Data4LifeSDKService?
    private(set) var userSessionProvider: UserSessionTokenProviderProtocol?
    private(set) var dataDonationService: DataDonationSDKService?

    init() { }

    func configureServices(d4LServiceConfiguration: Data4LifeSDKConfiguration) {
        sdkService = Data4LifeSDKService()
        sdkService?.configure(with: d4LServiceConfiguration)

        userSessionProvider = sdkService?.userSessionProvider

        dataDonationService = DataDonationSDKService(userSessionProvider: userSessionProvider!)
    }
}
