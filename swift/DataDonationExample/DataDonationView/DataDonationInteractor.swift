//
//  DataDonationInteractor.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation

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
        dataDonationSDKService.fetchUserConsents()
    }
    func didTapAdd() {
        dataDonationSDKService.createUserConsent()
    }

    func didTapRemove() {
        dataDonationSDKService.revokeUserConsent()
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
