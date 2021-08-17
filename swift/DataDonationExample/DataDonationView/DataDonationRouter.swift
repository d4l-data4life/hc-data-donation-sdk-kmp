//
//  DataDonationRouter.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 02.08.21.
//

import Foundation

final class DataDonationRouter {

    static func assemble(container: ServiceContainer) -> DataDonationViewController {
        let router = DataDonationRouter()
        let presenter = DataDonationPresenter(formatterService: container.formatterService!)
        let interactor = DataDonationInteractor(presenter: presenter, dataDonationSDKService: container.dataDonationService!, coreSDKService: container.sdkService!)
        let viewController = DataDonationViewController(interactor: interactor, router: router)
        presenter.view = viewController
        return viewController
    }
}
