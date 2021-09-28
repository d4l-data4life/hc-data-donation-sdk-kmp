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

final class DataDonationSignerTests: XCTestCase {

    private static let testKeyIdentifier = "data-donation-crypto-objc-test"

    private lazy var dataDonationSigner = DataDonationSigner(donorKeyHolder: donorKeyHolderMock,
                                                             coreCryptoService: coreCryptoServiceMock)
    private lazy var donorKeyHolderMock = DonorKeyHolderMock()
    private lazy var coreCryptoServiceMock = CoreCryptoServiceMock()
    private let keyFactory = KeyFixtureFactory()
    private let dataFactory = CryptedDataFactory()
}

extension DataDonationSignerTests {

    override func setUp() {
        coreCryptoServiceMock = CoreCryptoServiceMock()
        coreCryptoServiceMock.whenCreateAsymmetric = { [unowned self] _, type in
            switch type {
            case .private:
                return .success(self.keyFactory.privateKey)
            case .public:
                return .success(self.keyFactory.publicKey)
            @unknown default:
                fatalError("unknown rsa key type detected")
            }
        }

        coreCryptoServiceMock.whenVerify = { data, signature, keyPair, isSalted in
            do {
                let isVerified = try Data4LifeSigner.verify(data: data, against: signature, publicKey: keyPair.publicKey, isSalted: isSalted)
                return .success(isVerified)
            } catch {
                return .failure(error)
            }
        }

        donorKeyHolderMock = DonorKeyHolderMock()
        donorKeyHolderMock.whenFetch = { [unowned self] _ in
            .success(keyFactory.keyPair)
        }
    }

    func testVerifySalted() throws {

        // Given
        let data = dataFactory.signerInputData
        let signature = dataFactory.signatureSaltLength32

        // Then
        XCTAssertNoThrow(try dataDonationSigner.verify(data: data,
                                                       signature: signature,
                                                       isSalted: true,
                                                       donorKeyIdentifier: DataDonationSignerTests.testKeyIdentifier))
    }

    func testVerifyUnsalted() throws {

        // Given
        let data = dataFactory.signerInputData
        let signature = dataFactory.signatureSaltLength0

        // Then
        XCTAssertNoThrow(try dataDonationSigner.verify(data: data,
                                                       signature: signature,
                                                       isSalted: false,
                                                       donorKeyIdentifier: DataDonationSignerTests.testKeyIdentifier))
    }
}
