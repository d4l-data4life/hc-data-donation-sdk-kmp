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

package care.data4life.datadonation.di

import care.data4life.datadonation.Contract
import care.data4life.datadonation.core.model.Environment
import care.data4life.datadonation.internal.di.initKoin
import care.data4life.datadonation.internal.domain.usecases.DonateResources
import care.data4life.datadonation.internal.domain.usecases.RegisterNewDonor
import care.data4life.datadonation.internal.domain.usecases.UsecaseContract
import care.data4life.datadonation.internal.runner.UsecaseRunnerContract
import care.data4life.datadonation.mock.stub.ClientConfigurationStub
import org.koin.core.context.stopKoin
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinTest {
    private val config = ClientConfigurationStub()

    @BeforeTest
    fun setUp() {
        stopKoin()
        config.clear()
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a CreateUserConsent`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        // When
        val app = initKoin(config)
        // Then
        val usecase: UsecaseContract.CreateUserConsent by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a RegisterNewDonor`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        config.whenGetServicePublicKey = { service ->
            if (service == Contract.Service.DD) {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWYsUPv7etCQYYhMtwkP" +
                    "xGH7144My0yUnqCmF38w40S7CCd54fa1zhijyvAEU67gMgxesyi2bMHPQJp2E63f" +
                    "g/0IcY4kY//9NrtWY7QovOJaFa8ov+wiIbKa3Y5zy4sxq8VoBJlr1EBYaQNX6I9f" +
                    "NG+IcQlkoTTqL+qt7lYsW0P4H3vR/92HHaJjA+yvSbXhePMh2IN4ESTqbBSSwWfd" +
                    "AHtFlH63hV65EB0pUudPumWpUrJWYczveoUO3XUU4qmJ7lZU0kTUFBwwfdeprZtG" +
                    "nEgS+ZIQAp4Y9BId1Ris5XgZDwmMYF8mB1sqGEnbQkmkaMPoboeherMio0Z/PD6J" +
                    "rQIDAQAB"
            } else {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t" +
                    "+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7" +
                    "/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl" +
                    "+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwV" +
                    "I1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t0" +
                    "0G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/" +
                    "1QIDAQAB"
            }
        }
        // When
        val app = initKoin(config)
        // Then
        val usecase: RegisterNewDonor by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a FetchConsentDocuments`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        // When
        val app = initKoin(config)
        // Then
        val usecase: UsecaseContract.FetchConsentDocuments by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a FetchUserConsents`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        // When
        val app = initKoin(config)
        // Then
        val usecase: UsecaseContract.FetchUserConsents by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a RevokeUserConsent`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        // When
        val app = initKoin(config)
        // Then
        val usecase: UsecaseContract.RevokeUserConsent by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a DonateResources`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        config.whenGetServicePublicKey = { service ->
            if (service == Contract.Service.DD) {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWYsUPv7etCQYYhMtwkP" +
                    "xGH7144My0yUnqCmF38w40S7CCd54fa1zhijyvAEU67gMgxesyi2bMHPQJp2E63f" +
                    "g/0IcY4kY//9NrtWY7QovOJaFa8ov+wiIbKa3Y5zy4sxq8VoBJlr1EBYaQNX6I9f" +
                    "NG+IcQlkoTTqL+qt7lYsW0P4H3vR/92HHaJjA+yvSbXhePMh2IN4ESTqbBSSwWfd" +
                    "AHtFlH63hV65EB0pUudPumWpUrJWYczveoUO3XUU4qmJ7lZU0kTUFBwwfdeprZtG" +
                    "nEgS+ZIQAp4Y9BId1Ris5XgZDwmMYF8mB1sqGEnbQkmkaMPoboeherMio0Z/PD6J" +
                    "rQIDAQAB"
            } else {
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t" +
                    "+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7" +
                    "/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl" +
                    "+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwV" +
                    "I1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t0" +
                    "0G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/" +
                    "1QIDAQAB"
            }
        }
        // When
        val app = initKoin(config)
        // Then
        val usecase: DonateResources by app.koin.inject()
        assertNotNull(usecase)
    }

    @Test
    fun `Given initKoin is called with a Configuration, the resulting KoinApplication contains a UseCaseRunner`() {
        // Given
        config.whenGetEnvironment = { Environment.LOCAL }
        // When
        val app = initKoin(config)
        // Then
        val usecase: UsecaseRunnerContract by app.koin.inject()
        assertNotNull(usecase)
    }
}
