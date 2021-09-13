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
import Data4LifeSDK
import Data4LifeDataDonationSDK

final class Data4LifeSDKService {

    final class D4LTokenProvider: UserSessionTokenProviderProtocol {

        private let client: Data4LifeClient
        init(client: Data4LifeClient) {
            self.client = client
        }

        func getUserSessionToken() -> Data4LifeDataDonationSDK.ResultProtocol<NSString, KotlinThrowable> {
            let semaphore = DispatchSemaphore(value: 0)
            var session: Data4LifeDataDonationSDK.ResultProtocol<NSString, KotlinThrowable> = Data4LifeDataDonationSDK.ResultError<NSString, KotlinThrowable>.init(
                value: KotlinException(message: "Internal error - run before exceution for session token.")
            );
            
            client.refreshedAccessToken { result in
                switch result {
                case .success(.some(let token)):
                    session = Data4LifeDataDonationSDK.ResultSuccess<NSString, KotlinThrowable>.init(
                        value: token as NSString
                    );
                    semaphore.signal()
                case .success(.none):
                    session = Data4LifeDataDonationSDK.ResultError<NSString, KotlinThrowable>.init(
                        value: KotlinException(message: "No token available")
                    );
                    semaphore.signal()
                case .failure(let error):
                    session = Data4LifeDataDonationSDK.ResultError<NSString, KotlinThrowable>.init(
                        value: KotlinException(message: error.localizedDescription)
                    );
                    semaphore.signal()
                }
            };
            
            semaphore.wait()
            return session;
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

    func openLogin(
        from viewController: UIViewController,
        didLogin: @escaping (Result<Void, Error>) -> Void
    ) {
        client.presentLogin(
            on: viewController,
            animated: true
        ) { result in didLogin(result) }
    }

    var userSessionProvider: UserSessionTokenProviderProtocol {
        D4LTokenProvider(client: client)
    }

    func logOut(didLogout: @escaping ()->Void) {
        client.logout { result in
            didLogout()
        }
    }
}
