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

import Data4LifeCrypto

protocol DonorKeyHolderProtocol {
    func generateKeyPair(for programName: String) throws -> KeyPair
    func createKeyPair(from data: Data, for programName: String) throws -> KeyPair
    func fetchKeyPair(for programName: String) throws -> KeyPair
    func deleteKeyPair(for programName: String) throws
}

extension DonorKeyHolderProtocol {
    func privateKey(for programName: String) throws -> AsymmetricKey {
        do {
            return try fetchKeyPair(for: programName).privateKey
        } catch {
            return try generateKeyPair(for: programName).privateKey
        }
    }

    func publicKey(for programName: String) throws -> AsymmetricKey {
        do {
            return try fetchKeyPair(for: programName).publicKey
        } catch {
            return try generateKeyPair(for: programName).publicKey
        }
    }
}

final class DonorKeyHolder: DonorKeyHolderProtocol {

    private struct Tag {
        static let keyPair = "care.data4life.datadonation.keypair"
    }

    static private(set) var prefixTag = Tag.keyPair
    static private var donorKeyOptions: KeyExchangeFormat = {
        let type: KeyType = .dataDonation
        let keyExchangeFormat = try! KeyExchangeFactory.create(type: type)
        return keyExchangeFormat
    }()

    private let coreCryptoService: CoreCryptoServiceProtocol

    init(coreCryptoService: CoreCryptoServiceProtocol = CoreCryptoService()) {
        self.coreCryptoService = coreCryptoService
    }
}

extension DonorKeyHolder {

    @discardableResult
    func generateKeyPair(for programName: String) throws -> KeyPair {
        let programTag = tag(for: programName)
        do {
            return try coreCryptoService.generateAsymmetricKeyPair(tag: programTag)
        } catch {
            throw DataDonationCryptoObjCError.couldNotGenerateKeyPair
        }
    }

    @discardableResult
    func createKeyPair(from data: Data, for programName: String) throws -> KeyPair {
        do {
            let keyPair = try JSONDecoder().decode(KeyPair.self, from: data)
            let programTag = tag(for: programName)
            try store(keyPair: keyPair, with: programTag)
            return keyPair
        } catch {
            throw DataDonationCryptoObjCError.couldNotCreateKeyPairFromData
        }
    }

    func fetchKeyPair(for programName: String) throws -> KeyPair {
        let programTag = tag(for: programName)
        let algorithm = DonorKeyHolder.donorKeyOptions.algorithm
        do {
            return try KeyPair.load(tag: programTag, algorithm: algorithm)
        } catch {
            throw DataDonationCryptoObjCError.couldNotFetchKeyPair
        }
    }

    func deleteKeyPair(for programName: String) throws {
        let programTag = tag(for: programName)
        do {
        try KeyPair.destroy(tag: programTag)
        } catch {
            throw DataDonationCryptoObjCError.couldNotDeleteKeyPair
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

    func tag(for programName: String) -> String {
        return "\(Tag.keyPair).\(programName)"
    }
}

private extension DonorKeyHolder {

    func fetchOrGenerateKeyPair(for programName: String) throws -> KeyPair {
        guard let keyPair = try? fetchKeyPair(for: programName) else {
            return try generateKeyPair(for: programName)
        }

        return keyPair
    }

    func store(keyPair: KeyPair, with tag: String) throws {
        do {
            try keyPair.store(tag: tag)
        } catch {
            throw DataDonationCryptoObjCError.couldNotStoreKeyPair
        }
    }
}
