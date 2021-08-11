//
//  Data4LifeSDKService.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import Data4LifeSDK
import Data4LifeDataDonationSDK

final class Data4LifeSDKService {

    final class D4LTokenProvider: UserSessionTokenProvider {

        private let client: Data4LifeClient
        init(client: Data4LifeClient) {
            self.client = client
        }

        func getUserSessionToken(onSuccess: @escaping (String) -> Void, onError: @escaping (KotlinException) -> Void) {
            client.refreshedAccessToken { result in
                switch result {
                case .success(.some(let token)):
                    onSuccess(token)
                case .success(.none):
                    onError(KotlinException(message: "No token available"))
                case .failure(let error):
                    onError(KotlinException(message: error.localizedDescription))
                }
            }
        }
    }

    private var client: Data4LifeClient!

    init() { }
    
    func configure(with configuration: Data4LifeSDKConfiguration) {
        Data4LifeClient.configureWith(clientId: configuration.clientIdentifier,
                                      clientSecret: configuration.clientSecret,
                                      redirectURLString: configuration.redirectSchemeUrlString + "://oauth/",
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

    func handleUrl(_ url: URL) {
        client.handle(url: url)
    }

    func openLogin(from viewController: UIViewController, didLogin: @escaping (Result<Void, Error>) -> Void) {
        client.presentLogin(on: viewController, animated: true) { result in
            didLogin(result)
        }
    }

    var userSessionProvider: UserSessionTokenProvider {
        D4LTokenProvider(client: client)
    }

    func logOut(didLogout: @escaping ()->Void) {
        client.logout { result in
            didLogout()
        }
    }
}
