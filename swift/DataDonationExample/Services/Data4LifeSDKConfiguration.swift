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

import Foundation
import Data4LifeSDK

struct Data4LifeSDKConfiguration {
    
    private struct Key {
        static let environment: String = "D4LEnvironment"
        static let identifier: String = "D4LIdentifier"
        static let secret: String = "D4LSecret"
        static let redirectScheme: String = "D4LRedirectScheme"
    }
    
    private enum EnvironmentValue: String, CaseIterable {
        case development = "DEVELOPMENT"
        case sandbox = "SANDBOX"
        case staging = "STAGING"
        case production = "PRODUCTION"
    }
    
    private let bundle: Foundation.Bundle
    
    init(bundle: Foundation.Bundle = Bundle.main) {
        self.bundle = bundle
    }
}

extension Data4LifeSDKConfiguration {
    var environment: Data4LifeSDK.Environment {
        let stringEnvironment: String = infoPlistValue(for: Key.environment)
        switch stringEnvironment {
        case EnvironmentValue.development.rawValue:
            return .development
        case EnvironmentValue.sandbox.rawValue:
            return .sandbox
        case EnvironmentValue.staging.rawValue:
            return .staging
        case EnvironmentValue.production.rawValue:
            return .production
        default:
            fatalError("Environment can only be set to: \(EnvironmentValue.allCases.map { $0.rawValue }.joined(separator: ", "))")
        }
    }

    var clientIdentifier: String {
        infoPlistValue(for: Key.identifier)
    }

    var clientSecret: String {
        infoPlistValue(for: Key.secret)
    }

    var redirectSchemeUrlString: String {
        infoPlistValue(for: Key.redirectScheme)
    }
}

private extension Data4LifeSDKConfiguration {
    func infoPlistValue<T>(for key: String) -> T {
        guard let value = bundle.object(forInfoDictionaryKey: key) as? T else {
            fatalError("Scheme not set")
        }

        return value
    }
}
