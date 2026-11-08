package ledger.m07

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import ledger.coroutines.RetryableException
import ledger.coroutines.retry
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m07")
class RetryTest {

    @Test
    fun `returns on the first success`() = runTest {
        var attempts = 0

        val result = retry(3, 100) {
            attempts += 1
            "ok"
        }

        result shouldBe "ok"
        attempts shouldBe 1
        testScheduler.currentTime shouldBe 0L
    }

    @Test
    fun `retries with exponential backoff`() = runTest {
        var attempts = 0

        val result = retry(3, 100) {
            attempts += 1
            if (attempts < 3) throw RetryableException("temporary")
            "ok"
        }

        result shouldBe "ok"
        attempts shouldBe 3
        testScheduler.currentTime shouldBe 300L
    }

    @Test
    fun `rethrows the last retryable error after exhausting attempts`() = runTest {
        var attempts = 0

        val error = runCatching {
            retry(3, 100) {
                attempts += 1
                throw RetryableException("temporary-$attempts")
            }
        }.exceptionOrNull()

        error.shouldBeInstanceOf<RetryableException>().message shouldBe "temporary-3"
        attempts shouldBe 3
        testScheduler.currentTime shouldBe 300L
    }

    @Test
    fun `does not retry other exceptions`() = runTest {
        var attempts = 0

        val error = runCatching {
            retry(3, 100) {
                attempts += 1
                error("fatal")
            }
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe "fatal"
        attempts shouldBe 1
        testScheduler.currentTime shouldBe 0L
    }

    @Test
    fun `requires at least one attempt`() = runTest {
        val error = runCatching {
            retry<Unit>(0, 0) {}
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "times must be at least 1"
    }

    @Test
    fun `rejects a negative initial delay`() = runTest {
        val error = runCatching {
            retry<Unit>(1, -1) {}
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "initialDelayMillis must be non-negative"
    }
}
