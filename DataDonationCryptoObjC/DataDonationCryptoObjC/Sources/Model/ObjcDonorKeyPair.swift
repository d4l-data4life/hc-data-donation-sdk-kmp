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

@objc public class ObjCKeyPair: NSObject {
    public let base64EncodedPrivateKey: String
    public let base64EncodedPublicKey: String

    public init(privateKey: String, publicKey: String) {
        self.base64EncodedPrivateKey = privateKey
        self.base64EncodedPublicKey = publicKey
        super.init()
    }

    @objc public override func isEqual(_ object: Any?) -> Bool {
        guard let otherKeyPair = object as? ObjCKeyPair else {
            return false
        }

        return
            (otherKeyPair.base64EncodedPrivateKey == base64EncodedPrivateKey) &&
            (otherKeyPair.base64EncodedPublicKey == base64EncodedPublicKey)
    }

    public static func == (lhs: ObjCKeyPair, rhs: ObjCKeyPair) -> Bool {
        lhs.isEqual(rhs)
    }

    @objc public override var hash: Int {
        var hasher = Hasher()
        hasher.combine(base64EncodedPublicKey)
        hasher.combine(base64EncodedPrivateKey)
        return hasher.finalize()
    }
}

extension KeyPair {
    var objCKeyPair: ObjCKeyPair {

        guard let privateKey = try? privateKey.asBase64EncodedString(),
              let publicKey = try? publicKey.asBase64EncodedString() else {
            fatalError("Could not encode the keypair to base64")
        }

        return ObjCKeyPair(privateKey: privateKey, publicKey: publicKey)
    }
}
