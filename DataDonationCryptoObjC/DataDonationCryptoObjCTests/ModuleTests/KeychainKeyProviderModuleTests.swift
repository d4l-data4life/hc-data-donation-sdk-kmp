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

import XCTest
import DataDonationCryptoObjC
import Data4LifeCrypto

class KeychainKeyProviderModuleTests: XCTestCase {

    private let keychainKeyProvider = KeychainKeyProvider()
    private let keyFactory = KeyFactory()
    private let testProgramName = "data-donation-crypto-objc-test"

    override func setUpWithError() throws {
        try? keychainKeyProvider.removeDonorKeyPair(for: testProgramName)
    }

    func testGetPrivateKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try keychainKeyProvider.getDonorPrivateKey(for: testProgramName), "Private key creation should not create errors")

        XCTAssertNoThrow(try keychainKeyProvider.removeDonorKeyPair(for: testProgramName), "Key pair distruction should not create errors")
    }

    func testGetPublicKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try keychainKeyProvider.getDonorPublicKey(for: testProgramName), "Private key creation should not create errors")

        XCTAssertNoThrow(try keychainKeyProvider.removeDonorKeyPair(for: testProgramName), "Key pair distruction should not create errors")
    }

    func testGetPublicKeyReturnsAlwaysTheCreatedOne() throws {
        let createdKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        let fetchedKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        XCTAssertEqual(createdKey, fetchedKey)

        try keychainKeyProvider.removeDonorKeyPair(for: testProgramName)
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {
        let createdKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        let fetchedKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        XCTAssertEqual(createdKey, fetchedKey)
        try keychainKeyProvider.removeDonorKeyPair(for: testProgramName)
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {
        let keyPairData = keyFactory.keyPairData
        XCTAssertNoThrow(try keychainKeyProvider.storeDonorKeyPairData(keyPairData, for: testProgramName))
        XCTAssertNoThrow(try keychainKeyProvider.removeDonorKeyPair(for: testProgramName))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        try keychainKeyProvider.storeDonorKeyPairData(encodedKeyPairData, for: testProgramName)
        let storedPublicKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        let storedPrivateKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        XCTAssertEqual(try decodedKeyPair.privateKey.asBase64EncodedString(), storedPrivateKey)
        XCTAssertEqual(try decodedKeyPair.publicKey.asBase64EncodedString(), storedPublicKey)
        try keychainKeyProvider.removeDonorKeyPair(for: testProgramName)
    }
}
