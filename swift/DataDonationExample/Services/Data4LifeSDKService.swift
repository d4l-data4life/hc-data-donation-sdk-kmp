//
//  Data4LifeSDKService.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import Data4LifeSDK

final class Data4LifeSDKService {
    private var client: Data4LifeClient!

    init() { }
    
    func configure(with configuration: Data4LifeSDKConfiguration) {
        Data4LifeClient.configureWith(clientId: configuration.clientIdentifier,
                                      clientSecret: configuration.clientSecret,
                                      redirectURLString: configuration.redirectSchemeUrlString,
                                      environment: configuration.environment)
        self.client = Data4LifeClient.default
    }

    func isUserLoggedIn(_ didFetchStatus: @escaping (Bool) -> Void) {
        client.isUserLoggedIn { result in
            switch result {
            case .success:
                didFetchStatus(true)
            case .failure:
                didFetchStatus(false)
            }
        }
    }

    func openLogin(from viewController: UIViewController, didLogin: @escaping (Result<Void, Error>) -> Void) {
        client.presentLogin(on: viewController, animated: true) { result in
            didLogin(result)
        }
    }
}
