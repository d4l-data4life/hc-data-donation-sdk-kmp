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

enum BundleError: Error {
    case missingResource
}

extension Bundle {
    static var current: Bundle {
        #if SWIFT_PACKAGE
        return Bundle.module
        #else
        return Bundle(for: KeychainKeyProviderTests.self)
        #endif
    }

    func data(forResource named: String, withExtension type: String) -> Data? {
        if let url = self.url(forResource: named, withExtension: type) {
            return try? Data(contentsOf: url)
        } else {
            return nil
        }
    }

    private func decodable<T: Decodable>(fromURL url: URL) throws -> T {
        let data = try Data(contentsOf: url)
        return try JSONDecoder().decode(T.self, from: data)
    }

    func decodable<T: Decodable>(fromJSON named: String) throws -> T {
        guard let url = self.url(forResource: named, withExtension: "json") else {
            throw BundleError.missingResource
        }

        return try decodable(fromURL: url)
    }

    func data(fromJSON named: String) throws -> Data {
        guard let url = self.url(forResource: named, withExtension: "json") else {
            throw BundleError.missingResource
        }
        return try Data(contentsOf: url)
    }

    func json(named: String) throws -> [String: Any]? {
        guard let url = self.url(forResource: named, withExtension: "json") else {
            throw BundleError.missingResource
        }

        let data = try Data(contentsOf: url)
        return try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any]
    }

    func jsonFiles(fromDirectories dirs: [String]) -> [URL] {
        return dirs.flatMap { return self.urls(forResourcesWithExtension: "json", subdirectory: $0) ?? [] }
    }
}
