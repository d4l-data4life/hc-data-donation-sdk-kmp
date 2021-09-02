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

package care.data4life.datadonation.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonConfiguratorTest {
    @Test
    fun `It fulfils JsonConfigurator`() {
        val configurator: Any = JsonConfigurator

        assertTrue(configurator is JsonConfiguratorContract)
    }

    @Test
    fun `Given configure is called with a JsonBuilder it returns a JsonBuilder`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result: Any = JsonConfigurator.configure(builder!!)

        // Then
        assertTrue(result is JsonBuilder)
    }

    @Test
    fun `Given configure is called it configures the resulting Json Serializer`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result = JsonConfigurator.configure(builder!!)

        // Then
        assertTrue(result.isLenient)
        assertTrue(result.ignoreUnknownKeys)
        assertTrue(result.allowSpecialFloatingPointValues)
        assertFalse(result.useArrayPolymorphism)
    }
}
