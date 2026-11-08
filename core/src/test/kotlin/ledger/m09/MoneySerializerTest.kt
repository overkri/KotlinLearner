package ledger.m09

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ledger.domain.Currency
import ledger.domain.Money
import ledger.serialization.MoneyAsStringSerializer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m09")
class MoneySerializerTest {

    private val json = Json

    @Test
    fun `serializes money as a string`() {
        json.encodeToString(MoneyAsStringSerializer, Money(1_234, Currency.USD)) shouldBe
            "\"1234 USD\""
        json.encodeToString(MoneyAsStringSerializer, Money(12, Currency.JPY)) shouldBe
            "\"12 JPY\""
    }

    @Test
    fun `deserializes money from a string`() {
        json.decodeFromString(MoneyAsStringSerializer, "\"1234 USD\"") shouldBe
            Money(1_234, Currency.USD)
        json.decodeFromString(MoneyAsStringSerializer, "\"0 EUR\"") shouldBe
            Money(0, Currency.EUR)
    }

    @Test
    fun `roundtrips every supported currency`() {
        Currency.entries.forEach { currency ->
            val money = Money(42, currency)
            json.decodeFromString(
                MoneyAsStringSerializer,
                json.encodeToString(MoneyAsStringSerializer, money),
            ) shouldBe money
        }
    }

    @Test
    fun `rejects a malformed value`() {
        listOf("\"1234\"", "\"1234 usd\"", "\"abc USD\"", "\"1234 USD extra\"", "\"\"")
            .forEach { input ->
                val error = shouldThrow<SerializationException> {
                    json.decodeFromString(MoneyAsStringSerializer, input)
                }
                error.message shouldBe "invalid money: ${input.removeSurrounding("\"")}"
            }
    }

    @Test
    fun `rejects a negative amount`() {
        val error = shouldThrow<SerializationException> {
            json.decodeFromString(MoneyAsStringSerializer, "\"-1 USD\"")
        }

        error shouldHaveMessage "invalid money: -1 USD"
    }
}
