//
//  SplashViewController.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import Foundation
import UIKit

final class SplashViewController: UIViewController {

    private let interactor: SplashInteractor
    private let router: SplashRouter

    private lazy var mainStackView: UIStackView = {
        let verticalStackView = UIStackView(arrangedSubviews: [loadingIndicator])
        verticalStackView.axis = .horizontal
        verticalStackView.translatesAutoresizingMaskIntoConstraints = false
        verticalStackView.alignment = .center
        verticalStackView.distribution = .fill
        return verticalStackView
    }()

    private lazy var loadingIndicator: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView(style: .medium)
        view.startAnimating()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    init(interactor: SplashInteractor, router: SplashRouter) {
        self.interactor = interactor
        self.router = router
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.addSubview(mainStackView)
        configureView()
        interactor.viewDidLoad()
    }
}

extension SplashViewController {
    private func configureView() {

        view.backgroundColor = .white
        NSLayoutConstraint.activate([
            mainStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            mainStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            mainStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            mainStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
        ])
    }

    func openLoginScreen() {
        interactor.openLoginScreen(from: self)
    }

    func openDataDonationScreen() {
        router.routeToDataDonation()
    }
}
