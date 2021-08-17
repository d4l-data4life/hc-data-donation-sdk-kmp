//
//  DataDonationInteractor.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import Data4LifeDataDonationSDK

final class DataDonationInteractor {
    
    private let dataDonationSDKService: DataDonationSDKService
    private let coreSDKService: Data4LifeSDKService

    weak var view: DataDonationViewController?
    
    init(dataDonationSDKService: DataDonationSDKService, coreSDKService: Data4LifeSDKService) {
        self.dataDonationSDKService = dataDonationSDKService
        self.coreSDKService = coreSDKService
    }
}

extension DataDonationInteractor {

    func viewDidLoad() {
        view?.setCanLogoutState()
        dataDonationSDKService.fetchUserConsents { [weak view] result in
            DispatchQueue.main.async {
                let consents = (try? result.get()) ?? []
                let viewModel = DataDonationViewModel(userConsents: consents.map { $0.asItem })

                view?.configure(with: viewModel)
            }
        }
    }
    func didTapAdd() {
        dataDonationSDKService.createUserConsent { result in
            DispatchQueue.main.async {
                self.viewDidLoad()
            }
        }
    }

    func didTapRemove() {
        dataDonationSDKService.revokeUserConsent { result in

        }
    }

    func didTapLogOut() {
        coreSDKService.logOut { [weak self] in
            self?.view?.setNeedsLoginState()
        }
    }

    func didTapLogin() {
        coreSDKService.openLogin(from: view!, didLogin: { [weak self] result in
            self?.viewDidLoad()
        })
    }
}

private extension UserConsentProtocol {


    var asItem: UserConsentRowModel {
        let jsonDateFormatter = DateFormatter()
        jsonDateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        let date = jsonDateFormatter.date(from: createdAt)!

        let stringDateFormatter = DateFormatter()
        stringDateFormatter.dateStyle = .short
        stringDateFormatter.timeStyle = .short

        return UserConsentRowModel(key: self.consentDocumentKey,
                                   version: consentDocumentVersion,
                                   formattedDate: stringDateFormatter.string(from: date),
                                   eventType: self.event.name)
    }
}
