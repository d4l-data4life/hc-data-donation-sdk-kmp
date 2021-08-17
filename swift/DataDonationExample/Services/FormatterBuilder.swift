//
//  FormatterBuilder.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 17.08.21.
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
