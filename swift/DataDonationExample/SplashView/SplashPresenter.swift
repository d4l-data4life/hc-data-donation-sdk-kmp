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
            print("TODO move to data donation")
        } else {
            view?.openLoginScreen()
        }
    }

    func presentLoginError() {
        let errorMessage = "error"
        let alertController = UIAlertController(title: "Error", message: errorMessage, preferredStyle: .alert)
        view?.present(alertController, animated: true, completion: nil)
    }
}
