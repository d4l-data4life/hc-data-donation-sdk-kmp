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

    private var keychainKeyProvider: KeychainKeyProvider!
    private var keyHolderMock: DonorKeyHolderMock!

    private let keyFactory = KeyFactory()
    private let testProgramName = "data-donation-crypto-objc-test"

    override func setUpWithError() throws {
        keyHolderMock = DonorKeyHolderMock()
        keyHolderMock.privateResult = keyFactory.privateKey
        keyHolderMock.publicResult = keyFactory.publicKey

        keychainKeyProvider = KeychainKeyProvider(keyHolder: keyHolderMock)
    }

    func testGetPrivateKeyFlow() throws {
        let _ = try keychainKeyProvider.getDonorPrivateKey(for: testProgramName)
        XCTAssertEqual(keyHolderMock.isPrivateCalled, true)
        XCTAssertEqual(keyHolderMock.capturedPrivateParameter, testProgramName)
    }

    func testGetPublicKeyFlow() throws {
        let _ = try keychainKeyProvider.getDonorPublicKey(for: testProgramName)
        XCTAssertEqual(keyHolderMock.isPublicCalled, true)
        XCTAssertEqual(keyHolderMock.capturedPublicParameter, testProgramName)
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
