//
//  CommonSwift.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 17.08.21.
//

import Foundation
import Data4LifeDataDonationSDK

typealias KotlinJob = Kotlinx_coroutines_coreJob
typealias KotlinCancellationError = Kotlinx_coroutines_coreCancellationException

extension NSArray {
    func array<T>(of type: T.Type = T.self) -> [T] {
        guard let array = Array(self) as? [T] else {
            fatalError("Cant convert NSArray into [\(type)]")
        }
        return array
    }
}
