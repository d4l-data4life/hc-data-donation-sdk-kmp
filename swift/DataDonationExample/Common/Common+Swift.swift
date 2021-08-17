//
//  CommonSwift.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 17.08.21.
//

import Foundation
import Data4LifeDataDonationSDK

extension NSArray {
    func array<T>(of type: T.Type = T.self) -> [T] {
        guard let array = Array(self) as? [T] else {
            fatalError("Cant convert NSArray into [\(type)]")
        }
        return array
    }
}
