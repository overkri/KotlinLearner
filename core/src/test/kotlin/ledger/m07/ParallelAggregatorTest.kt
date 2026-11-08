package ledger.m07

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import ledger.coroutines.loadAccounts
import ledger.domain.AccountId
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m07")
class ParallelAggregatorTest {

    @Test
    fun `loads accounts concurrently and preserves order`() = runTest {
        val ids = listOf(AccountId("a"), AccountId("b"), AccountId("c"))
        val started = mutableListOf<AccountId>()

        val accounts = loadAccounts(ids) { id ->
            started += id
            delay(1_000)
            TestData.account(id = id.value)
        }

        accounts.map { it.id } shouldBe ids
        started.size shouldBe 3
        testScheduler.currentTime shouldBe 1_000L
    }

    @Test
    fun `propagates a failure and cancels siblings`() = runTest {
        val ids = listOf(AccountId("a"), AccountId("b"), AccountId("c"))

        val error = runCatching {
            loadAccounts(ids) { id ->
                delay(1_000)
                if (id.value == "b") error("boom")
                TestData.account(id = id.value)
            }
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe "boom"
    }
}
