/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, D4L data4life gGmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package care.data4life.datadonation.internal.domain.usecases

import care.data4life.datadonation.core.model.ConsentDocument
import care.data4life.datadonation.internal.domain.repositories.ConsentDocumentRepository

open class GetConsentDocument(private val consentDocumentRepository: ConsentDocumentRepository) :
    ParameterizedUsecase<GetConsentDocument.Parameters, ConsentDocument>() {

    private val DONATION_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqwTD2GY3SClkbmDVtgIE\n" +
                "it7RGK1MqK8G1n6f7N7qixBTzTbtHCG0Z/gS7fGhZ3PlOJJZdrQakNvtNAk91i05\n" +
                "IffIo1Uw3501R3wvv7KJ8NtT5EIvHL8/7nZBTl+M4+FwFS60VGS3C0ZRWuuNe8SP\n" +
                "yuVrf/0ZYfI+jXVzhdUJ3qkm11GPQ4jSVKivDRISkPBgEXk36lm9+IsqC6Fk3dsv\n" +
                "eON0/CEMpxvpTBWu5vc3t6qNAWO0qWVVc0XcJt9nKsTi1D75TC5JuvujpgLOKnJj\n" +
                "Wt1u6JeH+bnf828NsURM/HJjU8iYksFkEateoX59qbqadWyaTmBU7gb406l2CdwF\n" +
                "JQIDAQAB"

    override suspend fun execute(): ConsentDocument =
        consentDocumentRepository.getConsentDocument(
            DONATION_KEY,
            parameter.version,
            parameter.language
        )

    sealed class Parameters {
        abstract val language: String
        abstract val version: String
    }
}
