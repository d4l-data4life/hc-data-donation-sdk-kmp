//  Copyright (c) 2020 D4L data4life gGmbH
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

import Data4LifeCrypto

protocol DonorKeyHolderProtocol {
    func privateKey(for programName: String) throws -> AsymmetricKey
    func publicKey(for programName: String) throws -> AsymmetricKey
    func deleteKeyPair(for programName: String) throws
    func createKeyPair(from data: Data, for programName: String) throws
}

enum DonorKeyHolderError: Error {
    case couldNotStoreKeyPair(reason: String)
}

final class DonorKeyHolder: DonorKeyHolderProtocol {

    private struct Tag {
        static let keyPair = "care.data4life.datadonation.keypair"
    }

    static private(set) var prefixTag = Tag.keyPair
    static private var donorKeyOptions: KeyExchangeFormat = {
        let type: KeyType = .appPrivate
        let keyExchangeFormat = try! KeyExhangeFactory.create(type: type)
        return keyExchangeFormat
    }()

    // Security iOS frameworks stores automatically when generating keys
    func generateKeyPair(for programName: String) throws -> KeyPair {
        let tag = tag(for: programName)
        let algorithm = DonorKeyHolder.donorKeyOptions.algorithm
        let keySize = DonorKeyHolder.donorKeyOptions.size
        let options = KeyOptions(size: keySize, tag: tag)
        do {
            return try Data4LifeCryptor.generateAsymKeyPair(algorithm: algorithm, options: options)
        } catch {
            throw DataDonationCryptoObjCError.couldNotGenerateKeyPair(programName: programName)
        }
    }

    func createKeyPair(from data: Data, for programName: String) throws {
        do {
            let keyPair = try JSONDecoder().decode(KeyPair.self, from: data)
            let tag = tag(for: programName)
            try add(keyPair: keyPair, with: tag)
        } catch {
            throw DataDonationCryptoObjCError.couldNotCreateKeyPairFromData(programName: programName)
        }
    }

    func fetchKeyPair(for programName: String) throws -> KeyPair {
        let tag = tag(for: programName)
        let algorithm = DonorKeyHolder.donorKeyOptions.algorithm
        do {
            return try KeyPair.load(tag: tag, algorithm: algorithm)
        } catch {
            throw DataDonationCryptoObjCError.couldNotFetchKeyPair(programName: programName)
        }
    }

    func deleteKeyPair(for programName: String) throws {
        let tag = tag(for: programName)
        do {
        try KeyPair.destroy(tag: tag)
        } catch {
            throw DataDonationCryptoObjCError.couldNotDeleteKeyPair(programName: programName)
        }
    }
}

extension DonorKeyHolder {
    func privateKey(for programName: String) throws -> AsymmetricKey {
        return try fetchOrGenerateKeyPair(for: programName).privateKey
    }

    func publicKey(for programName: String) throws -> AsymmetricKey {
        return try fetchOrGenerateKeyPair(for: programName).publicKey
    }
}

private extension DonorKeyHolder {

    private func tag(for programName: String) -> String {
        return "\(Tag.keyPair).\(programName)"
    }

    private func fetchOrGenerateKeyPair(for programName: String) throws -> KeyPair {
        guard let keyPair = try? fetchKeyPair(for: programName) else {
            return try generateKeyPair(for: programName)
        }

        return keyPair
    }

    private func add(keyPair: KeyPair, with tag: String) throws {

        let privateSecKeyData = try keyPair.privateKey.asData()
        let publicSecKeyData = try keyPair.publicKey.asData()

        let addPrivateKeyQuery = [kSecClass as String: kSecClassKey,
                                  kSecAttrKeyType as String: kSecAttrKeyTypeRSA,
                                  kSecAttrKeyClass as String: kSecAttrKeyClassPrivate,
                                  kSecValueData as String: privateSecKeyData,
                                  kSecAttrApplicationTag as String: tag] as [String: Any]
        let addPublicKeyQuery = [kSecClass as String: kSecClassKey,
                                 kSecAttrKeyType as String: kSecAttrKeyTypeRSA,
                                 kSecAttrKeyClass as String: kSecAttrKeyClassPublic,
                                 kSecValueData as String: publicSecKeyData,
                                 kSecAttrApplicationTag as String: tag] as [String: Any]

        var status = SecItemAdd(addPrivateKeyQuery as CFDictionary, nil)

        guard status == errSecSuccess else {
            let errorMessage = String(SecCopyErrorMessageString(status, nil)!)
            throw DonorKeyHolderError.couldNotStoreKeyPair(reason: errorMessage)
        }

        status = SecItemAdd(addPublicKeyQuery as CFDictionary, nil)
        guard status == errSecSuccess else {
            let errorMessage = String(SecCopyErrorMessageString(status, nil)!)
            throw DonorKeyHolderError.couldNotStoreKeyPair(reason: errorMessage)
        }
    }
}
