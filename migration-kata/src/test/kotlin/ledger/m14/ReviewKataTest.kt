package ledger.m14

import io.kotest.matchers.shouldBe
import ledger.domain.TransferStatus
import ledger.migration.reviewTransfers
import ledger.testing.TestData
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.Instant

@Tag("m14")
class ReviewKataTest {

    private val t1: Instant = Instant.parse("2026-06-01T10:00:00Z")
    private val t2: Instant = Instant.parse("2026-06-01T11:00:00Z")
    private val t3: Instant = Instant.parse("2026-06-01T12:00:00Z")

    @Test
    fun `keeps completed transfers and removes rejected ones`() {
        val completed = TestData.transfer(id = "tr-1", status = TransferStatus.COMPLETED, createdAt = t1)
        val rejected = TestData.transfer(id = "tr-2", status = TransferStatus.REJECTED, createdAt = t2)

        reviewTransfers(listOf(completed, rejected)) shouldBe listOf(completed)
    }

    @Test
    fun `keeps the latest transfer for a duplicated idempotency key`() {
        val older = TestData.transfer(
            id = "tr-old",
            idempotencyKey = "idem-1",
            status = TransferStatus.COMPLETED,
            createdAt = t1,
        )
        val newer = TestData.transfer(
            id = "tr-new",
            idempotencyKey = "idem-1",
            status = TransferStatus.COMPLETED,
            createdAt = t3,
        )

        reviewTransfers(listOf(older, newer)) shouldBe listOf(newer)
    }

    @Test
    fun `sorts by createdAt and breaks ties by id`() {
        val later = TestData.transfer(id = "b", idempotencyKey = "idem-b", createdAt = t2)
        val earlier = TestData.transfer(id = "a", idempotencyKey = "idem-a", createdAt = t1)

        reviewTransfers(listOf(later, earlier)) shouldBe listOf(earlier, later)
    }

    @Test
    fun `breaks createdAt ties by id value`() {
        val first = TestData.transfer(id = "a", idempotencyKey = "idem-a", createdAt = t1)
        val second = TestData.transfer(id = "b", idempotencyKey = "idem-b", createdAt = t1)

        reviewTransfers(listOf(second, first)) shouldBe listOf(first, second)
    }

    @Test
    fun `handles an empty list`() {
        reviewTransfers(emptyList()) shouldBe emptyList()
    }
}
