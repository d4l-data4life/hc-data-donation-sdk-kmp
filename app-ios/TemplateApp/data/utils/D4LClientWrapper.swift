//
//  D4LClientWrapper.swift
//  TemplateApp
//
//  Created by Bence Stumpf on 06/04/2020.
//  Copyright © 2020 data4life. All rights reserved.
//

import Foundation
import Common
import Data4LifeSDK

class D4LClientWrapper: D4LClient {
    
    init() {
        Data4LifeClient.configureWith(clientId: "31be119e-3782-4db8-a24a-1490eea27ed3#ios",
        clientSecret: "iossupersecret",
        redirectURLString: "de.gesundheitscloud.31be119e-3782-4db8-a24a-1490eea27ed3://oauth/",
        environment: .staging)
    }
    
    
    func isUserLoggedIn(resultListener: ResultListener) {
        Data4LifeClient.default.isUserLoggedIn { result in
            if (result.error == nil) {
                resultListener.onSuccess(t: result.value)
            } else {
                resultListener.onSuccess(t: false)
            }
        }
    }
}
