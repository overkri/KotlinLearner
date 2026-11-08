package ledger.m03

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.Currency
import ledger.domain.Money
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m03")
class MoneyTest {

    @Test
    fun `rejects a negative minor amount`() {
        val error = shouldThrow<IllegalArgumentException> { Money(-1, Currency.USD) }
        error shouldHaveMessage "minor must be non-negative"
    }

    @Test
    fun `zero is a valid amount`() {
        val zero = Money(0, Currency.USD)

        zero.minor shouldBe 0
        zero.isZero shouldBe true
        Money(1, Currency.USD).isZero shouldBe false
    }

    @Test
    fun `companion factory creates zero for a currency`() {
        Money.zero(Currency.EUR) shouldBe Money(0, Currency.EUR)
        Money.zero(Currency.JPY).isZero shouldBe true
    }

    @Test
    fun `ofMajor respects currency fraction digits`() {
        Money.ofMajor(12, Currency.USD) shouldBe Money(1_200, Currency.USD)
        Money.ofMajor(12, Currency.EUR) shouldBe Money(1_200, Currency.EUR)
        Money.ofMajor(12, Currency.JPY) shouldBe Money(12, Currency.JPY)
    }

    @Test
    fun `ofMajor rejects a negative major amount`() {
        val error = shouldThrow<IllegalArgumentException> { Money.ofMajor(-1, Currency.USD) }
        error shouldHaveMessage "major must be non-negative"
    }

    @Test
    fun `data class equality includes the currency`() {
        Money(1_200, Currency.USD) shouldBe Money(1_200, Currency.USD)
        Money(1_200, Currency.USD) shouldNotBe Money(1_200, Currency.EUR)
    }

    @Test
    fun `supports copy and destructuring`() {
        val money = Money(1_200, Currency.USD)
        val (minor, currency) = money

        minor shouldBe 1_200
        currency shouldBe Currency.USD
        money.copy(minor = 500) shouldBe Money(500, Currency.USD)
        money.minor shouldBe 1_200
    }
}
