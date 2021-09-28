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
    private let testKeyIdentifier = "data-donation-crypto-objc-test"

    override func setUpWithError() throws {
        coreCryptoMock = CoreCryptoServiceMock()
        coreCryptoMock.whenGenerateAsymmetric = { [unowned self] tag in
            let keyPair = keyFactory.keyPair
            try! keyPair.store(tag: tag)
            return .success(keyPair)
        }
    }
    
    override func tearDownWithError() throws {
        try? donorKeyHolder.deleteKeyPair(with: testKeyIdentifier)
    }
}

extension DonorKeyHolderTests {

    func testGetPrivateKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try donorKeyHolder.privateKey(with: testKeyIdentifier), "Private key creation should not create errors")
    }

    func testGetPrivateKeyFlow() throws {

        // When
        _ = try donorKeyHolder.privateKey(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(coreCryptoMock.isGenerateAsymmetricCalled, true)
        XCTAssertEqual(coreCryptoMock.capturedGenerateAsymmetricParameters, ("care.data4life.datadonation.donor.keypair.\(testKeyIdentifier)"))
    }

    func testGetPrivateKeyErrorIsMapped() throws {

        // Given
        coreCryptoMock.whenGenerateAsymmetric = { _ in
            .failure(MockError.random)
        }

        // Then
        XCTAssertThrowsError(try donorKeyHolder.privateKey(with: testKeyIdentifier),
                             "should propagate error") { error in
            XCTAssertEqual(error as? DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotGenerateKeyPair)
        }
    }

    func testGetPublicKeyDoesNotThrowErrors() throws {
        XCTAssertNoThrow(try donorKeyHolder.publicKey(with: testKeyIdentifier), "Private key creation should not create errors")
    }

    func testGetPublicKeyFlow() throws {

        // When
        _ = try donorKeyHolder.publicKey(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(coreCryptoMock.isGenerateAsymmetricCalled, true)
        XCTAssertEqual(coreCryptoMock.capturedGenerateAsymmetricParameters, ("care.data4life.datadonation.donor.keypair.\(testKeyIdentifier)"))
    }

    func testGetPublicKeyErrorIsMapped() throws {

        // Given
        coreCryptoMock.whenGenerateAsymmetric = { _ in
            .failure(MockError.random)
        }

        // Then
        XCTAssertThrowsError(try donorKeyHolder.publicKey(with: testKeyIdentifier),
                             "should propagate error") { error in
            XCTAssertEqual(error as? DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotGenerateKeyPair)
        }
    }

    func testGetPublicKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        let createdKey = try donorKeyHolder.publicKey(with: testKeyIdentifier)
        let fetchedKey = try donorKeyHolder.publicKey(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testGetPrivateKeyReturnsAlwaysTheCreatedOne() throws {

        // Given
        let createdKey = try donorKeyHolder.privateKey(with: testKeyIdentifier)
        let fetchedKey = try donorKeyHolder.privateKey(with: testKeyIdentifier)

        // Then
        XCTAssertEqual(try createdKey.asBase64EncodedString(), try fetchedKey.asBase64EncodedString())
    }

    func testStoreKeyPairFromDataDoesNotThrowErrors() throws {

        // Given
        let keyPairData = keyFactory.keyPairData

        // Then
        XCTAssertNoThrow(try donorKeyHolder.createKeyPair(from: keyPairData, with: testKeyIdentifier))
    }

    func testStoredKeyPairFromDataIsTheSameFetched() throws {

        // Given
        let encodedKeyPairData = keyFactory.keyPairData
        let decodedKeyPair = keyFactory.keyPair

        // When
        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, with: testKeyIdentifier)
        let storedKeyPair = try donorKeyHolder.fetchKeyPair(with: testKeyIdentifier)

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
        XCTAssertThrowsError(try donorKeyHolder.fetchKeyPair(with: testKeyIdentifier),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair)
                             })
    }

    func testFetchKeyWithDifferentTagShouldThrowError() throws {
        _ = try donorKeyHolder.generateKeyPair(with: testKeyIdentifier)
        XCTAssertThrowsError(try donorKeyHolder.fetchKeyPair(with: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotFetchKeyPair)
                             })
    }

    func testDeleteNonExistingKeyShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.deleteKeyPair(with: "non-existing"),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotDeleteKeyPair)
                             })
    }

    func testCreateKeyFromBadDataShouldThrowError() throws {
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: Data(), with: testKeyIdentifier),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData)
                             })
    }

    func testCreateKeysWithSameDataShouldThrowError() throws {
        let encodedKeyPairData = keyFactory.keyPairData
        try donorKeyHolder.createKeyPair(from: encodedKeyPairData, with: testKeyIdentifier)
        XCTAssertThrowsError(try donorKeyHolder.createKeyPair(from: encodedKeyPairData, with: testKeyIdentifier),
                             "should throw the right error", { error in
                                XCTAssertEqual(error as! DataDonationCryptoObjCError, DataDonationCryptoObjCError.couldNotCreateKeyPairFromData)
                             })
    }
}
