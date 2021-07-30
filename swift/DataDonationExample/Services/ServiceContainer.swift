//
//  ServiceContainer.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 29.07.21.
//

import Foundation

final class ServiceContainer {

    let sdkService: Data4LifeSDKService

    init() {
        self.sdkService = Data4LifeSDKService()
    }

    func configureServices(d4LServiceConfiguration: Data4LifeSDKConfiguration) {
        sdkService.configure(with: d4LServiceConfiguration)
    }
}
