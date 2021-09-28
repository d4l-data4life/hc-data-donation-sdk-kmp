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

protocol CoreCryptoServiceProtocol {

    func generateSymmetricKey() throws -> Key
    func generateAsymmetricKeyPair(tag: String) throws -> KeyPair

    func createAsymmetricKey(from data: Data, type: AsymmetricKeyType) throws -> AsymmetricKey

    func sign(data: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Data
    func verify(data: Data, signature: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Bool
    
    func asymmetricEncrypt(data: Data, with publicKey: AsymmetricKey) throws -> Data
    func asymmetricDecrypt(data: Data, with privateKey: AsymmetricKey) throws -> Data
    func symmetricDecrypt(data: Data, with key: Key, using iv: Data) throws -> Data
    func symmetricEncrypt(data: Data, with key: Key, using iv: Data) throws -> Data
}

final class CoreCryptoService {
    init() {}
}

// MARK: - KeyGenerator
extension CoreCryptoService: CoreCryptoServiceProtocol {

    func generateSymmetricKey() throws -> Key {
        let keyExchangeConfiguration = try KeyExchangeFactory.create(type: .common)
        let keyOptions = KeyOptions(size: keyExchangeConfiguration.size)
        return try Data4LifeKeyGenerator.generateSymKey(algorithm: keyExchangeConfiguration.algorithm,
                                                        options: keyOptions,
                                                        type: .common)
    }

    func generateAsymmetricKeyPair(tag: String) throws -> KeyPair {
        let keyExchangeConfiguration = try KeyExchangeFactory.create(type: .dataDonation)
        let keyOptions = KeyOptions(size: keyExchangeConfiguration.size,
                                    tag: tag)
        return try Data4LifeKeyGenerator.generateAsymKeyPair(algorithm: keyExchangeConfiguration.algorithm,
                                                             options: keyOptions)
    }

    func createAsymmetricKey(from data: Data, type: AsymmetricKeyType) throws -> AsymmetricKey {
        let keyExchangeFormat = try KeyExchangeFactory.create(type: .dataDonation)
        return try AsymmetricKey(data: data,
                                 type: type,
                                 keySize: keyExchangeFormat.size)
    }
}

// MARK: - Cryptor
extension CoreCryptoService {

    func asymmetricEncrypt(data: Data, with publicKey: AsymmetricKey) throws -> Data {
        guard publicKey.type == .public else {
            throw DataDonationCryptoObjCError.couldNotEncryptData
        }
        return try Data4LifeCryptor.asymEncrypt(publicKey: publicKey, data: data)
    }

    func asymmetricDecrypt(data: Data, with privateKey: AsymmetricKey) throws -> Data {
        guard privateKey.type == .private else {
            throw DataDonationCryptoObjCError.couldNotDecryptData
        }
        return try Data4LifeCryptor.asymDecrypt(privateKey: privateKey,
                                                data: data)
    }

    func symmetricDecrypt(data: Data, with key: Key, using iv: Data) throws -> Data {
        return try Data4LifeCryptor.symDecrypt(key: key,
                                               data: data,
                                               iv: iv)
    }

    func symmetricEncrypt(data: Data, with key: Key, using iv: Data) throws -> Data {
        return try Data4LifeCryptor.symEncrypt(key: key,
                                               data: data,
                                               iv: iv)
    }
}

// MARK: - Signer
extension CoreCryptoService {

    func sign(data: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Data {
        return try Data4LifeSigner.sign(data: data, privateKey: keyPair.privateKey, isSalted: isSalted)
    }

    func verify(data: Data, signature: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Bool {
        return try Data4LifeSigner.verify(data: data, against: signature, publicKey: keyPair.publicKey, isSalted: isSalted)
    }
}
