//
//  iOSCryptoDD.swift
//  iOSCryptoDD
//
//  Created by Bence Stumpf on 1/7/21.
//

import Foundation
import CryptoKit

@objc public class CryptoAES: NSObject {
    private var key: Data
    
    @objc public init(key: Data) {
        //TODO: Add padding as a param
        self.key = key
    }
    
    @objc public func encrypt(plainText: Data, iv:Data, associatedData: Data) -> Data {
            let sealedData = try! AES.GCM.seal(plainText, using: SymmetricKey(data: key), nonce: AES.GCM.Nonce(data:iv))
            let encryptedContent = sealedData.combined
           
            return encryptedContent ?? Data()
    }
    
    @objc public func decrypt(encrypted: Data, iv:Data, associatedData: Data) -> Data {
        do {
            let sealedBox = try AES.GCM.SealedBox(nonce: AES.GCM.Nonce(data: iv),
                                                   ciphertext: encrypted,
                                                   tag: associatedData)
            let decryptedData = try AES.GCM.open(sealedBox, using: SymmetricKey(data: key))
            return decryptedData
        } catch _ {
            //TODO: Manage exceptions with Kotlin / Obj-C
            return Data()
        }
    }
}

