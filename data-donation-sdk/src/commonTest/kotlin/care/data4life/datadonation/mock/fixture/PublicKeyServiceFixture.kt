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

import care.data4life.datadonation.DataDonationSDK
import care.data4life.datadonation.donation.publickeyservice.PublicKeyServiceContract
import care.data4life.datadonation.donation.publickeyservice.model.RawKey
import care.data4life.datadonation.donation.publickeyservice.model.RawKeys
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object PublicKeyServiceFixture {
    val sampleRawKeys = RawKeys(
        credentials = listOf(
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.DonationService,
                environment = DataDonationSDK.Environment.DEVELOPMENT,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.DonationService,
                environment = DataDonationSDK.Environment.SANDBOX,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.DonationService,
                environment = DataDonationSDK.Environment.STAGING,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.DonationService,
                environment = DataDonationSDK.Environment.PRODUCTION,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.ALP,
                environment = DataDonationSDK.Environment.DEVELOPMENT,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.ALP,
                environment = DataDonationSDK.Environment.SANDBOX,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.ALP,
                environment = DataDonationSDK.Environment.STAGING,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            ),
            RawKey(
                domain = PublicKeyServiceContract.KeyDomain.ALP,
                environment = DataDonationSDK.Environment.PRODUCTION,
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvemFxDHLfwTWztqu+M5t+becNfUvJpYBqRYsRFKxoUe2s+9WZjwPMzIvJ43DlCK2dqtZelomGhVpi53AqbG7/Nm3dMH1nNSacfz20tZclshimJuHF1d126tbGn/3WdAxYfTq9DN8GZmqgRf1iunl+DwE/sP3Dm8I1y4BG3RyQcD/K66s0PWvpX71UlvoVdWmWA5rGkfzi4msdZz7wfwVI1cGnAX+YrBGTfkwJtHuHXCcLuR3zdNnG/ZB87O0Etl2bFHjCsDbAIRDggjXW+t00G+OALY8BMdU1cYKb8GBdqQW11BhRttGvFKFFt3i/8KH0b9ff80whY0bbeTAo51/1QIDAQAB"
            )
        )
    )
}
