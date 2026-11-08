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

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 9 · УПРАЖНЕНИЕ 9.1 — Кастомный сериализатор Money
 * ═══════════════════════════════════════════════════════════
 *  Тема:           KSerializer, descriptor, encode/decode
 *  Java-аналогия:  Jackson JsonSerializer<Money>
 *  Тесты:          ./gradlew :core:test -Pmodule=09 --tests "ledger.m09.MoneySerializerTest"
 *  Уровень:        ★★★
 *
 *  ЗАДАНИЕ
 *  Сериализуйте Money строкой "<minor> <CUR>", например "1234 USD".
 *
 *  ТРЕБОВАНИЯ
 *  1. descriptor — примитив STRING с именем "Money".
 *  2. serialize: encodeString("${minor} ${currency.name}").
 *  3. deserialize: split по пробелу ровно на две части; minor — Long,
 *     currency — Currency.valueOf.
 *  4. Любая ошибка формата — SerializationException("invalid money: <text>").
 *  5. Не использовать Json внутри сериализатора.
 *
 *  ПОДСКАЗКИ
 *  - `PrimitiveSerialDescriptor("Money", PrimitiveKind.STRING)`.
 *  - `decoder.decodeString()` / `encoder.encodeString(...)`.
 *  - Оборачивайте разбор в runCatching и переводите ошибку в SerializationException.
 *
 *  ЛОВУШКА
 *  Почему сериализатор не должен молча возвращать Money(0, USD) при ошибке?
 *  Что произойдёт с деньгами клиента при таком «fallback»?
 * ═══════════════════════════════════════════════════════════
 */
object MoneyAsStringSerializer : KSerializer<Money> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Money", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Money): Unit = TODO("m09.1.1")

    override fun deserialize(decoder: Decoder): Money = TODO("m09.1.2")
}
