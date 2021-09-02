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

struct EncryptedData: Equatable {

    static let ivSize: Int = 16

    let encryptionVersion: UInt8
    let iv: Data
    let encryptedKey: Data
    let encryptedBody: Data
}

extension EncryptedData {
    init(combined: Data) {

        let bytes = combined.asBytes
        let encryptionVersion = bytes[0]
        let encryptedKeyOffset = 1
        let encryptedKeyLengthRange = encryptedKeyOffset..<encryptedKeyOffset + 2
        let keyLengthArray = Array(bytes[encryptedKeyLengthRange])
        let encryptedKeyLength = UInt16(uInt8View: keyLengthArray)
        let encryptedKeyRange = encryptedKeyLengthRange.upperBound..<(encryptedKeyLengthRange.upperBound + Int(encryptedKeyLength))

        let encryptedKey = Data(bytes[encryptedKeyRange])

        let ivOffset = encryptedKeyRange.upperBound
        let ivRange = ivOffset..<(ivOffset + EncryptedData.ivSize)
        let iv = Data(bytes[ivRange])

        let encryptedBodyOffset = ivRange.upperBound
        let encryptedBodyLengthRange = encryptedBodyOffset..<(encryptedBodyOffset + 8)

        let encryptedBodyLength = UInt64(uInt8View: Array(bytes[encryptedBodyLengthRange]))
        let encryptedBodyRange = encryptedBodyLengthRange.upperBound..<(encryptedBodyLengthRange.upperBound + Int(encryptedBodyLength))
        let encryptedBody = Data(bytes[encryptedBodyRange])

        self.init(encryptionVersion: encryptionVersion,
                  iv: iv,
                  encryptedKey: encryptedKey,
                  encryptedBody: encryptedBody)
    }

    var combined: Data {
        var byteSequence = [UInt8]()

        byteSequence.append(encryptionVersion)

        let keySize = UInt16(encryptedKey.byteCount).uInt8View
        byteSequence.append(contentsOf: keySize)
        byteSequence.append(contentsOf: encryptedKey.asBytes)

        byteSequence.append(contentsOf: iv.asBytes)

        let bodySize = UInt64(encryptedBody.byteCount).uInt8View
        byteSequence.append(contentsOf: bodySize)
        byteSequence.append(contentsOf: encryptedBody.asBytes)

        return Data(byteSequence)
    }
}

fileprivate extension UInt16 {
    var uInt8View : [UInt8] {
        let littleEndianSelf = self.littleEndian
        return [UInt8(truncatingIfNeeded: littleEndianSelf >> 8),
                UInt8(truncatingIfNeeded: littleEndianSelf)].reversed()
    }

    init(uInt8View: [UInt8]) {
        self = uInt8View.reversed().reduce(UInt16(0)) {
            $0 << 0o10 + UInt16($1)
        }
    }
}

fileprivate extension UInt64 {
    var uInt8View: [UInt8] {
        let littleEndianSelf = self.littleEndian
        return [UInt8(truncatingIfNeeded: littleEndianSelf >> 56),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 48),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 40),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 32),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 24),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 16),
                UInt8(truncatingIfNeeded: littleEndianSelf >> 8),
                UInt8(truncatingIfNeeded: littleEndianSelf)].reversed()
    }

    init(uInt8View: [UInt8]) {
        self = uInt8View.reversed().reduce(UInt64(0)) {
            $0 << 0o10 + UInt64($1)
        }
    }
}
