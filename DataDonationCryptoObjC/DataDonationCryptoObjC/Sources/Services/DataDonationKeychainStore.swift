//  Copyright (c) 2021 D4L data4life gGmbH
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

final class DataDonationKeychainStore {

    private let keyHolder: DonorKeyHolderProtocol

    init(keyHolder: DonorKeyHolderProtocol = DonorKeyHolder()) {
        self.keyHolder = keyHolder
    }
}

extension DataDonationKeychainStore: DataDonationKeychainStoreObjCProtocol {
    func generateDonorKeyPair(with keyIdentifier: String) throws {
        try keyHolder.generateKeyPair(with: keyIdentifier)
    }

    func storeDonorKeyPairData(_ data: Data, with keyIdentifier: String) throws {
        try keyHolder.createKeyPair(from: data, with: keyIdentifier)
    }

    func fetchKeyPairAsBase64(with keyIdentifier: String) throws -> ObjCKeyPair {
        try keyHolder.fetchKeyPair(with: keyIdentifier).objCKeyPair
    }

    func deleteDonorKeyPair(with keyIdentifier: String) throws {
        try keyHolder.deleteKeyPair(with: keyIdentifier)
    }

    func updateDonorKeyPair(_ data: Data, with keyIdentifier: String) throws {
        try keyHolder.deleteKeyPair(with: keyIdentifier)
        try keyHolder.createKeyPair(from: data, with: keyIdentifier)
    }

    func hasSameKeypairStored(as keyPair: ObjCKeyPair, with keyIdentifier: String) -> Bool {
        do {
            let storedKeyPair = try fetchKeyPairAsBase64(with: keyIdentifier)
            return storedKeyPair == keyPair
        } catch {
            return false
        }
    }
}
