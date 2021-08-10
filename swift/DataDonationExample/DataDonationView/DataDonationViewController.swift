//
//  DataDonationViewController.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 28.07.21.
//

import UIKit

final class DataDonationViewController: UIViewController {

    private let interactor: DataDonationInteractor
    private let router: DataDonationRouter
    private lazy var dataDonationDiffableDataSource: DataDonationDiffableDataSource = {
        let dataSource = DataDonationDiffableDataSource(tableView: tableView) { tableView, indexPath, consentItem in
            return UITableViewCell()
        }

        return dataSource
    }()

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
        stackView.alignment = .center
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
            self.interactor.didTapAdd()
        }))
        button.setTitle("Add test UC", for: .normal)
        return button
    }()

    private lazy var removeButton: UIButton = {
        let button = UIButton(type: .roundedRect, primaryAction: UIAction(handler: { action in
            self.interactor.didTapRemove()
        }))
        button.setTitle("Remove test UC", for: .normal)
        return button
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
}

private extension DataDonationViewController {
    func configureSubviews() {
        view.addSubview(mainStackView)
        mainStackView.addArrangedSubview(tableView)
        mainStackView.addArrangedSubview(buttonStackView)

        let spacingViewOne = UIView()
        let spacingViewTwo = UIView()
        buttonStackView.addArrangedSubview(spacingViewOne)
        buttonStackView.addArrangedSubview(addButton)
        buttonStackView.addArrangedSubview(removeButton)
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
    }
}


enum ConsentSection: Hashable  {
    case main
}

struct ConsentItem: Hashable {
    let item: String
}

extension DataDonationViewController: UITableViewDelegate {

}

final class DataDonationDiffableDataSource: UITableViewDiffableDataSource<ConsentSection, ConsentItem> {

}
