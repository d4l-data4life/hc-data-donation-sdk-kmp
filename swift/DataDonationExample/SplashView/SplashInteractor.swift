//
//  SplashInteractor.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import Data4LifeSDK

final class SplashInteractor {

    private let presenter: SplashPresenter
    private let data4LifeSDKService: Data4LifeSDKService

    init(presenter: SplashPresenter, data4LifeSDKService: Data4LifeSDKService) {
        self.data4LifeSDKService = data4LifeSDKService
        self.presenter = presenter
    }
}

extension SplashInteractor {
    func viewDidLoad() {
        data4LifeSDKService.configure(with: Data4LifeSDKConfiguration())
        data4LifeSDKService.isUserLoggedIn { [weak self] isLoggedIn in
            self?.presenter.handleNavigation(isLoggedIn: isLoggedIn)
        }
    }

    func openLoginScreen(from viewController: UIViewController) {
        data4LifeSDKService.openLogin(from: viewController) { [weak self] result in
            switch result {
            case .success:
                self?.presenter.handleNavigation(isLoggedIn: true)
            case .failure:
                self?.presenter.presentLoginError()
            }
        }
    }
}
