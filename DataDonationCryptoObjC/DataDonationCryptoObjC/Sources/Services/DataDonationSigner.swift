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

final class DataDonationSigner {

    private let donorKeyHolder: DonorKeyHolderProtocol
    private let coreCryptoService: CoreCryptoServiceProtocol

    private let programName: String

    init(programName: String,
         donorKeyHolder: DonorKeyHolderProtocol = DonorKeyHolder(),
         coreCryptoService: CoreCryptoServiceProtocol = CoreCryptoService()) {

        self.donorKeyHolder = donorKeyHolder
        self.coreCryptoService = coreCryptoService
        self.programName = programName
    }
}

extension DataDonationSigner: DataDonationSignerObjCProtocol {

    func sign(data: Data, isSalted: Bool) throws -> Data {
        let donorKeyPair = try donorKeyHolder.fetchKeyPair(for: programName)
        do {
            return try coreCryptoService.sign(data: data,
                                              with: donorKeyPair,
                                              isSalted: isSalted)
        } catch {
            throw DataDonationCryptoObjCError.couldNotSignData
        }
    }

    func verify(data: Data, signature: Data, isSalted: Bool) throws {
        let donorKeyPair = try donorKeyHolder.fetchKeyPair(for: programName)
        do {
            _ = try coreCryptoService.verify(data: data,
                                             signature: signature,
                                             with: donorKeyPair,
                                             isSalted: isSalted)
        } catch {
            throw DataDonationCryptoObjCError.couldNotVerifyData
        }
    }
}
