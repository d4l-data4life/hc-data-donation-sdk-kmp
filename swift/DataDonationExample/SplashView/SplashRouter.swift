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

    static func assemble(_ container: ServiceContainer) -> UIViewController {
        let presenter = SplashPresenter()
        let interactor = SplashInteractor(presenter: presenter, data4LifeSDKService: container.sdkService)
        let router = SplashRouter()
        let viewController = SplashViewController(interactor: interactor, router: router)
        presenter.view = viewController
        router.view = viewController
        return viewController
    }

    func routeToDataDonation() {
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(700)) {
            let viewController = DataDonationViewController()
            let navigationController = UINavigationController(rootViewController: viewController)
            navigationController.modalPresentationStyle = .overCurrentContext
            navigationController.modalTransitionStyle = .crossDissolve
            self.view?.present(navigationController, animated: true)
        }
    }
}

extension UIWindow {
        static var sceneMain: UIWindow? {

                guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                      let delegate = windowScene.delegate as? SceneDelegate, let window = delegate.window else { return nil }
                return window
            }
}
