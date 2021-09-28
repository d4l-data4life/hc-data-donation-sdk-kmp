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

import Foundation
import Data4LifeCrypto
import CryptoKit

final class DataDonationCryptor {

    private let ivGenerator: InitializationVectorGeneratorProtocol
    private let coreCryptoService: CoreCryptoServiceProtocol
    private let encryptionVersion: UInt8

    init(encryptionVersion: UInt8 = 2,
         ivGenerator: InitializationVectorGeneratorProtocol = InitializationVectorGenerator(),
         coreCryptoService: CoreCryptoServiceProtocol = CoreCryptoService()) {

        self.ivGenerator = ivGenerator
        self.coreCryptoService = coreCryptoService
        self.encryptionVersion = encryptionVersion
    }
}

extension DataDonationCryptor: DataDonationCryptorObjCProtocol {

    func encrypt(_ plainBody: Data, base64EncodedPublicKey: String) throws -> Data {

        guard let publicKeyData = Data(base64Encoded: base64EncodedPublicKey) else {
            throw DataDonationCryptoObjCError.couldNotEncryptData
        }

        let symmetricKey = try coreCryptoService.generateSymmetricKey()
        let asymmetricPublicKey = try coreCryptoService.createAsymmetricKey(from: publicKeyData, type: .public)
        let encryptedSymmetricKey = try coreCryptoService.asymmetricEncrypt(data: symmetricKey.value,
                                                                            with: asymmetricPublicKey)
        let iv = ivGenerator.randomIVData(of: HybridEncryptedData.ivSize)
        let encryptedBody = try coreCryptoService.symmetricEncrypt(data: plainBody,
                                                                   with: symmetricKey,
                                                                   using: iv)
        return HybridEncryptedData(encryptionVersion: encryptionVersion,
                             iv: iv,
                             encryptedKey: encryptedSymmetricKey,
                             encryptedBody: encryptedBody).combined
    }

    func decrypt(_ encryptedData: Data, base64EncodedPrivateKey: String) throws -> Data {

        guard let privateKeyData = Data(base64Encoded: base64EncodedPrivateKey) else {
            throw DataDonationCryptoObjCError.couldNotEncryptData
        }

        let encryptedData = HybridEncryptedData(combined: encryptedData)
        let asymmetricPrivateKey = try coreCryptoService.createAsymmetricKey(from: privateKeyData, type: .private)
        let symmetricKeyData = try coreCryptoService.asymmetricDecrypt(data: encryptedData.encryptedKey,
                                                                       with: asymmetricPrivateKey)
        let symmetricKey = try Key(data: symmetricKeyData,
                                   type: .common)
        return try coreCryptoService.symmetricDecrypt(data: encryptedData.encryptedBody,
                                                      with: symmetricKey,
                                                      using: encryptedData.iv)
    }
}
