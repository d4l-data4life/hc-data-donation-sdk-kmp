//  Copyright (c) 2020 D4L data4life gGmbH
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

final class FormatterService {
    enum Variant {
        case isoDate
        case readableDate
    }
}

extension FormatterService {
    func date(from string: String, type: Variant) -> Date {
        guard let date = dateFormatter(type).date(from: string) else {
            fatalError("String: \(string) is not parsable as type: \(type)")
        }

        return date
    }

    func string(from date: Date, type: Variant) -> String {
        return dateFormatter(type).string(from: date)
    }
}

private extension FormatterService {
    static var isoDateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        return dateFormatter
    }()
    
    static var readableDateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .short
        dateFormatter.timeStyle = .short
        return dateFormatter
    }()

    func dateFormatter(_ type: FormatterService.Variant) -> DateFormatter {
        switch type {
        case .isoDate:
            return FormatterService.isoDateFormatter
        case .readableDate:
            return FormatterService.readableDateFormatter
        }
    }
}
