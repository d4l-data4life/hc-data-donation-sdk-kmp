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
import Data4LifeCrypto

@objc public final class KeychainKeyProvider: NSObject, KeychainKeyProviderProtocol {

    private let keyHolder: DonorKeyHolderProtocol

    @objc public override init() {
        self.keyHolder = DonorKeyHolder()
        super.init()
    }

    init(keyHolder: DonorKeyHolderProtocol = DonorKeyHolder()) {
        self.keyHolder = keyHolder
        super.init()
    }
    
    @objc public func getDonorPrivateKey(for programName: String) throws -> String {
        return try keyHolder.privateKey(for: programName).asBase64EncodedString()
    }

    @objc public func getDonorPublicKey(for programName: String) throws -> String {
        return try keyHolder.publicKey(for: programName).asBase64EncodedString()
    }

    @objc public func removeDonorKeyPair(for programName: String) throws {
        try keyHolder.deleteKeyPair(for: programName)
    }

    @objc public func storeDonorKeyPairData(_ data: Data, for programName: String) throws {
        try keyHolder.createKeyPair(from: data, for: programName)
    }
}
