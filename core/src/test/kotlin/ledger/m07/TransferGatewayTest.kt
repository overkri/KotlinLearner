package ledger.m07

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import ledger.coroutines.RetryPolicy
import ledger.coroutines.RetryableException
import ledger.coroutines.TransferClient
import ledger.coroutines.TransferGateway
import ledger.domain.AccountId
import ledger.domain.IdempotencyKey
import ledger.domain.TransferError
import ledger.domain.TransferRequest
import ledger.domain.TransferResult
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m07")
class TransferGatewayTest {

    private val request = TransferRequest(
        from = AccountId("from"),
        to = AccountId("to"),
        amount = TestData.usd(100),
        idempotencyKey = IdempotencyKey("idem-1"),
    )

    private val success = TransferResult.Success(TestData.transfer())

    private class FlakyClient(
        private val failures: Int,
        private val result: TransferResult,
    ) : TransferClient {
        var calls = 0
            private set

        override suspend fun submit(request: TransferRequest): TransferResult {
            calls += 1
            if (calls <= failures) throw RetryableException("temporary")
            return result
        }
    }

    @Test
    fun `retries temporary failures`() = runTest {
        val client = FlakyClient(failures = 2, result = success)
        val gateway = TransferGateway(client, RetryPolicy(attempts = 3, initialDelayMillis = 10))

        gateway.transfer(request) shouldBe success
        client.calls shouldBe 3
        testScheduler.currentTime shouldBe 30L
    }

    @Test
    fun `does not retry a business failure`() = runTest {
        val failure = TransferResult.Failure(
            TransferError.InsufficientFunds(
                accountId = AccountId("from"),
                available = TestData.usd(10),
                requested = TestData.usd(100),
            ),
        )
        var calls = 0
        val client = TransferClient {
            calls += 1
            failure
        }
        val gateway = TransferGateway(client, RetryPolicy(attempts = 5, initialDelayMillis = 10))

        gateway.transfer(request) shouldBe failure
        calls shouldBe 1
        testScheduler.currentTime shouldBe 0L
    }

    @Test
    fun `propagates a non-retryable exception`() = runTest {
        var calls = 0
        val client = TransferClient {
            calls += 1
            error("fatal")
        }
        val gateway = TransferGateway(client, RetryPolicy(attempts = 5, initialDelayMillis = 10))

        val error = runCatching { gateway.transfer(request) }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe "fatal"
        calls shouldBe 1
    }

    @Test
    fun `returns the first successful result without delay`() = runTest {
        val client = FlakyClient(failures = 0, result = success)
        val gateway = TransferGateway(client, RetryPolicy(attempts = 3, initialDelayMillis = 10))

        gateway.transfer(request) shouldBe success
        client.calls shouldBe 1
        testScheduler.currentTime shouldBe 0L
    }
}
