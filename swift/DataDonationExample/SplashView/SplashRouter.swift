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
