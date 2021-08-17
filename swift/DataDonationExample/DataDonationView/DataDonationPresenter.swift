//
//  DataDonationPresenter.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 17.08.21.
//

import Foundation
import Data4LifeDataDonationSDK

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

    private func makeRowModel(from userConsent: UserConsent) -> UserConsentRowModel {

        let createdAtDate = formatterService.date(from: userConsent.createdAt, type: .isoDate)
        let createdAtDescription = formatterService.string(from: createdAtDate, type: .readableDate)

        return UserConsentRowModel(key: userConsent.consentDocumentKey,
                                   version: userConsent.consentDocumentVersion,
                                   formattedDate: createdAtDescription,
                                   eventType: userConsent.event.name)
    }
}
