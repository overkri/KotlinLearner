package ledger.m08

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.TransferRequest
import ledger.state.TransferQueue
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m08")
class TransferQueueTest {

    private fun request(key: String): TransferRequest = TransferRequest(
        from = AccountId("from"),
        to = AccountId("to"),
        amount = TestData.usd(100),
        idempotencyKey = IdempotencyKey(key),
    )

    @Test
    fun `processes requests in order`() = runTest {
        val queue = TransferQueue()
        val processed = mutableListOf<TransferRequest>()
        val consumer = queue.start(this) { processed += it }

        queue.submit(request("one"))
        queue.submit(request("two"))
        queue.close()
        consumer.join()

        processed shouldBe listOf(request("one"), request("two"))
    }

    @Test
    fun `drains queued requests before completing`() = runTest {
        val queue = TransferQueue(capacity = 10)
        val processed = mutableListOf<String>()
        val consumer = queue.start(this) {
            delay(100)
            processed += it.idempotencyKey.value
        }

        repeat(3) { queue.submit(request("req-$it")) }
        queue.close()
        consumer.join()

        processed shouldBe listOf("req-0", "req-1", "req-2")
        testScheduler.currentTime shouldBe 300L
    }
}
