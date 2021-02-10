#!/usr/bin/env bash

SURVEY_SERVICE_DIR="cov-survey"
SURVEY_DATA_DIR="survey-data"
CONSENT_SERVICE_DIR="consent-management"
CONSENT_DATA_DIR="consent-documents"
DONATION_SERVICE_DIR="cov-donation"

function startSurveyService() {
  echo "START SURVEY SERVICE ********************"
  SRC_DIR=$(pwd)
  (cd ../${SURVEY_SERVICE_DIR} && make docker-database)
  (cd ../${SURVEY_SERVICE_DIR} && make run) &>>SURVEY_SERVICE.LOG &
  sleep 10
}

function populateSurveyService() {
  echo "POPULATE SURVEY SERVICE ********************"
  (cd ../${SURVEY_DATA_DIR} && make upload) &>>SURVEY_SERVICE_UPLOAD.LOG
}

function clearSurveyService() {
  echo "CLEAR SURVEY SERVICE ********************"
  (cd ../${SURVEY_SERVICE_DIR} && make docker-database-delete)
}

function startConsentService() {
  echo "START CONSENT SERVICE ********************"
  SRC_DIR=$(pwd)
  (cd ../${CONSENT_SERVICE_DIR} && make docker-database)
  (cd ../${CONSENT_SERVICE_DIR} && make run) &>>CONSENT_SERVICE.LOG &
  sleep 10
}

function populateConsentService() {
  echo "POPULATE CONSENT SERVICE ********************"
  (cd ../${CONSENT_DATA_DIR} && make upload) &>>CONSENT_SERVICE_UPLOAD.LOG
}

function clearConsentService() {
  echo "CLEAR CONSENT SERVICE ********************"
  (cd ../${CONSENT_SERVICE_DIR} && make docker-database-delete)
}

function startDonationService() {
  echo "START DONATION SERVICE ********************"
  (cd ../${DONATION_SERVICE_DIR} && make docker-database)
  (cd ../${DONATION_SERVICE_DIR} && make run-api) &>>DONATION_SERVICE_API.LOG &
  #(cd ../${DONATION_SERVICE_DIR} && make run-uploader) &>>DONATION_SERVICE_UPLOADER.LOG &
  cd "$SRC_DIR" || exit
}

function clearDonationService() {
  echo "CLEAR DONATION SERVICE ********************"
  (cd ../${DONATION_SERVICE_DIR} && make docker-database-delete)
}

function updateAll() {
  echo "PULL ALL REPOSITORIES ********************"
  (cd ../${SURVEY_SERVICE_DIR} && git pull)
  (cd ../${SURVEY_DATA_DIR} && git pull)
  (cd ../${CONSENT_SERVICE_DIR} && git pull)
  (cd ../${CONSENT_DATA_DIR} && git pull)
  (cd ../${DONATION_SERVICE_DIR} && git pull)
  cd "$SRC_DIR" || exit
}

function DD-start() {
  clearSurveyService || true
  clearConsentService || true
  clearDonationService || true
  startSurveyService
  populateSurveyService
  startConsentService
  populateConsentService
  startDonationService
}

function DD-stop() {
  clearSurveyService || true
  clearConsentService || true
  clearDonationService || true
  echo "Killing processes"
  ps aux | grep '[m]ain' | grep 'go' | awk '{print $2}' | xargs sudo kill -9
}

function DD-restart() {
  DD-stop
  DD-start
}
