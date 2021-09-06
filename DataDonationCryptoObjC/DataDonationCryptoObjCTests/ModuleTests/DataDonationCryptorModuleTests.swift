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

import XCTest
@testable import DataDonationCryptoObjC
import Data4LifeCrypto

class DataDonationCryptorModuleTests: XCTestCase {

    private static let testKeyIdentifier = "data-donation-crypto-objc-test"

    private let dataDonationCryptor = DataDonationCryptor()
    private let keyFactory = KeyFixtureFactory()
}

extension DataDonationCryptorModuleTests {

    func testEncryptDecrypt() throws {

        // Given
        let plainBody = "Hello23';z/.1@@@üû".data(using: .utf8)!
        let base64EncodedPublicKey = try! keyFactory.publicKey.asBase64EncodedString()
        let base64EncodedPrivateKey = try! keyFactory.privateKey.asBase64EncodedString()

        // When
        let encryptedData = try dataDonationCryptor.encrypt(plainBody, base64EncodedPublicKey: base64EncodedPublicKey)
        let decryptedBody = try dataDonationCryptor.decrypt(encryptedData, base64EncodedPrivateKey: base64EncodedPrivateKey)

        // Then
        XCTAssertEqual(plainBody, decryptedBody)
    }
}
