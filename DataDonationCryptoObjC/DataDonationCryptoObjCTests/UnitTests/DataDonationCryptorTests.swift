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

final class DataDonationCryptorTests: XCTestCase {

    private let testProgramName = "data-donation-crypto-objc-test"
    private let keyFactory = KeyFixtureFactory()
    private let dataFactory = CryptedDataFactory()

    private var donorKeyHolderMock = DonorKeyHolderMock()
    private var ivGeneratorMock = IVGeneratorMock()
    private var coreCryptoServiceMock = CoreCryptoServiceMock()
    
    private lazy var cryptor: DataDonationCryptor = {
        DataDonationCryptor(programName: testProgramName,
                            donorKeyHolder: donorKeyHolderMock,
                            ivGenerator: ivGeneratorMock,
                            coreCryptoService: coreCryptoServiceMock)
    }()
}

extension DataDonationCryptorTests {

    override func setUp() {
        coreCryptoServiceMock = CoreCryptoServiceMock()
        coreCryptoServiceMock.whenGenerateSymmetric = { [unowned self] in
            .success(self.keyFactory.symmetricKey)
        }
        coreCryptoServiceMock.whenSymmetricEncrypt = { data, key, iv in
            .success(try! Data4LifeCryptor.symEncrypt(key: key, data: data, iv: iv))
        }
        coreCryptoServiceMock.whenSymmetricDecrypt = { data, key, iv in
            .success(try! Data4LifeCryptor.symDecrypt(key: key, data: data, iv: iv))
        }
        coreCryptoServiceMock.whenAsymmetricEncrypt = { data, keyPair in
            .success(try! Data4LifeCryptor.asymEncrypt(key: keyPair, data: data))
        }
        coreCryptoServiceMock.whenAsymmetricDecrypt = { data, keyPair in
            .success(try! Data4LifeCryptor.asymDecrypt(key: keyPair, data: data))
        }

        ivGeneratorMock = IVGeneratorMock()
        ivGeneratorMock.whenRandom = { [unowned self] _ in
            .success(keyFactory.iv)
        }

        donorKeyHolderMock = DonorKeyHolderMock()
        donorKeyHolderMock.whenFetch = { [unowned self] _ in
            .success(keyFactory.keyPair)
        }
    }

    func testEncrypt() throws {

        // Given

        // Necessary because RSA-OAEP returns always different data
        coreCryptoServiceMock.whenAsymmetricEncrypt = { [unowned self] data, keyPair in
            .success(self.dataFactory.encryptedSymmetricKey)
        }

        let plainBody = dataFactory.plainInputData

        // When
        let encryptedData = try cryptor.encrypt(plainBody)

        // Then
        let combinedOne = EncryptedData(combined: encryptedData)
        let combinedTwo = EncryptedData(combined: dataFactory.encryptedOutputData)
        XCTAssertEqual(combinedOne.combined.byteCount, combinedTwo.combined.byteCount)
        XCTAssertEqual(combinedOne.encryptionVersion, combinedTwo.encryptionVersion)
        XCTAssertEqual(combinedOne.encryptedBody, combinedTwo.encryptedBody)
        XCTAssertEqual(combinedOne.iv, combinedTwo.iv)
        XCTAssertEqual(combinedOne.encryptedKey, combinedTwo.encryptedKey)
    }

    func testDecrypt() throws {

        // Given
        let encryptedData = dataFactory.encryptedOutputData

        // When
        let decryptedBody = try cryptor.decrypt(encryptedData)

        // Then
        XCTAssertEqual(dataFactory.plainInputData,
                       decryptedBody)
    }

    func testEncryptDecrypt() throws {

        // Given
        let body = "any kind of weird message with ü".data(using: .utf8)!

        // When
        let encryptedData = try cryptor.encrypt(body)
        let decryptedBody = try cryptor.decrypt(encryptedData)

        // Then 
        XCTAssertEqual(body, decryptedBody)
    }
}
