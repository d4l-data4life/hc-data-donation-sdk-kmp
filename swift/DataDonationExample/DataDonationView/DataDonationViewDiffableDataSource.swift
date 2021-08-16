//
//  DataDonationViewDiffableDataSource.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 16.08.21.
//

import Foundation
import UIKit

enum UserConsentSection: Hashable  {
    case main
}

struct UserConsentRowModel: Hashable {
    let key: String
    let version: String
    let formattedDate: String
    let eventType: String
}

final class DataDonationViewDiffableDataSource: UITableViewDiffableDataSource<UserConsentSection, UserConsentRowModel> {

    init(tableView: UITableView) {
        super.init(tableView: tableView) { tableView, indexPath, consentItem in
            let cell = UITableViewCell()
            cell.contentConfiguration = consentItem
            return cell
        }
    }

    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        "User Consents"
    }
}
