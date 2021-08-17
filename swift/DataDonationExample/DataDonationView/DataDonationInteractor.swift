//
//  DataDonationInteractor.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import UIKit
import Data4LifeDataDonationSDK

final class DataDonationInteractor {
    
    private let dataDonationSDKService: DataDonationSDKService
    private let coreSDKService: Data4LifeSDKService
    private let presenter: DataDonationPresenter

    init(presenter: DataDonationPresenter, dataDonationSDKService: DataDonationSDKService, coreSDKService: Data4LifeSDKService) {
        self.presenter = presenter
        self.dataDonationSDKService = dataDonationSDKService
        self.coreSDKService = coreSDKService
    }
}

extension DataDonationInteractor {

    func viewDidLoad() {
        dataDonationSDKService.fetchUserConsents { [weak presenter] result in
            DispatchQueue.main.async {
                let consents = (try? result.get()) ?? []
                presenter?.presentLoggedIn(with: consents)
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
            DispatchQueue.main.async {
                self.viewDidLoad()
            }
        }
    }

    func didTapLogOut() {
        coreSDKService.logOut { [weak presenter] in
            presenter?.presentLoggedOut()
        }
    }

    func didTapLogin(from view: UIViewController) {
        coreSDKService.openLogin(from: view, didLogin: { [weak self] result in
            self?.viewDidLoad()
        })
    }
}
