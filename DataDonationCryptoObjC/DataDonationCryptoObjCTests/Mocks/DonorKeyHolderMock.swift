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
@testable import DataDonationCryptoObjC
import Data4LifeCrypto

final class DonorKeyHolderMock: DonorKeyHolderProtocol {

    var isGenerateCalled = false
    var capturedGenerateParameter: String?
    var whenGenerate: ((String) -> Result<KeyPair,Error>)?
    
    func generateKeyPair(with keyIdentifier: String) throws -> KeyPair {
        isGenerateCalled = true
        capturedGenerateParameter = keyIdentifier
        if let result = whenGenerate?(keyIdentifier) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isCreateCalled = false
    var capturedCreateParameters: (Data, String)?
    var whenCreate: ((Data, String) -> Result<KeyPair,Error>)?

    func createKeyPair(from data: Data, with keyIdentifier: String) throws -> KeyPair {
        isCreateCalled = true
        capturedCreateParameters = (data, keyIdentifier)
        if let result = whenCreate?(data, keyIdentifier) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isFetchCalled = false
    var capturedFetchParameter: String?
    var whenFetch: ((String) -> Result<KeyPair,Error>)?

    func fetchKeyPair(with keyIdentifier: String) throws -> KeyPair {
        isFetchCalled = true
        capturedFetchParameter = keyIdentifier
        if let result = whenFetch?(keyIdentifier) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isDeleteCalled = false
    var capturedDeleteParameter: String?
    var whenDelete: ((String) -> Result<Void,Error>)?

    func deleteKeyPair(with keyIdentifier: String) throws {
        isDeleteCalled = true
        capturedDeleteParameter = keyIdentifier
        if let result = whenDelete?(keyIdentifier) {
            try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }
}
