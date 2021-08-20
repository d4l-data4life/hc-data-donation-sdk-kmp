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
import Data4LifeDataDonationSDK
import UIKit

final class DataDonationPresenter {

    weak var view: DataDonationViewController?

    private let formatterService: FormatterService

    init(formatterService: FormatterService) {
        self.formatterService = formatterService
    }
}

extension DataDonationPresenter {

    func presentLoggedIn(with userConsents: [UserConsent]) {
        let viewModel = DataDonationViewModel(state: .loggedIn(userConsents.map { makeRowModel(from: $0)}))
        view?.configure(with: viewModel)
    }

    func presentLoggedOut() {
        view?.configure(with: DataDonationViewModel(state: .loggedOut))
    }

    func presentError(_ error: Error) {
        let alertController = UIAlertController(title: "Error", message: error.localizedDescription, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        view?.present(alertController, animated: true)
    }

    private func makeRowModel(from userConsent: UserConsent) -> UserConsentRow {

        let createdAtDate = formatterService.date(from: userConsent.createdAt, type: .isoDate)
        let createdAtDescription = formatterService.string(from: createdAtDate, type: .readableDate)

        return UserConsentRow(key: userConsent.consentDocumentKey,
                                   version: userConsent.consentDocumentVersion,
                                   formattedDate: createdAtDescription,
                                   eventType: userConsent.event.name)
    }
}
