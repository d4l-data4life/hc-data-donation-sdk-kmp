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

class KeychainKeyProviderTests: XCTestCase {

    private lazy var keychainKeyProvider = KeychainKeyProvider(keyHolder: keyHolderMock)
    private lazy var keyHolderMock = DonorKeyHolderMock()
    private let keyFactory = KeyFixtureFactory()
    
    private let testProgramName = "data-donation-crypto-objc-test"

    override func setUpWithError() throws {
        keyHolderMock.whenFetch = { [unowned self] _ in
            .success(self.keyFactory.keyPair)
        }
        keyHolderMock.whenCreate = { [unowned self] _,_ in
            .success(self.keyFactory.keyPair)
        }
        keyHolderMock.whenDelete = { _ in
            .success(())
        }
    }

    func testGetPrivateKeyFlow() throws {
        let _ = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        XCTAssertEqual(keyHolderMock.isFetchCalled, true)
        XCTAssertEqual(keyHolderMock.capturedFetchParameter, testProgramName)
    }

    func testGetPublicKeyFlow() throws {
        let _ = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        XCTAssertEqual(keyHolderMock.isFetchCalled, true)
        XCTAssertEqual(keyHolderMock.capturedFetchParameter, testProgramName)
    }

    func testStoreFlow() throws {
        try keychainKeyProvider.storeDonorKeyPairData(Data(), for: testProgramName)
        XCTAssertEqual(keyHolderMock.isCreateCalled, true)
        XCTAssertEqual(keyHolderMock.capturedCreateParameters?.0, Data())
        XCTAssertEqual(keyHolderMock.capturedCreateParameters?.1, testProgramName)
    }

    func testDeleteFlow() throws {
        try keychainKeyProvider.removeDonorKeyPair(for: testProgramName)
        XCTAssertEqual(keyHolderMock.isDeleteCalled, true)
        XCTAssertEqual(keyHolderMock.capturedDeleteParameter, testProgramName)
    }
}
