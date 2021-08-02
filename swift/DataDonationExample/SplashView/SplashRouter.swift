//
//  SplashViewRouter.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import UIKit

final class SplashRouter {

    private let container: ServiceContainer
    weak var view: UIViewController?

    init(container: ServiceContainer) {
        self.container = container
    }

    static func assemble(_ container: ServiceContainer) -> UIViewController {
        guard let sdkService = container.sdkService else {
            fatalError("Dependency needs to be in place")
        }

        let presenter = SplashPresenter()
        let interactor = SplashInteractor(presenter: presenter, data4LifeSDKService: sdkService)
        let router = SplashRouter(container: container)
        let viewController = SplashViewController(interactor: interactor, router: router)
        presenter.view = viewController
        router.view = viewController
        return viewController
    }

    func routeToDataDonation() {
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(700)) {
            let viewController = DataDonationRouter.assemble(container: self.container)
            let navigationController = UINavigationController(rootViewController: viewController)
            navigationController.modalPresentationStyle = .overCurrentContext
            navigationController.modalTransitionStyle = .crossDissolve
            self.view?.present(navigationController, animated: true)
        }
    }
}
