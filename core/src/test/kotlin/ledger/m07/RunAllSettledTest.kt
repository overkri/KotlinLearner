package ledger.m07

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import ledger.coroutines.runAllSettled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m07")
class RunAllSettledTest {

    @Test
    fun `returns a result for every task in order`() = runTest {
        val tasks: List<suspend () -> Int> = listOf(
            { delay(100); 1 },
            { error("boom") },
            { delay(50); 3 },
        )

        val results = runAllSettled(tasks)

        results.size shouldBe 3
        results[0].getOrNull() shouldBe 1
        results[1].exceptionOrNull().shouldBeInstanceOf<IllegalStateException>().message shouldBe "boom"
        results[2].getOrNull() shouldBe 3
        testScheduler.currentTime shouldBe 100L
    }

    @Test
    fun `one failure does not cancel the remaining tasks`() = runTest {
        val tasks: List<suspend () -> String> = listOf(
            { error("boom") },
            { delay(100); "slow" },
            { delay(10); "fast" },
        )

        val results = runAllSettled(tasks)

        results[0].isFailure shouldBe true
        results[1].getOrNull() shouldBe "slow"
        results[2].getOrNull() shouldBe "fast"
        testScheduler.currentTime shouldBe 100L
    }

    @Test
    fun `handles an empty task list`() = runTest {
        runAllSettled<Int>(emptyList()) shouldBe emptyList()
    }
}
