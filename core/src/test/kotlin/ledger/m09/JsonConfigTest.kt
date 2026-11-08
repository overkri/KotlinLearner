package ledger.m09

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ledger.serialization.TransferRequestDto
import ledger.serialization.ledgerJson
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m09")
class JsonConfigTest {

    private val json = ledgerJson()

    @Test
    fun `configures the expected flags`() {
        json.configuration.ignoreUnknownKeys shouldBe true
        json.configuration.encodeDefaults shouldBe true
        json.configuration.explicitNulls shouldBe false
        json.configuration.prettyPrint shouldBe false
        json.configuration.classDiscriminator shouldBe "type"
        json.configuration.isLenient shouldBe false
    }

    @Test
    fun `ignores unknown keys`() {
        val dto = json.decodeFromString<TransferRequestDto>(
            """
            {
              "from": "acc-1",
              "to": "acc-2",
              "amountMinor": 100,
              "currency": "USD",
              "idempotencyKey": "idem-1",
              "extra": true
            }
            """.trimIndent(),
        )

        dto.from shouldBe "acc-1"
    }

    @Test
    fun `omits explicit nulls`() {
        val dto = TransferRequestDto("a", "b", 100, "USD", "idem-1", note = null)

        json.encodeToString(dto) shouldNotContain "\"note\""
    }

    @Test
    fun `decodes a missing optional note as null`() {
        val dto = json.decodeFromString<TransferRequestDto>(
            """{"from":"a","to":"b","amountMinor":100,"currency":"USD","idempotencyKey":"k"}""",
        )

        dto.note shouldBe null
    }
}
