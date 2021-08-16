//
//  UserConsentCell.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 11.08.21.
//

import UIKit

struct UserConsentItem: UIContentConfiguration, Hashable {
    func makeContentView() -> UIView & UIContentView {
        UserConsentCell(configuration: self)
    }

    func updated(for state: UIConfigurationState) -> UserConsentItem {
        self
    }

    let key: String
    let version: String
    let date: String
    let eventType: String
}

final class UserConsentCell: UITableViewCell, UIContentView {

    var configuration: UIContentConfiguration {
        didSet {
            guard let configuration = configuration as? UserConsentItem else {
                return
            }

            configure(with: configuration)
        }
    }


    private lazy var mainStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.axis = .horizontal
        stackView.spacing = 8
        return stackView
    }()

    private lazy var keyLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()

    private lazy var versionLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()

    private lazy var dateLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()

    private lazy var eventTypeLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.required, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()

    private var onTap: (() -> Void)?

    init(configuration: UserConsentItem) {
        self.configuration = configuration
        super.init(style: .default, reuseIdentifier: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        keyLabel.text = ""
        versionLabel.text = ""
        dateLabel.text = ""
        eventTypeLabel.text = ""
    }
}

extension UserConsentCell {
    func configure(with userConsentItem: UserConsentItem) {
        keyLabel.text = userConsentItem.key
        versionLabel.text = userConsentItem.version
        dateLabel.text = userConsentItem.date
        eventTypeLabel.text = userConsentItem.eventType
    }
}

private extension UserConsentCell {
    func setupViews() {
        contentView.addSubview(mainStackView)
        NSLayoutConstraint.activate([
            mainStackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 15),
            mainStackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: 15),
            mainStackView.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 15),
            mainStackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: 15)
        ])

        mainStackView.addArrangedSubview(keyLabel)
        mainStackView.addArrangedSubview(versionLabel)
        mainStackView.addArrangedSubview(dateLabel)
        mainStackView.addArrangedSubview(eventTypeLabel)
    }
}
