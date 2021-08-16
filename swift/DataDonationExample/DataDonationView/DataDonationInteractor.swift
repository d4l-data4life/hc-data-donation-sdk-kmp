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
            let consents = (try? result.get()) ?? []
            let viewModel = DataDonationViewModel(userConsents: consents.map { $0.asItem })
            DispatchQueue.main.async {
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
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateStyle = .medium
//        dateFormatter.dateFormat = ""
//        let date = dateFormatter.date(from: createdAt)!
//        let formattedDate = dateFormatter.string(from: date)
        return UserConsentRowModel(key: self.consentDocumentKey,
                            version: consentDocumentVersion,
                            formattedDate: createdAt,
                            eventType: self.event.name)
    }
}
