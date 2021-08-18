#!/bin/bash

xcodebuild  -project DataDonationCryptoObjC/DataDonationCryptoObjC.xcodeproj -scheme DataDonationCryptoObjC -derivedDataPath ./build/release-iphoneos -destination 'generic/platform=iOS' -configuration Release SKIP_INSTALL=NO
cp -r ./build/release-iphoneos/Build/Products/Release-iphoneos ./DataDonationCryptoObjC/Products/
xcodebuild  -project DataDonationCryptoObjC/DataDonationCryptoObjC.xcodeproj -scheme DataDonationCryptoObjC -derivedDataPath ./build/release-iphonesimulator -destination 'generic/platform=iOS Simulator' -configuration Release SKIP_INSTALL=NO
cp -r ./build/release-iphonesimulator/Build/Products/Release-iphonesimulator ./DataDonationCryptoObjC/Products/