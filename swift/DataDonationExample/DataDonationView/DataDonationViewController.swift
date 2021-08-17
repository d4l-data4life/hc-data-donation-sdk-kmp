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

import UIKit

final class DataDonationViewController: UIViewController {

    private let interactor: DataDonationInteractor
    private let router: DataDonationRouter
    private lazy var dataDonationDiffableDataSource = DataDonationViewDiffableDataSource(tableView: tableView)

    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .insetGrouped)
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.delegate = self
        return tableView
    }()

    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [])
        stackView.axis = .vertical
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.distribution = .fill
        stackView.spacing = 10
        return stackView
    }()

    private lazy var buttonStackView: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [])
        stackView.axis = .horizontal
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.alignment = .center
        stackView.distribution = .fill
        stackView.spacing = 10
        return stackView
    }()

    private lazy var addButton: UIButton = {
        let button = UIButton(type: .roundedRect, primaryAction: UIAction(handler: { action in
            self.interactor.tappedAddButton()
        }))
        button.setTitle("Accept", for: .normal)
        return button
    }()

    private lazy var revokeButton: UIButton = {
        let button = UIButton(type: .roundedRect, primaryAction: UIAction(handler: { action in
            self.interactor.tappedRevokeButton()
        }))
        button.setTitle("Revoke", for: .normal)
        return button
    }()

    private lazy var logOutButton: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(title: "Log Out", image: nil, primaryAction: UIAction.init(handler: { _ in
            self.interactor.tappedLogOutButton()
        }), menu: nil)
        return buttonItem
    }()

    private lazy var loginButton: UIBarButtonItem = {
        let buttonItem = UIBarButtonItem(title: "Log In", image: nil, primaryAction: UIAction.init(handler: { _ in
            self.interactor.tappedLogInButton(from: self)
        }), menu: nil)
        return buttonItem
    }()

    init(interactor: DataDonationInteractor, router: DataDonationRouter) {
        self.interactor = interactor
        self.router = router
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        title = "Data Donation"
        configureSubviews()
        interactor.viewDidLoad()
    }

    func configure(with viewModel: DataDonationViewModel) {

        tableView.refreshControl?.endRefreshing()

        switch viewModel.state {
        case .loggedIn(let userConsents):
            showLogoutButton()
            var snapshot = NSDiffableDataSourceSnapshot<UserConsentSection, UserConsentRow>()
            snapshot.appendSections([.userConsents])
            snapshot.appendItems(userConsents, toSection: .userConsents)
            dataDonationDiffableDataSource.apply(snapshot, animatingDifferences: true)
            buttonStackView.isHidden = false
        case .loggedOut:
            showLoginButton()
            let emptySnapshot = NSDiffableDataSourceSnapshot<UserConsentSection, UserConsentRow>()
            dataDonationDiffableDataSource.apply(emptySnapshot, animatingDifferences: false)
            buttonStackView.isHidden = true
        }
    }
}

private extension DataDonationViewController {
    func configureSubviews() {

        showLogoutButton()

        view.addSubview(mainStackView)
        mainStackView.addArrangedSubview(tableView)
        mainStackView.addArrangedSubview(buttonStackView)

        let spacingViewOne = UIView()
        let spacingViewTwo = UIView()
        buttonStackView.addArrangedSubview(spacingViewOne)
        buttonStackView.addArrangedSubview(addButton)
        buttonStackView.addArrangedSubview(revokeButton)
        buttonStackView.addArrangedSubview(spacingViewTwo)
        NSLayoutConstraint.activate([
            buttonStackView.heightAnchor.constraint(equalToConstant: 44),
            spacingViewOne.widthAnchor.constraint(equalTo: spacingViewTwo.widthAnchor),
            mainStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            mainStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            mainStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            mainStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
        ])

        tableView.dataSource = dataDonationDiffableDataSource
        tableView.refreshControl = UIRefreshControl(frame: .zero,
                                                    primaryAction: UIAction { [unowned self] _ in
                                                        self.interactor.viewDidLoad()
                                                    })
    }

    private func showLoginButton() {
        navigationItem.setRightBarButton(loginButton, animated: true)
    }
    private func showLogoutButton() {
        navigationItem.setRightBarButton(logOutButton, animated: true)
    }
}

extension DataDonationViewController: UITableViewDelegate {

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        UITableView.automaticDimension
    }
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        44
    }
}
