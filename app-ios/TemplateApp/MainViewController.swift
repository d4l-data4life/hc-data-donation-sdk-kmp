//
//  ContentView.swift
//  TemplateApp
//
//  Created by Bence Stumpf on 02/04/2020.
//  Copyright © 2020 data4life. All rights reserved.
//

import UIKit
import Common

class MainViewController: UIViewController {
    
    let hello_label = UILabel()
    let login_label = UILabel()
    
    private var mainVM: MainViewModel?

    // MARK: Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        setupViews()
        observeVM()
        mainVM!.onResume()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        mainVM?.onCleared()
        mainVM = nil
    }
    
    private func observeVM(){
        mainVM = MainViewModel(userLoggedIn:{ isUserLoggedIn in
            let isUserLoggedIn = isUserLoggedIn as! Bool
            self.login_label.text = isUserLoggedIn ? "yes" : "no"
        })
    }
}

// MARK: - Setup views
extension MainViewController {

    /**
     * SetupViews
     */
    private func setupViews() {
        view.backgroundColor = .white
        
        configureSubviews()
        addSubviews()
    }
    
   
    /**
     * ConfigureSubviews
     */
    private func configureSubviews() {
        hello_label.text = SampleKt.hello()
        hello_label.translatesAutoresizingMaskIntoConstraints = false
        
        login_label.text = "Wut"
        login_label.translatesAutoresizingMaskIntoConstraints = false
    }
    

    /**
    * AddSubviews
    */
    private func addSubviews() {
        view.addSubview(hello_label)
        view.addSubview(login_label)
        
        NSLayoutConstraint.activate([
            hello_label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            hello_label.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            hello_label.widthAnchor.constraint(equalToConstant: 120),
            hello_label.heightAnchor.constraint(equalToConstant: 21),
            
            login_label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            login_label.centerYAnchor.constraint(equalTo: hello_label.bottomAnchor, constant: 50.0),
            login_label.widthAnchor.constraint(equalToConstant: 120),
            login_label.heightAnchor.constraint(equalToConstant: 21),
        ])
        
        
    }
}
