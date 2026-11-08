package ledger.m08

import io.kotest.matchers.shouldBe
import ledger.domain.AccountId
import ledger.domain.DomainEvent
import ledger.domain.TransferError
import ledger.state.BalanceProjector
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m08")
class BalanceProjectorTest {

    private val from = AccountId("from")
    private val to = AccountId("to")
    private val fixed = Instant.parse("2026-03-01T10:00:00Z")

    @Test
    fun `applies a completed transfer`() {
        val projector = BalanceProjector(mapOf(from to 1_000L, to to 0L))

        projector.apply(
            DomainEvent.TransferCompleted(
                transferId = TestData.transfer().id,
                from = from,
                to = to,
                amount = TestData.usd(400),
                occurredAt = fixed,
            ),
        )

        projector.balances.value shouldBe mapOf(from to 600L, to to 400L)
    }

    @Test
    fun `treats a missing account as zero`() {
        val projector = BalanceProjector()

        projector.apply(
            DomainEvent.TransferCompleted(
                transferId = TestData.transfer().id,
                from = from,
                to = to,
                amount = TestData.usd(100),
                occurredAt = fixed,
            ),
        )

        projector.balances.value shouldBe mapOf(from to -100L, to to 100L)
    }

    @Test
    fun `ignores rejected transfers`() {
        val projector = BalanceProjector(mapOf(from to 1_000L, to to 0L))

        projector.apply(
            DomainEvent.TransferRejected(
                transferId = TestData.transfer().id,
                from = from,
                to = to,
                error = TransferError.InsufficientFunds(from, TestData.usd(10), TestData.usd(100)),
                occurredAt = fixed,
            ),
        )

        projector.balances.value shouldBe mapOf(from to 1_000L, to to 0L)
    }

    @Test
    fun `ignores account frozen events`() {
        val projector = BalanceProjector(mapOf(from to 1_000L))

        projector.apply(DomainEvent.AccountFrozen(from, fixed))

        projector.balances.value shouldBe mapOf(from to 1_000L)
    }

    @Test
    fun `accumulates several completed transfers`() {
        val projector = BalanceProjector(mapOf(from to 1_000L, to to 0L))

        repeat(2) { index ->
            projector.apply(
                DomainEvent.TransferCompleted(
                    transferId = TestData.transfer(id = "tr-$index").id,
                    from = from,
                    to = to,
                    amount = TestData.usd(100),
                    occurredAt = fixed,
                ),
            )
        }

        projector.balances.value shouldBe mapOf(from to 800L, to to 200L)
    }
}
