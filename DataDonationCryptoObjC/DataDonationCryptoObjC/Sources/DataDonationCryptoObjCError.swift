//
//  DataDonationError.swift
//  DataDonationCryptoObjC
//
//  Created by Alessio Borraccino on 24.08.21.
//

import Foundation

enum DataDonationCryptoObjCError: Error, Equatable {
    case couldNotCreateKeyPairFromData(programName: String)
    case couldNotGenerateKeyPair(programName: String)
    case couldNotFetchKeyPair(programName: String)
    case couldNotDeleteKeyPair(programName: String)
}
