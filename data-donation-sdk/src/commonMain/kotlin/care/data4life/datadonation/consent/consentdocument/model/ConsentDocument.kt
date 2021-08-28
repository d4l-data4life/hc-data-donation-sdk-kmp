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

package care.data4life.datadonation.consent.consentdocument.model

import care.data4life.datadonation.ConsentDataContract
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConsentDocument(
    override val key: String,
    override val version: String,
    override val processor: String,
    override val description: String? = null,
    override val recipient: String,
    override val language: String,
    override val title: String? = null,
    override val text: String,
    @SerialName("allowAdminConsent")
    override val allowsAdminConsent: Boolean = false,
    @SerialName("irrevocable")
    override val isIrrevocable: Boolean = false,
    override val consentEmailTemplateKey: String? = null,
    override val revokeEmailTemplateKey: String? = null,
) : ConsentDataContract.ConsentDocument
