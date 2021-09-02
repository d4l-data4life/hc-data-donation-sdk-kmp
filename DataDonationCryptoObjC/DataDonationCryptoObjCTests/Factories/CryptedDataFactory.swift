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
@testable import DataDonationCryptoObjC

final class CryptedDataFactory {

    private let bundle: Foundation.Bundle

    init(bundle: Foundation.Bundle = Bundle.current) {
        self.bundle = bundle
    }

    var plainInputData: Data {
        let encodedData = bundle.data(forResource: "plainDataInput", withExtension: "txt")!
        let encodedDataString = String(data: encodedData, encoding: .utf8)?.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        return encodedDataString!.data(using: .utf8)!
    }

    var encryptedOutputData: Data {
        let encodedData = bundle.data(forResource: "encryptedDataOutput", withExtension: "txt")!
        let encodedDataString = String(data: encodedData, encoding: .utf8)?.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        return Data(base64Encoded: encodedDataString!)!
    }

    var encryptedSymmetricKey: Data {
        EncryptedData(combined: encryptedOutputData).encryptedKey
    }
}
