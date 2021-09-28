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

final class KeyFixtureFactory {

    private let bundle: Foundation.Bundle

    init(bundle: Foundation.Bundle = Bundle.current) {
        self.bundle = bundle
    }

    lazy var keyPairData: Data = {
        try! bundle.data(fromJSON: "asymmetricDataDonationKeyPair")
    }()

    lazy var keyPair: KeyPair = {
        try! bundle.decodable(fromJSON: "asymmetricDataDonationKeyPair")
    }()

    var privateKey: AsymmetricKey {
        keyPair.privateKey
    }

    var publicKey: AsymmetricKey {
        keyPair.publicKey
    }

    var symmetricKey: Key {
        let data = bundle.data(forResource: "symmetricKey", withExtension: "txt")!
        let dataString = String(data: data, encoding: .utf8)!.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        let keyData = Data(base64Encoded: dataString)!
        return try! Key(data: keyData, type: .common)
    }

    var iv: Data {
        let data = bundle.data(forResource: "iv", withExtension: "txt")!
        let dataString = String(data: data, encoding: .utf8)!.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        return Data(base64Encoded: dataString)!
    }
}
