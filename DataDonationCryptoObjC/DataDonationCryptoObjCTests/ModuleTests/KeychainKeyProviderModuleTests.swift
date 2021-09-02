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
import DataDonationCryptoObjC
import Data4LifeCrypto

class KeychainKeyProviderModuleTests: XCTestCase {

    private let keychainKeyProvider = DataDonationCryptoObjCFactory.keychainKeyProvider
    private let keyFactory = KeyFixtureFactory()
    private let testProgramName = "data-donation-crypto-objc-test"

    override func tearDown() {
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

        // When
        let createdKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        let fetchedKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)

        // Then
        XCTAssertEqual(createdKey, fetchedKey)
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {

        // When
        let createdKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        let fetchedKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)

        // Then
        XCTAssertEqual(createdKey, fetchedKey)
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData

        // Then
        XCTAssertNoThrow(try keychainKeyProvider.storeDonorKeyPairData(keyPairData, for: testProgramName))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {

        // Given
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        // When
        try keychainKeyProvider.storeDonorKeyPairData(encodedKeyPairData, for: testProgramName)
        let storedPublicKey = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        let storedPrivateKey = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)

        // Then 
        XCTAssertEqual(try decodedKeyPair.privateKey.asBase64EncodedString(), storedPrivateKey)
        XCTAssertEqual(try decodedKeyPair.publicKey.asBase64EncodedString(), storedPublicKey)
    }
}
