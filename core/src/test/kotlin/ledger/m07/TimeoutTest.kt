package ledger.m07

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import ledger.coroutines.withTimeoutFallback
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m07")
class TimeoutTest {

    @Test
    fun `returns the fallback on timeout`() = runTest {
        val result = withTimeoutFallback(100, "fallback") {
            delay(1_000)
            "done"
        }

        result shouldBe "fallback"
        testScheduler.currentTime shouldBe 100L
    }

    @Test
    fun `returns the result when the block is fast enough`() = runTest {
        val result = withTimeoutFallback(1_000, "fallback") {
            delay(10)
            "done"
        }

        result shouldBe "done"
        testScheduler.currentTime shouldBe 10L
    }

    @Test
    fun `propagates a failure from the block`() = runTest {
        val error = runCatching {
            withTimeoutFallback(1_000, "fallback") {
                error("boom")
            }
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalStateException>().message shouldBe "boom"
    }

    @Test
    fun `rejects a non-positive timeout`() = runTest {
        val error = runCatching {
            withTimeoutFallback<Unit>(0, Unit) {}
        }.exceptionOrNull()

        error.shouldBeInstanceOf<IllegalArgumentException>().message shouldBe
            "timeoutMillis must be positive"
    }
}
