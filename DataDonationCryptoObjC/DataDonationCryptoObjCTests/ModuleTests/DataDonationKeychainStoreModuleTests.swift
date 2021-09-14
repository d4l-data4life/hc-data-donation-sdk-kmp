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
        let storedKeyPair = try dataDonationKeychainStore.fetchKeyPairAsBase64(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(decodedKeyPair.objCKeyPair, storedKeyPair)
    }

    func testFetchKeypairDoesThrowErrorIfNotPresentBefore() throws {
        XCTAssertThrowsError(try dataDonationKeychainStore.fetchKeyPairAsBase64(with: testKeyIdentifier))
    }

    func testFetchKeypairReturnsAlwaysTheCreatedOne() throws {

        // Given
        try dataDonationKeychainStore.generateDonorKeyPair(with: testKeyIdentifier)

        // When
        let createdKey = try dataDonationKeychainStore.fetchKeyPairAsBase64(with: testKeyIdentifier)
        let fetchedKey = try dataDonationKeychainStore.fetchKeyPairAsBase64(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(createdKey, fetchedKey)
    }

    func testUpdateKeyPairFromDataDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData
        try dataDonationKeychainStore.storeDonorKeyPairData(keyPairData, with: testKeyIdentifier)

        // Then
        XCTAssertNoThrow(try dataDonationKeychainStore.updateDonorKeyPair(keyPairData, with: testKeyIdentifier))
    }

    func testUpdateKeyPairFromDataThrowErrorIfNotPresentBefore() throws {

        // Given
        let keyPairData = keyFactory.keyPairData

        // Then
        XCTAssertThrowsError(try dataDonationKeychainStore.updateDonorKeyPair(keyPairData, with: testKeyIdentifier))
    }

    func testDeleteKeyPairDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData
        try dataDonationKeychainStore.storeDonorKeyPairData(keyPairData, with: testKeyIdentifier)

        // Then
        XCTAssertNoThrow(try dataDonationKeychainStore.deleteDonorKeyPair(with: testKeyIdentifier))
    }

    func testDeleteKeyPairThrowsErrorIfNotPresentBefore() throws {

        // Then
        XCTAssertThrowsError(try dataDonationKeychainStore.deleteDonorKeyPair(with: testKeyIdentifier))
    }

    func testHasSameKeyPairStoredReturnsTrueWhenTestingSameKeyPair() throws {
        
        // Given
        let keyPairData = keyFactory.keyPairData
        try dataDonationKeychainStore.storeDonorKeyPairData(keyPairData, with: testKeyIdentifier)
        let objcKeyPair = keyFactory.keyPair.objCKeyPair

        // Then
        XCTAssertEqual(dataDonationKeychainStore.hasSameKeypairStored(as: objcKeyPair, with: testKeyIdentifier), true)
    }

    func testHasSameKeyPairStoredReturnsFalseWhenTestingDifferentKeyPair() throws {

        // Given
        let keyPairData = keyFactory.keyPairData
        try dataDonationKeychainStore.storeDonorKeyPairData(keyPairData, with: testKeyIdentifier)
        let objcKeyPair = ObjCKeyPair(privateKey: "another", publicKey: "keypair")

        // Then
        XCTAssertEqual(dataDonationKeychainStore.hasSameKeypairStored(as: objcKeyPair, with: testKeyIdentifier), false)
    }
}
