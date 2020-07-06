//
//  AppDelegate.swift
//  TemplateApp
//
//  Created by Bence Stumpf on 02/04/2020.
//  Copyright © 2020 data4life. All rights reserved.
//

import UIKit
import Common

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        KoinIosKt.doInitKoin(client: D4LClientWrapper())
        showInitialView()
        return true
    }

}

// MARK: - Navigation methods
extension AppDelegate {
    
    private func showInitialView() {
        window = UIWindow(frame: UIScreen.main.bounds)
        
        guard let window = window else { return }
        
        window.rootViewController = MainViewController()
        
        let duration: TimeInterval = 0.5
        UIView.animate(withDuration: duration) {}
    }
    
}

