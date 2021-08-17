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
    func tappedAddButton() {
        dataDonationSDKService.createUserConsent { result in
            DispatchQueue.main.async {
                self.viewDidLoad()
            }
        }
    }

    func tappedRevokeButton() {
        dataDonationSDKService.revokeUserConsent { result in
            DispatchQueue.main.async {
                self.viewDidLoad()
            }
        }
    }

    func tappedLogOutButton() {
        coreSDKService.logOut { [weak presenter] in
            presenter?.presentLoggedOut()
        }
    }

    func tappedLogInButton(from view: UIViewController) {
        coreSDKService.openLogin(from: view, didLogin: { [weak self] result in
            self?.viewDidLoad()
        })
    }
}
