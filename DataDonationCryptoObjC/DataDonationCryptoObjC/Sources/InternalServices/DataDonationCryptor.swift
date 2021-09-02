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

    private let donorKeyHolder: DonorKeyHolderProtocol
    private let ivGenerator: InitializationVectorGeneratorProtocol
    private let coreCryptoService: CoreCryptoServiceProtocol

    private let programName: String
    private let encryptionVersion: UInt8

    init(programName: String,
         encryptionVersion: UInt8 = 2,
         donorKeyHolder: DonorKeyHolderProtocol = DonorKeyHolder(),
         ivGenerator: InitializationVectorGeneratorProtocol = InitializationVectorGenerator(),
         coreCryptoService: CoreCryptoServiceProtocol = CoreCryptoService()) {
        
        self.donorKeyHolder = donorKeyHolder
        self.ivGenerator = ivGenerator
        self.coreCryptoService = coreCryptoService

        self.programName = programName
        self.encryptionVersion = encryptionVersion
    }
}

extension DataDonationCryptor: DataDonationCryptorProtocol {

    static func make(programName: String) -> DataDonationCryptorProtocol {
        return DataDonationCryptor(programName: programName)
    }

    func encrypt(_ plainBody: Data) throws -> Data {
        let symmetricKey = try coreCryptoService.generateSymmetricKey()
        let donorKeyPair = try donorKeyHolder.fetchKeyPair(for: programName)
        let encryptedSymmetricKey = try coreCryptoService.asymmetricEncrypt(data: symmetricKey.value,
                                                                            with: donorKeyPair)
        let iv = ivGenerator.randomIVData(of: EncryptedData.ivSize)
        let encryptedBody = try coreCryptoService.symmetricEncrypt(data: plainBody,
                                                                   with: symmetricKey,
                                                                   using: iv)
        return EncryptedData(encryptionVersion: encryptionVersion,
                             iv: iv,
                             encryptedKey: encryptedSymmetricKey,
                             encryptedBody: encryptedBody).combined
    }

    func decrypt(_ encryptedData: Data) throws -> Data {
        let donorKeyPair = try donorKeyHolder.fetchKeyPair(for: programName)
        let encryptedData = EncryptedData(combined: encryptedData)
        let symmetricKeyData = try coreCryptoService.asymmetricDecrypt(data: encryptedData.encryptedKey,
                                                                       with: donorKeyPair)
        let symmetricKey = try Key(data: symmetricKeyData,
                                   type: .common)
        return try coreCryptoService.symmetricDecrypt(data: encryptedData.encryptedBody,
                                                      with: symmetricKey,
                                                      using: encryptedData.iv)
    }
}
