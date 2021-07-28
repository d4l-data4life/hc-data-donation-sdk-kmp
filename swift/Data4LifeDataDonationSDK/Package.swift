// swift-tools-version:5.4
import PackageDescription

let package = Package(
    name: "Data4LifeDataDonationSDK",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "Data4LifeDataDonationSDK",
            targets: ["Data4LifeDataDonationSDK"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "Data4LifeDataDonationSDK",
            path: "./Data4LifeDataDonationSDK.xcframework"
        ),
    ]
)
