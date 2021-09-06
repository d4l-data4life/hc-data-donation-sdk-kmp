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

class DataDonationKeychainStoreTests: XCTestCase {

    private lazy var keychainStore = DataDonationKeychainStore(keyHolder: keyHolderMock)
    private lazy var keyHolderMock = DonorKeyHolderMock()
    private let keyFactory = KeyFixtureFactory()
    
    private let testKeyIdentifier = "data-donation-crypto-objc-test"

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
        let _ = try keychainStore.fetchDonorPrivateKeyAsBase64(with: testKeyIdentifier)
        XCTAssertEqual(keyHolderMock.isFetchCalled, true)
        XCTAssertEqual(keyHolderMock.capturedFetchParameter, testKeyIdentifier)
    }

    func testGetPublicKeyFlow() throws {
        let _ = try keychainStore.fetchDonorPublicKeyAsBase64(with: testKeyIdentifier)
        XCTAssertEqual(keyHolderMock.isFetchCalled, true)
        XCTAssertEqual(keyHolderMock.capturedFetchParameter, testKeyIdentifier)
    }

    func testStoreFlow() throws {
        try keychainStore.storeDonorKeyPairData(Data(), with: testKeyIdentifier)
        XCTAssertEqual(keyHolderMock.isCreateCalled, true)
        XCTAssertEqual(keyHolderMock.capturedCreateParameters?.0, Data())
        XCTAssertEqual(keyHolderMock.capturedCreateParameters?.1, testKeyIdentifier)
    }

    func testDeleteFlow() throws {
        try keychainStore.deleteDonorKeyPair(with: testKeyIdentifier)
        XCTAssertEqual(keyHolderMock.isDeleteCalled, true)
        XCTAssertEqual(keyHolderMock.capturedDeleteParameter, testKeyIdentifier)
    }
}
