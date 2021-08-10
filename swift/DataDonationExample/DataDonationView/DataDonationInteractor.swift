//
//  DataDonationInteractor.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation

final class DataDonationInteractor {
    
    private let dataDonationSDKService: DataDonationSDKService
    weak var view: DataDonationViewController?
    
    init(service: DataDonationSDKService) {
        self.dataDonationSDKService = service
    }
}

extension DataDonationInteractor {

    func viewDidLoad() {
        dataDonationSDKService.fetchUserConsents()
    }
    func didTapAdd() {
        dataDonationSDKService.createUserConsent()
    }

    func didTapRemove() {
        dataDonationSDKService.revokeUserConsent()
    }
}
