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

class DonorKeyHolderTests: XCTestCase {

    private lazy var coreCryptoMock = CoreCryptoServiceMock()
    private lazy var donorKeyHolder = DonorKeyHolder(coreCryptoService: coreCryptoMock)

    private let keyFactory = KeyFixtureFactory()
    private let testProgramName = "data-donation-crypto-objc-test"

    override func setUpWithError() throws {
        coreCryptoMock = CoreCryptoServiceMock()
        coreCryptoMock.whenGenerateAsymmetric = { [unowned self] tag in
            let keyPair = keyFactory.keyPair
            try! keyPair.store(tag: tag)
            return .success(keyPair)
        }
    }
    
    override func tearDownWithError() throws {
        try? donorKeyHolder.deleteKeyPair(for: testProgramName)
    }
}

extension DonorKeyHolderTests {

    func testGetPrivateKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try donorKeyHolder.privateKey(for: testProgramName), "Private key creation should not create errors")
    }

    func testGetPrivateKeyFlow() throws {

        // When
        _ = try donorKeyHolder.privateKey(for: testProgramName)

        // Then
        XCTAssertEqual(coreCryptoMock.isGenerateAsymmetricCalled, true)
        XCTAssertEqual(coreCryptoMock.capturedGenerateAsymmetricParameters, (donorKeyHolder.tag(for:testProgramName)))
    }

    func testGetPrivateKeyErrorIsMapped() throws {

        // Given
        coreCryptoMock.whenGenerateAsymmetric = { _ in
            .failure(MockError.random)
        }

        // Then
        XCTAssertThrowsError(try donorKeyHolder.privateKey(for: testProgramName),
                             "should propagate error") { error in
            XCTAssertEqual(error as? DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotGenerateKeyPair)
        }
    }

    func testGetPublicKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try donorKeyHolder.publicKey(for: testProgramName), "Private key creation should not create errors")
    }

    func testGetPublicKeyFlow() throws {

        // When
        _ = try donorKeyHolder.publicKey(for: testProgramName)

        // Then
        XCTAssertEqual(coreCryptoMock.isGenerateAsymmetricCalled, true)
        XCTAssertEqual(coreCryptoMock.capturedGenerateAsymmetricParameters, (donorKeyHolder.tag(for:testProgramName)))
    }

    func testGetPublicKeyErrorIsMapped() throws {

        // Given
        coreCryptoMock.whenGenerateAsymmetric = { _ in
            .failure(MockError.random)
        }

        // Then
        XCTAssertThrowsError(try donorKeyHolder.publicKey(for: testProgramName),
                             "should propagate error") { error in
            XCTAssertEqual(error as? DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotGenerateKeyPair)
        }
    }

    func testGetPublicKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        let createdKey = try donorKeyHolder.publicKey(for: testProgramName)
        let fetchedKey = try donorKeyHolder.publicKey(for: testProgramName)

        // Then
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        let createdKey = try donorKeyHolder.privateKey(for: testProgramName)
        let fetchedKey = try donorKeyHolder.privateKey(for: testProgramName)

        // Then
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData

        // Then
        XCTAssertNoThrow(try donorKeyHolder.createKeyPair(from: keyPairData, for: testProgramName))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {

        // Given
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        // When
        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName)
        let storedKeyPair = try donorKeyHolder.fetchKeyPair(for: testProgramName)

        // Then
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
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair)
                             })
    }

    func testFetchKeyWithDifferentTagShouldThrowError() throws {
        _ = try donorKeyHolder.generateKeyPair(for: testProgramName)
        XCTAssertThrowsError(try donorKeyHolder.fetchKeyPair(for: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair)
                             })
    }

    func testDeleteNonExistingKeyShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.deleteKeyPair(for: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotDeleteKeyPair)
                             })
    }

    func testCreateKeyFromBadDataShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: Data(), for: testProgramName),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData)
                             })
    }

    func testCreateKeysWithSameDataShouldThrowError() throws {
        let encodedKeyPairData = keyFactory.keyPairData
        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName)
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: encodedKeyPairData, for: testProgramName),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData)
                             })
    }
}
