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
        DataDonationCryptor(ivGenerator: ivGeneratorMock,
                            coreCryptoService: coreCryptoServiceMock)
    }()
}

extension DataDonationCryptorTests {

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
        coreCryptoServiceMock.whenGenerateSymmetric = { [unowned self] in
            .success(self.keyFactory.symmetricKey)
        }
        coreCryptoServiceMock.whenSymmetricEncrypt = { data, key, iv in
            .success(try! Data4LifeCryptor.symEncrypt(key: key, data: data, iv: iv))
        }
        coreCryptoServiceMock.whenSymmetricDecrypt = { data, key, iv in
            .success(try! Data4LifeCryptor.symDecrypt(key: key, data: data, iv: iv))
        }
        coreCryptoServiceMock.whenAsymmetricEncrypt = { data, publicKey in
            .success(try! Data4LifeCryptor.asymEncrypt(publicKey: publicKey, data: data))
        }
        coreCryptoServiceMock.whenAsymmetricDecrypt = { data, privateKey in
            .success(try! Data4LifeCryptor.asymDecrypt(privateKey: privateKey, data: data))
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

        let plainBody = dataFactory.cryptorInputData
        let base64EncodedPublicKey = try! keyFactory.publicKey.asBase64EncodedString()

        // When
        let encryptedData = try cryptor.encrypt(plainBody, base64EncodedPublicKey: base64EncodedPublicKey)

        // Then
        let combinedOne = HybridEncryptedData(combined: encryptedData)
        let combinedTwo = HybridEncryptedData(combined: dataFactory.encryptedOutputData)
        XCTAssertEqual(combinedOne.combined.byteCount, combinedTwo.combined.byteCount)
        XCTAssertEqual(combinedOne.encryptionVersion, combinedTwo.encryptionVersion)
        XCTAssertEqual(combinedOne.encryptedBody, combinedTwo.encryptedBody)
        XCTAssertEqual(combinedOne.iv, combinedTwo.iv)
        XCTAssertEqual(combinedOne.encryptedKey, combinedTwo.encryptedKey)
    }

    func testDecrypt() throws {

        // Given
        let encryptedData = dataFactory.encryptedOutputData
        let base64EncodedPrivateKey = try! keyFactory.privateKey.asBase64EncodedString()

        // When
        let decryptedBody = try cryptor.decrypt(encryptedData, base64EncodedPrivateKey: base64EncodedPrivateKey)

        // Then
        XCTAssertEqual(dataFactory.cryptorInputData,
                       decryptedBody)
    }

    func testEncryptDecrypt() throws {

        // Given
        let body = "any kind of weird message with ü".data(using: .utf8)!
        let base64EncodedPublicKey = try! keyFactory.publicKey.asBase64EncodedString()
        let base64EncodedPrivateKey = try! keyFactory.privateKey.asBase64EncodedString()

        // When
        let encryptedData = try cryptor.encrypt(body, base64EncodedPublicKey: base64EncodedPublicKey)
        let decryptedBody = try cryptor.decrypt(encryptedData, base64EncodedPrivateKey: base64EncodedPrivateKey)

        // Then 
        XCTAssertEqual(body, decryptedBody)
    }
}
