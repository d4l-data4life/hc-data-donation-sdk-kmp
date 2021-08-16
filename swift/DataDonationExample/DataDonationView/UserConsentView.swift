//
//  UserConsentCell.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 11.08.21.
//

import UIKit


final class UserConsentView: UIView, UIContentView {

    var configuration: UIContentConfiguration {
        didSet {
            guard let configuration = configuration as? UserConsentRowModel else {
                return
            }

            configure(with: configuration)
        }
    }


    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .vertical
        stackView.spacing = 8
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var keyLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        label.textColor = .black
        label.numberOfLines = 0
        return label
    }()

    private lazy var versionLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        label.textColor = .black
        label.numberOfLines = 0
        return label
    }()

    private lazy var dateLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        label.textColor = .black
        label.numberOfLines = 0
        return label
    }()

    private lazy var eventTypeLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        label.textColor = .black
        label.numberOfLines = 0
        return label
    }()

    private var onTap: (() -> Void)?

    init(configuration: UserConsentRowModel) {
        self.configuration = configuration
        super.init(frame: .zero)
        setupViews()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

private extension UserConsentView {
    func setupViews() {
        addSubview(mainStackView)
        NSLayoutConstraint.activate([
            mainStackView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 8),
            mainStackView.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -8),
            mainStackView.topAnchor.constraint(equalTo: topAnchor, constant: 15),
            mainStackView.bottomAnchor.constraint(equalTo: bottomAnchor, constant: -15)
        ])

        mainStackView.addArrangedSubview(keyLabel)
        mainStackView.addArrangedSubview(versionLabel)
        mainStackView.addArrangedSubview(dateLabel)
        mainStackView.addArrangedSubview(eventTypeLabel)
    }


    func configure(with rowModel: UserConsentRowModel) {
        keyLabel.text = rowModel.key
        versionLabel.text = rowModel.version
        dateLabel.text = rowModel.formattedDate
        eventTypeLabel.text = rowModel.eventType
    }
}

extension UserConsentRowModel: UIContentConfiguration {
    func makeContentView() -> UIView & UIContentView {
        UserConsentView(configuration: self)
    }

    func updated(for state: UIConfigurationState) -> UserConsentRowModel {
        self
    }
}
