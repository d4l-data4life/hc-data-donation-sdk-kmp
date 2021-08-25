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
@testable import DataDonationCryptoObjC
import Data4LifeCrypto

enum MockDefaultError: Error {
    case resultNotDefined
}

final class DonorKeyHolderMock: DonorKeyHolderProtocol {

    var isDeleteCalled = false
    var deleteArgument: String?
    var deleteError: Error?
    func deleteKeyPair(for programName: String) throws {
        isDeleteCalled = true
        deleteArgument = programName
        if let error = deleteError {
            throw error
        }
    }

    var isCreateCalled = false
    var createArguments: (Data, String)?
    var createError: Error?
    func createKeyPair(from data: Data, for programName: String) throws {
        isCreateCalled = true
        createArguments = (data, programName)
        if let error = createError {
            throw error
        }
    }


    var isPrivateCalled = false
    var privateArgument: String?
    var privateResult: AsymmetricKey?
    var privateError: Error?
    func privateKey(for programName: String) throws -> AsymmetricKey {
        isPrivateCalled = true
        privateArgument = programName
        if let error = privateError {
            throw error
        } else if let result = privateResult {
            return result
        } else {
            throw MockDefaultError.resultNotDefined
        }
    }

    var isPublicCalled = false
    var publicArgument: String?
    var publicResult: AsymmetricKey?
    var publicError: Error?
    func publicKey(for programName: String) throws -> AsymmetricKey {
        isPublicCalled = true
        publicArgument = programName
        if let error = publicError {
            throw error
        } else if let result = publicResult {
            return result
        } else {
            throw MockDefaultError.resultNotDefined
        }
    }
}
