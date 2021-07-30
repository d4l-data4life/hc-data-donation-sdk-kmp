//
//  SplashPresenter.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import UIKit

final class SplashPresenter {

    weak var view: SplashViewController?

    func handleNavigation(isLoggedIn: Bool) {
        if isLoggedIn {
            view?.openDataDonationScreen()
        } else {
            view?.openLoginScreen()
        }
    }

    func presentLoginError(_ error: Error) {
        let errorMessage = error.localizedDescription
        let alertController = UIAlertController(title: "Login Error", message: errorMessage, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "Ok", style: .default, handler: { [unowned self] _ in
            view?.openLoginScreen()
        }))
        view?.present(alertController, animated: true, completion: nil)
    }
}
