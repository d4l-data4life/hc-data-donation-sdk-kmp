//
//  iOSCryptoDD.swift
//  iOSCryptoDD
//
//  Created by Bence Stumpf on 1/7/21.
//

import Foundation
import CryptoSwift

@objc public class CryptoAES: NSObject {
    private var key: Data
       private var padding: Padding

       @objc public init(key: Data) {
           //TODO: Add padding as a param
           self.key = key
           self.padding = .noPadding
       }

       @objc public func encrypt(plainText: Data, iv:Data, associatedData: Data) -> Data {
               let blockMode = GCM(iv: iv.bytes, additionalAuthenticatedData: associatedData.bytes, mode: .combined)
               let aes = try! AES(key: key.bytes, blockMode: blockMode, padding: padding)
               let ciphertext = try! aes.encrypt(plainText.bytes)

               return Data(ciphertext)
       }

       @objc public func decrypt(encrypted: Data, iv:Data, associatedData: Data) -> Data {
           do {
               let block = GCM(iv: iv.bytes, additionalAuthenticatedData: associatedData.bytes, mode: .combined)
               let aes = try AES(key: key.bytes, blockMode: block, padding: padding)
               let ciphertext = try aes.decrypt(encrypted.bytes)

               return Data(ciphertext)
           } catch _ {
               //TODO: Manage exceptions with Kotlin / Obj-C
               return Data()
           }
       }
}

