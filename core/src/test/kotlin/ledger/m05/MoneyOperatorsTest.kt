package ledger.m05

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.domain.Currency
import ledger.domain.Money
import ledger.functional.compareTo
import ledger.functional.minus
import ledger.functional.plus
import ledger.functional.times
import ledger.functional.total
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m05")
class MoneyOperatorsTest {

    @Test
    fun `adds money in the same currency`() {
        (TestData.usd(100) + TestData.usd(50)) shouldBe TestData.usd(150)
    }

    @Test
    fun `rejects addition across currencies`() {
        val error = shouldThrow<IllegalArgumentException> {
            TestData.usd(100) + TestData.eur(50)
        }

        error shouldHaveMessage "currency mismatch"
    }

    @Test
    fun `subtracts money and rejects a negative result`() {
        (TestData.usd(100) - TestData.usd(40)) shouldBe TestData.usd(60)

        val error = shouldThrow<IllegalArgumentException> {
            TestData.usd(100) - TestData.usd(140)
        }
        error shouldHaveMessage "result must be non-negative"
    }

    @Test
    fun `multiplies by a non-negative factor`() {
        (TestData.usd(100) * 3) shouldBe TestData.usd(300)
        (TestData.usd(100) * 0) shouldBe TestData.usd(0)

        val error = shouldThrow<IllegalArgumentException> { TestData.usd(100) * -1 }
        error shouldHaveMessage "factor must be non-negative"
    }

    @Test
    fun `detects multiplication overflow`() {
        val huge = Money(Long.MAX_VALUE, Currency.USD)

        shouldThrow<ArithmeticException> { huge * 2 }
    }

    @Test
    fun `compares money by minor units`() {
        (TestData.usd(100) < TestData.usd(200)) shouldBe true
        (TestData.usd(100) <= TestData.usd(100)) shouldBe true
        (TestData.usd(300) > TestData.usd(200)) shouldBe true

        val error = shouldThrow<IllegalArgumentException> {
            TestData.usd(100) < TestData.eur(200)
        }
        error shouldHaveMessage "currency mismatch"
    }

    @Test
    fun `totals a non-empty collection`() {
        listOf(TestData.usd(100), TestData.usd(200), TestData.usd(50)).total() shouldBe
            TestData.usd(350)
    }

    @Test
    fun `rejects an empty total`() {
        val error = shouldThrow<IllegalArgumentException> { emptyList<Money>().total() }
        error shouldHaveMessage "cannot total an empty collection"
    }

    @Test
    fun `rejects a mixed-currency total`() {
        val error = shouldThrow<IllegalArgumentException> {
            listOf(TestData.usd(100), TestData.eur(100)).total()
        }
        error shouldHaveMessage "currency mismatch"
    }
}
