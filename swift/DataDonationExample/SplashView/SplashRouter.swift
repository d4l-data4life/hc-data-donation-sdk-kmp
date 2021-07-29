//
//  SplashViewRouter.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import UIKit

final class SplashRouter {

    weak var view: UIViewController?

    static func assemble() -> UIViewController {
        let presenter = SplashPresenter()
        let interactor = SplashInteractor(presenter: presenter, data4LifeSDKService: Data4LifeSDKService())
        let router = SplashRouter()
        let viewController = SplashViewController(interactor: interactor, router: router)
        presenter.view = viewController
        router.view = viewController
        return UINavigationController(rootViewController: viewController)
    }

    func routeToDataDonation() {
        let viewController = DataDonationViewController()
        view?.navigationController?.pushViewController(viewController, animated: true)
    }
}
