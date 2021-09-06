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

class DataDonationKeychainStoreModuleTests: XCTestCase {

    private let dataDonationKeychainStore = DataDonationCryptoObjCFactory.dataDonationKeychainStore
    private let keyFactory = KeyFixtureFactory()
    private let testKeyIdentifier = "data-donation-crypto-objc-test"

    override func tearDown() {
        try? dataDonationKeychainStore.deleteDonorKeyPair(with: testKeyIdentifier)
    }

    func testGenerateKeyPairDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try dataDonationKeychainStore.generateDonorKeyPair(with: testKeyIdentifier))
    }

    func testGetPrivateKeyDoesThrowErrorIfNotGeneratedBefore() throws {
        XCTAssertThrowsError(try dataDonationKeychainStore.fetchDonorPrivateKeyAsBase64(with: testKeyIdentifier), "Private key creation should create errors")
    }

    func testGetPublicKeyDoesThrowErrorIfNotGeneratedBefore() throws {
        XCTAssertThrowsError(try dataDonationKeychainStore.fetchDonorPublicKeyAsBase64(with: testKeyIdentifier), "Private key creation should not create errors")
    }

    func testGetPublicKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        try dataDonationKeychainStore.generateDonorKeyPair(with: testKeyIdentifier)

        // When
        let createdKey = try dataDonationKeychainStore.fetchDonorPublicKeyAsBase64(with: testKeyIdentifier)
        let fetchedKey = try dataDonationKeychainStore.fetchDonorPublicKeyAsBase64(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(createdKey, fetchedKey)
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        try dataDonationKeychainStore.generateDonorKeyPair(with: testKeyIdentifier)
        
        // When
        let createdKey = try dataDonationKeychainStore.fetchDonorPrivateKeyAsBase64(with: testKeyIdentifier)
        let fetchedKey = try dataDonationKeychainStore.fetchDonorPrivateKeyAsBase64(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(createdKey, fetchedKey)
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData

        // Then
        XCTAssertNoThrow(try dataDonationKeychainStore.storeDonorKeyPairData(keyPairData, with: testKeyIdentifier))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {

        // Given
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        // When
        try dataDonationKeychainStore.storeDonorKeyPairData(encodedKeyPairData, with: testKeyIdentifier)
        let storedPublicKey = try dataDonationKeychainStore.fetchDonorPublicKeyAsBase64(with: testKeyIdentifier)
        let storedPrivateKey = try dataDonationKeychainStore.fetchDonorPrivateKeyAsBase64(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(try decodedKeyPair.privateKey.asBase64EncodedString(), storedPrivateKey)
        XCTAssertEqual(try decodedKeyPair.publicKey.asBase64EncodedString(), storedPublicKey)
    }
}
