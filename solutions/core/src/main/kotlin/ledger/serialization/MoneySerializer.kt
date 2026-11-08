package ledger.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ledger.domain.Currency
import ledger.domain.Money

object MoneyAsStringSerializer : KSerializer<Money> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Money", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Money) {
        encoder.encodeString("${value.minor} ${value.currency.name}")
    }

    override fun deserialize(decoder: Decoder): Money {
        val text = decoder.decodeString()
        return runCatching {
            val parts = text.trim().split(" ")
            require(parts.size == 2) { "expected '<minor> <CUR>'" }
            val minor = parts[0].toLong()
            val currency = Currency.valueOf(parts[1])
            Money(minor, currency)
        }.getOrElse { error ->
            throw SerializationException("invalid money: $text", error)
        }
    }
}
