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
@testable import DataDonationCryptoObjC
@_implementationOnly import Data4LifeCrypto

class DonorKeyHolderTests: XCTestCase {

    private let donorKeyHolder = DonorKeyHolder()
    private let keyFactory = KeyFactory()
    private let testProgramName = "data-donation-crypto-objc-test"

    override func tearDownWithError() throws {
        try? donorKeyHolder.deleteKeyPair(for: testProgramName)
    }

    func testGetPrivateKeyDoesNotThrowErrors() throws {

        XCTAssertNoThrow(try donorKeyHolder.privateKey(for: testProgramName), "Private key creation should not create errors")

        XCTAssertNoThrow(try donorKeyHolder.deleteKeyPair(for: testProgramName), "Key pair distruction should not create errors")
    }

    func testGetPublicKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try donorKeyHolder.publicKey(for: testProgramName), "Private key creation should not create errors")

        XCTAssertNoThrow(try donorKeyHolder.deleteKeyPair(for: testProgramName), "Key pair distruction should not create errors")
    }

    func testGetPublicKeyReturnsAlwaysTheCreatedOne() throws {
        let createdKey = try donorKeyHolder.publicKey(for: testProgramName)
        let fetchedKey = try donorKeyHolder.publicKey(for: testProgramName)
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {
        let createdKey = try donorKeyHolder.privateKey(for: testProgramName)
        let fetchedKey = try donorKeyHolder.privateKey(for: testProgramName)
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {
        let keyPairData = keyFactory.keyPairData
        XCTAssertNoThrow(try donorKeyHolder.createKeyPair(from: keyPairData, for: testProgramName))
        XCTAssertNoThrow(try donorKeyHolder.deleteKeyPair(for: testProgramName))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName)
        let storedKeyPair = try donorKeyHolder.fetchKeyPair(for: testProgramName)

        XCTAssertEqual(decodedKeyPair.algorithm.blockMode, storedKeyPair.algorithm.blockMode)
        XCTAssertEqual(decodedKeyPair.algorithm.cipher, storedKeyPair.algorithm.cipher)
        XCTAssertEqual(decodedKeyPair.algorithm.hash, storedKeyPair.algorithm.hash)
        XCTAssertEqual(decodedKeyPair.algorithm.padding, storedKeyPair.algorithm.padding)
        XCTAssertEqual(decodedKeyPair.keySize, storedKeyPair.keySize)
        XCTAssertEqual(try decodedKeyPair.privateKey.asBase64EncodedString(),
                       try storedKeyPair.privateKey.asBase64EncodedString())
        XCTAssertEqual(try decodedKeyPair.publicKey.asBase64EncodedString(),
                       try storedKeyPair.publicKey.asBase64EncodedString())
    }

    func testFetchKeyWhenKeyIsNotGeneratedShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.fetchKeyPair(for: testProgramName),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair(programName: testProgramName))
                             })
    }

    func testFetchKeyWithDifferentTagShouldThrowError() throws {
        _ = try donorKeyHolder.generateKeyPair(for: testProgramName)
        XCTAssertThrowsError(try donorKeyHolder.fetchKeyPair(for: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair(programName: "non-existing"))
                             })
    }

    func testDeleteNonExistingKeyShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.deleteKeyPair(for: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotDeleteKeyPair(programName: "non-existing"))
                             })
    }

    func testCreateKeyFromBadDataShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: Data(), for: testProgramName),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData(programName: testProgramName))
                             })
    }

    func testCreateKeysWithSameDataShouldThrowError() throws {
        let encodedKeyPairData = keyFactory.keyPairData
        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName)
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData(programName: testProgramName))
                             })
    }
}
