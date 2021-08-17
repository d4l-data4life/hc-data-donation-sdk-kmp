//
//  DataDonationViewModel.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 17.08.21.
//

import Foundation

struct DataDonationViewModel {
    enum State {
        case loggedIn([UserConsentRowModel])
        case loggedOut
    }
    let state: State
}
