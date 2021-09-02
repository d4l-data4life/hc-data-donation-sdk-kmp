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

class DataDonationCryptorModuleTests: XCTestCase {

    private static let testProgramName = "data-donation-crypto-objc-test"

    private let donorKeyHolder = DonorKeyHolder()
    private lazy var dataDonationCryptor = DataDonationCryptor(programName: DataDonationCryptorModuleTests.testProgramName,
                                                               donorKeyHolder: donorKeyHolder)

    override func setUpWithError() throws {
        try? donorKeyHolder.deleteKeyPair(for: DataDonationCryptorModuleTests.testProgramName)
        try donorKeyHolder.generateKeyPair(for: DataDonationCryptorModuleTests.testProgramName)
    }
}

extension DataDonationCryptorModuleTests {

    func testEncryptDecrypt() throws {

        // Given
        let plainBody = "Hello23';z/.1@@@üû".data(using: .utf8)!

        // When
        let encryptedData = try dataDonationCryptor.encrypt(plainBody)
        let decryptedBody = try dataDonationCryptor.decrypt(encryptedData)

        // Then
        XCTAssertEqual(plainBody, decryptedBody)
    }
}
