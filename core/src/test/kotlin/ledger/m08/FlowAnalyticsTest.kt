package ledger.m08

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import ledger.domain.Currency
import ledger.domain.Direction
import ledger.domain.TransferStatus
import ledger.state.completedOnly
import ledger.state.totalsByCurrencyFlow
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m08")
class FlowAnalyticsTest {

    @Test
    fun `emits a running total after every entry`() = runTest {
        val entries = flowOf(
            TestData.entry(id = 1, direction = Direction.DEBIT, amount = TestData.usd(100)),
            TestData.entry(id = 2, direction = Direction.CREDIT, amount = TestData.usd(50)),
            TestData.entry(id = 3, direction = Direction.DEBIT, amount = TestData.eur(30)),
        )

        entries.totalsByCurrencyFlow().toList() shouldBe listOf(
            emptyMap(),
            mapOf(Currency.USD to -100L),
            mapOf(Currency.USD to -50L),
            mapOf(Currency.USD to -50L, Currency.EUR to -30L),
        )
    }

    @Test
    fun `emits an initial empty map for an empty flow`() = runTest {
        flowOf<ledger.domain.LedgerEntry>().totalsByCurrencyFlow().toList() shouldBe
            listOf(emptyMap())
    }

    @Test
    fun `keeps only completed transfers`() = runTest {
        val completed = TestData.transfer(id = "completed", status = TransferStatus.COMPLETED)
        val pending = TestData.transfer(id = "pending", status = TransferStatus.PENDING)
        val rejected = TestData.transfer(id = "rejected", status = TransferStatus.REJECTED)

        flowOf(completed, pending, rejected).completedOnly().toList() shouldBe listOf(completed)
    }

    @Test
    fun `is cold and re-evaluates on every collection`() = runTest {
        var collections = 0
        val source = kotlinx.coroutines.flow.flow {
            collections += 1
            emit(TestData.entry(direction = Direction.CREDIT, amount = TestData.usd(100)))
        }
        val flow = source.totalsByCurrencyFlow()

        flow.toList()
        flow.toList()

        collections shouldBe 2
    }
}
