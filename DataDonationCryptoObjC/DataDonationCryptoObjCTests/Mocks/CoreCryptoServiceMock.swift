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
@testable import DataDonationCryptoObjC
import Data4LifeCrypto

final class CoreCryptoServiceMock: CoreCryptoServiceProtocol {

    var isGenerateSymmetricCalled = false
    var whenGenerateSymmetric: (() -> Result<Key, Error>)?

    func generateSymmetricKey() throws -> Key {
        isGenerateSymmetricCalled = true
        if let result = whenGenerateSymmetric?() {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isGenerateAsymmetricCalled = false
    var capturedGenerateAsymmetricParameters: (String)?
    var whenGenerateAsymmetric: ((String) -> Result<KeyPair, Error>)?

    func generateAsymmetricKeyPair(tag: String) throws -> KeyPair {
        isGenerateAsymmetricCalled = true
        capturedGenerateAsymmetricParameters = (tag)
        if let result = whenGenerateAsymmetric?(tag) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isCreateAsymmetricCalled = false
    var capturedCreateAsymmetricParameters: (Data, AsymmetricKeyType)?
    var whenCreateAsymmetric: ((Data, AsymmetricKeyType) -> Result<AsymmetricKey, Error>)?

    func createAsymmetricKey(from data: Data, type: AsymmetricKeyType) throws -> AsymmetricKey {
        isCreateAsymmetricCalled = true
        capturedCreateAsymmetricParameters = (data, type)
        if let result = whenCreateAsymmetric?(data, type) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isAsymmetricEncryptCalled = false
    var capturedAsymmetricEncryptParameters: (Data, AsymmetricKey)?
    var whenAsymmetricEncrypt: ((Data, AsymmetricKey) -> Result<Data, Error>)?

    func asymmetricEncrypt(data: Data, with publicKey: AsymmetricKey) throws -> Data {
        isAsymmetricEncryptCalled = true
        capturedAsymmetricEncryptParameters = (data, publicKey)

        if let result = whenAsymmetricEncrypt?(data, publicKey) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isAsymmetricDecryptCalled = false
    var capturedAsymmetricDecryptParameters: (Data, AsymmetricKey)?
    var whenAsymmetricDecrypt: ((Data, AsymmetricKey) -> Result<Data, Error>)?

    func asymmetricDecrypt(data: Data, with publicKey: AsymmetricKey) throws -> Data {
        isAsymmetricDecryptCalled = true
        capturedAsymmetricDecryptParameters = (data, publicKey)

        if let result = whenAsymmetricDecrypt?(data, publicKey) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }


    var isSymmetricDecryptCalled = false
    var capturedSymmetricDecryptParameters: (Data, Key, Data)?
    var whenSymmetricDecrypt: ((Data, Key, Data) -> Result<Data, Error>)?

    func symmetricDecrypt(data: Data, with key: Key, using iv: Data) throws -> Data {
        isSymmetricDecryptCalled = true
        capturedSymmetricDecryptParameters = (data, key, iv)

        if let result = whenSymmetricDecrypt?(data, key, iv) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isSymmetricEncryptCalled = false
    var capturedSymmetricEncryptParameters: (Data, Key, Data)?
    var whenSymmetricEncrypt: ((Data, Key, Data) -> Result<Data, Error>)?

    func symmetricEncrypt(data: Data, with key: Key, using iv: Data) throws -> Data {
        isSymmetricEncryptCalled = true
        capturedSymmetricEncryptParameters = (data, key, iv)

        if let result = whenSymmetricEncrypt?(data, key, iv) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isSignCalled = false
    var capturedSignParameters: (Data, KeyPair, Bool)?
    var whenSign: ((Data, KeyPair, Bool) -> Result<Data, Error>)?

    func sign(data: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Data {
        isSignCalled = true
        capturedSignParameters = (data, keyPair, isSalted)
        if let result = whenSign?(data, keyPair, isSalted) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }

    var isVerifyCalled = false
    var capturedVerifyParameters: (Data, Data, KeyPair, Bool)?
    var whenVerify: ((Data, Data, KeyPair, Bool) -> Result<Bool, Error>)?

    func verify(data: Data, signature: Data, with keyPair: KeyPair, isSalted: Bool) throws -> Bool {
        isVerifyCalled = true
        capturedVerifyParameters = (data, signature, keyPair, isSalted)
        if let result = whenVerify?(data, signature, keyPair, isSalted) {
            return try result.get()
        } else {
            throw MockError.resultNotDefined
        }
    }
}
