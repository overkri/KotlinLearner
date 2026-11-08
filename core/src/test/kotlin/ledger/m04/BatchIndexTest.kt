package ledger.m04

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.collections.chunkIds
import ledger.collections.indexById
import ledger.collections.partitionByStatus
import ledger.domain.AccountId
import ledger.domain.AccountStatus
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m04")
class BatchIndexTest {

    @Test
    fun `chunks identifiers`() {
        val ids = listOf("a", "b", "c", "d", "e").map(::AccountId)

        chunkIds(ids, 2) shouldBe listOf(
            listOf(AccountId("a"), AccountId("b")),
            listOf(AccountId("c"), AccountId("d")),
            listOf(AccountId("e")),
        )
    }

    @Test
    fun `handles empty input`() {
        chunkIds(emptyList(), 10) shouldBe emptyList()
    }

    @Test
    fun `rejects a non-positive chunk size`() {
        val error = shouldThrow<IllegalArgumentException> { chunkIds(emptyList(), 0) }
        error shouldHaveMessage "size must be positive"
    }

    @Test
    fun `indexes accounts and keeps the last duplicate`() {
        val first = TestData.account(id = "a", balance = TestData.usd(100))
        val second = TestData.account(id = "a", balance = TestData.usd(200))

        val index = indexById(listOf(first, second))

        index[AccountId("a")]?.balance shouldBe TestData.usd(200)
    }

    @Test
    fun `partitions active accounts and preserves order`() {
        val active1 = TestData.account(id = "a1", status = AccountStatus.ACTIVE)
        val frozen = TestData.account(id = "f1", status = AccountStatus.FROZEN)
        val active2 = TestData.account(id = "a2", status = AccountStatus.ACTIVE)
        val closed = TestData.account(id = "c1", status = AccountStatus.CLOSED)

        val (active, other) = partitionByStatus(listOf(active1, frozen, active2, closed))

        active.map { it.id } shouldBe listOf(AccountId("a1"), AccountId("a2"))
        other.map { it.id } shouldBe listOf(AccountId("f1"), AccountId("c1"))
    }
}
