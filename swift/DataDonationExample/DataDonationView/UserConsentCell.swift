//
//  UserConsentCell.swift
//  DataDonationExample
//
//  Created by Alessio Borraccino on 11.08.21.
//

import UIKit

final class UserConsentCell: UITableViewCell {

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

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupViews()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupViews()
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        keyLabel.text = ""
        versionLabel.text = ""
        dateLabel.text = ""
        eventTypeLabel.text = ""
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
