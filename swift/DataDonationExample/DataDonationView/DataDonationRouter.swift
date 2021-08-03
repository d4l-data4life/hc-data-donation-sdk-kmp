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
        let interactor = DataDonationInteractor(service: container.dataDonationService!)
        let viewController = DataDonationViewController(interactor: interactor, router: router)
        interactor.view = viewController
        return viewController
    }
}
