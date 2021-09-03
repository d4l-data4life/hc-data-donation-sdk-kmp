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
    func asymmetricEncrypt(data: Data, with keyPair: KeyPair) throws -> Data
    func asymmetricDecrypt(data: Data, with keyPair: KeyPair) throws -> Data
    func symmetricDecrypt(data: Data, with key: Key, using iv: Data) throws -> Data
    func symmetricEncrypt(data: Data, with key: Key, using iv: Data) throws -> Data
}

final class CoreCryptoService {
    init() {}
}

extension CoreCryptoService: CoreCryptoServiceProtocol {

    func generateSymmetricKey() throws -> Key {
        let keyExchangeConfiguration = try KeyExchangeFactory.create(type: .common)
        return try Data4LifeKeyGenerator.generateSymKey(algorithm: keyExchangeConfiguration.algorithm,
                                                   options: KeyOptions(size: keyExchangeConfiguration.size),
                                                   type: .common)
    }

    func generateAsymmetricKeyPair(tag: String) throws -> KeyPair {
        let keyExchangeConfiguration = try KeyExchangeFactory.create(type: .dataDonation)
        return try Data4LifeKeyGenerator.generateAsymKeyPair(algorithm: keyExchangeConfiguration.algorithm,
                                                             options: KeyOptions(size: keyExchangeConfiguration.size,
                                                                            tag: tag))
    }

    func asymmetricEncrypt(data: Data, with keyPair: KeyPair) throws -> Data {
        return try Data4LifeCryptor.asymEncrypt(key: keyPair, data: data)
    }

    func asymmetricDecrypt(data: Data, with keyPair: KeyPair) throws -> Data {
        return try Data4LifeCryptor.asymDecrypt(key: keyPair,
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

