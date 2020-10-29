//  BSD 3-Clause License
//
//  Copyright (c) 2019, HPS Gesundheitscloud gGmbH
//  All rights reserved.
//
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are met:
//
//  * Redistributions of source code must retain the above copyright notice, this
//  list of conditions and the following disclaimer.
//
//  * Redistributions in binary form must reproduce the above copyright notice,
//  this list of conditions and the following disclaimer in the documentation
//  and/or other materials provided with the distribution.
//
//  * Neither the name of the copyright holder nor the names of its
//  contributors may be used to endorse or promote products derived from
//  this software without specific prior written permission.
//
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
//  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
//  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
//  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
import XCTest
@testable import iOSCryptoDD

class iOSCryptoDDTests: XCTestCase {
    
    func testCryptoDDEncryptNoPadding() {
        let cryptoClient = CryptoAES(key: Data(base64Encoded: "LL85V5L0n5024C6XLGe+/mmg9F/VkUbRQycnN28S5L4=")!)
        let plainText = Data(base64Encoded: "SGVsbG8gV29ybGQ=")!
        let associatedData = Data(base64Encoded: "uy2BwDTY4vUeBrp+")!
        
        let encryptedData = cryptoClient.encrypt(plainText: plainText,iv:associatedData, associatedData: associatedData)
        let decryptedData = cryptoClient.decrypt(encrypted: encryptedData,iv:associatedData, associatedData: associatedData)
        
        XCTAssertEqual (plainText , decryptedData)
    }
    
    func testCryptoDDDecryptNoPadding() {
        let cryptoClient = CryptoAES(key: Data(base64Encoded: "LL85V5L0n5024C6XLGe+/mmg9F/VkUbRQycnN28S5L4=")!)
        let input = Data(base64Encoded: "t7V3l3jyjXv6DWP0e9KlEiTOHXGPxBsqBCTl")!
        let associatedData = Data(base64Encoded: "uy2BwDTY4vUeBrp+")!
        
        let decryptedData = cryptoClient.decrypt(encrypted: input,iv:associatedData, associatedData: associatedData)
        
        XCTAssertEqual(Data(base64Encoded: "SGVsbG8gV29ybGQ="), decryptedData)
    }
    
//    func testCryptoDDZeroPadding() {
//        let cryptoClient = CryptoAES(key: Data(base64Encoded: "LL85V5L0n5024C6XLGe+/mmg9F/VkUbRQycnN28S5L4=")!, padding: .zeroPadding)
//        let plainText = Data(base64Encoded: "SGVsbG8gV29ybGQ=")!
//        let associatedData = Data(base64Encoded: "uy2BwDTY4vUeBrp+")!
//        
//        let encryptedData = cryptoClient.encrypt(plainText: plainText, associatedData: associatedData)
//        
//        let decryptedData = cryptoClient.decrypt(encrypted: encryptedData, associatedData: associatedData)
//        XCTAssertEqual(plainText, decryptedData)
//    }
//    
//    func testCryptoDDPKCS7Padding() {
//        let cryptoClient = CryptoAES(key: Data(base64Encoded: "LL85V5L0n5024C6XLGe+/mmg9F/VkUbRQycnN28S5L4=")!, padding: .pkcs7)
//        let plainText = Data(base64Encoded: "SGVsbG8gV29ybGQ=")!
//        let associatedData = Data(base64Encoded: "uy2BwDTY4vUeBrp+")!
//        
//        let encryptedData = cryptoClient.encrypt(plainText: plainText, associatedData: associatedData)
//        
//        let decryptedData = cryptoClient.decrypt(encrypted: encryptedData, associatedData: associatedData)
//        XCTAssertEqual(plainText, decryptedData)
//    }
}
