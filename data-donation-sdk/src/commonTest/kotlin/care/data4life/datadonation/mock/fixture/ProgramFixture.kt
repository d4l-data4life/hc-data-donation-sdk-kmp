/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.datadonation.mock.fixture

import care.data4life.datadonation.donation.program.model.BlurFunction
import care.data4life.datadonation.donation.program.model.FhirResourceConfiguration
import care.data4life.datadonation.donation.program.model.Program
import care.data4life.datadonation.donation.program.model.ProgramBlur
import care.data4life.datadonation.donation.program.model.ProgramConfiguration
import care.data4life.datadonation.donation.program.model.ProgramDonationConfiguration
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseBlur
import care.data4life.datadonation.donation.program.model.QuestionnaireResponseItemBlur
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object ProgramFixture {
    val sampleProgram = Program(
        name = "sample",
        slug = "sample",
        tenantID = "d4l",
        configuration = ProgramDonationConfiguration(
            consentKey = "d4l.sample",
            programConfiguration = ProgramConfiguration(
                programBlur = ProgramBlur(
                    targetTimeZone = "Europe/Berlin",
                    questionnaireResponseAuthored = BlurFunction.START_OF_DAY,
                    researchSubject = BlurFunction.START_OF_DAY
                )
            ),
            fhirResourceConfigurations = listOf(
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/personal_info",
                    versions = listOf(
                        "1.0.0",
                        "1.0.1",
                        "1.1.0",
                        "1.2.0"
                    )
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/covid_exposition",
                    versions = listOf(
                        "1.0.0",
                        "1.0.1"
                    ),
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/corona_testing",
                    versions = listOf(
                        "1.0.0",
                        "1.1.0"
                    )
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/corona_testing_and_symptoms",
                    versions = listOf(
                        "1.0.0",
                        "1.1.0",
                        "1.2.0"
                    )
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/covid_first_something",
                    versions = listOf(
                        "1.0.0",
                        "1.1.0"
                    ),
                    fhirBlur = QuestionnaireResponseBlur(
                        questionnaireResponseItemBlurs = listOf(
                            QuestionnaireResponseItemBlur(
                                linkId = "when_done_something_first_time",
                                function = BlurFunction.START_OF_DAY
                            )
                        )
                    )
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/covid_second_something",
                    versions = listOf(
                        "1.0.0",
                        "1.1.0",
                        "1.2.0"
                    ),
                    fhirBlur = QuestionnaireResponseBlur(
                        questionnaireResponseItemBlurs = listOf(
                            QuestionnaireResponseItemBlur(
                                linkId = "when_done_something_second_time",
                                function = BlurFunction.START_OF_DAY
                            )
                        )
                    )
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/vaccination_side_effects_a|1.0.0"
                ),
                FhirResourceConfiguration(
                    url = "http://fhir.data4life.care/stu3/SAMLPE/Questionnaire/vaccination_side_effects_b|1.0.0"
                )
            ),
            delay = 0.0,
            studyID = "sample"
        )
    )
}
