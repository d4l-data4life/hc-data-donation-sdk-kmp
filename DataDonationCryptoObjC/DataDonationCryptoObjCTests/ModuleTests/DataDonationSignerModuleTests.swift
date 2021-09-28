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

final class DataDonationSignerModuleTests: XCTestCase {

    private static let testKeyIdentifier = "data-donation-crypto-objc-test"

    private let dataDonationSigner = DataDonationCryptoObjCFactory.dataDonationSigner
    private let keyFactory = KeyFixtureFactory()
    private let donorKeyHolder = DonorKeyHolder()
}

extension DataDonationSignerModuleTests {

    override func setUpWithError() throws {
        try donorKeyHolder.generateKeyPair(with: DataDonationSignerModuleTests.testKeyIdentifier)
    }

    override func tearDownWithError() throws {
        try? donorKeyHolder.deleteKeyPair(with: DataDonationSignerModuleTests.testKeyIdentifier)
    }

    func testSignAndVerifyWithSalted() throws {

        // Given
        let data = "Hello World!".data(using: .utf8)!

        // When
        let signature = try dataDonationSigner.sign(data: data,
                                                    isSalted: true,
                                                    donorKeyIdentifier: DataDonationSignerModuleTests.testKeyIdentifier)

        // Then
        XCTAssertNoThrow(try dataDonationSigner.verify(data: data,
                                                       signature: signature,
                                                       isSalted: true,
                                                       donorKeyIdentifier: DataDonationSignerModuleTests.testKeyIdentifier))
    }

    func testSignAndVerifyWithUnsalted() throws {

        // Given
        let data = "Hello World!".data(using: .utf8)!

        // When
        let signature = try dataDonationSigner.sign(data: data,
                                                    isSalted: false,
                                                    donorKeyIdentifier: DataDonationSignerModuleTests.testKeyIdentifier)

        // Then
        XCTAssertNoThrow(try dataDonationSigner.verify(data: data,
                                                       signature: signature,
                                                       isSalted: false,
                                                       donorKeyIdentifier: DataDonationSignerModuleTests.testKeyIdentifier))
    }
}
