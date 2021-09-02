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

import XCTest
@testable import DataDonationCryptoObjC
import Data4LifeCrypto

class EncryptedDataTests: XCTestCase {

    func testCombineDecombine() throws {
        let iv = InitializationVectorGenerator().randomIVData(of: 16)
        let encryptedBody = "Hello-i-am-encrypted".data(using: .utf8)
        let encryptedKey = "whatever-encrypted-key".data(using: .utf8)
        let encryptionVersion: UInt8 = 2

        let encryptedData = EncryptedData(encryptionVersion: encryptionVersion,
                                          iv: iv,
                                          encryptedKey: encryptedKey!,
                                          encryptedBody: encryptedBody!)
        
        let combinedData = encryptedData.combined
        XCTAssertEqual(encryptedData.expectedCombinedLength,
                       combinedData.byteCount)
        
        let decombinedData = EncryptedData(combined: combinedData)
        XCTAssertEqual(encryptedData, decombinedData)
    }
}

private extension EncryptedData {
    var expectedCombinedLength: Int {
        let encryptionVersionByteCount = MemoryLayout<UInt8>.size
        let encryptedKeyLengthByteCount = MemoryLayout<UInt16>.size
        let encryptedKeyByteCount = encryptedKey.byteCount
        let ivByteCount = iv.byteCount
        let encryptedBodyLengthByteCount = MemoryLayout<UInt64>.size
        let encryptedBodyByteCount = encryptedBody.byteCount

        let sum = encryptionVersionByteCount +
            encryptedKeyLengthByteCount + encryptedKeyByteCount +
            ivByteCount +
            encryptedBodyLengthByteCount + encryptedBodyByteCount

        return sum
    }
}
