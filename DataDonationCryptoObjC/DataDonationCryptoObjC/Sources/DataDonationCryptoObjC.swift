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

@objc public protocol DataDonationCryptorObjCProtocol {
    @objc func encrypt(_ plainBody: Data, base64EncodedPublicKey: String) throws -> Data 
    @objc func decrypt(_ encryptedData: Data, base64EncodedPrivateKey: String) throws -> Data
}

@objc public protocol DataDonationSignerObjCProtocol {
    @objc func sign(data: Data, isSalted: Bool, donorKeyIdentifier: String) throws -> Data
    @objc func verify(data: Data, signature: Data, isSalted: Bool, donorKeyIdentifier: String) throws
}

@objc public protocol DataDonationKeychainStoreObjCProtocol {
    @objc func generateDonorKeyPair(with keyIdentifier: String) throws
    @objc func storeDonorKeyPairData(_ data: Data, with keyIdentifier: String) throws
    @objc func fetchDonorPrivateKeyAsBase64(with keyIdentifier: String) throws -> String
    @objc func fetchDonorPublicKeyAsBase64(with keyIdentifier: String) throws -> String
    @objc func deleteDonorKeyPair(with keyIdentifier: String) throws
}


