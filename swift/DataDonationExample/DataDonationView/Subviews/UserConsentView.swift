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

final class UserConsentView: UIView, UIContentView {

    var configuration: UIContentConfiguration {
        didSet {
            guard let configuration = configuration as? UserConsentRow else {
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

    private lazy var keyLabel: UILabel = UILabel.default.constrainedToFit(on: .vertical)
    private lazy var versionLabel: UILabel = UILabel.default.constrainedToFit(on: .vertical)
    private lazy var dateLabel: UILabel = UILabel.default.constrainedToFit(on: .vertical)
    private lazy var eventTypeLabel: UILabel = UILabel.default.constrainedToFit(on: .vertical)

    private var onTap: (() -> Void)?

    init(configuration: UserConsentRow) {
        self.configuration = configuration
        super.init(frame: .zero)
        setupViews()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func configure(_ action: @escaping () -> Void) {
        onTap = action
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
        let tapGestureRecognizer = UITapGestureRecognizer()
        tapGestureRecognizer.addTarget(self, action: #selector(didTapContentView(_:)))
        addGestureRecognizer(tapGestureRecognizer)
    }

    @objc func didTapContentView(_ sender: Any) {
        onTap?()
    }

    func configure(with rowModel: UserConsentRow) {
        keyLabel.text = rowModel.key
        versionLabel.text = rowModel.version
        dateLabel.text = rowModel.formattedDate
        eventTypeLabel.text = rowModel.eventType
        backgroundColor = .white 
    }
}

extension UserConsentRow: UIContentConfiguration {
    func makeContentView() -> UIView & UIContentView {
        UserConsentView(configuration: self)
    }

    func updated(for state: UIConfigurationState) -> UserConsentRow {
        self
    }
}
