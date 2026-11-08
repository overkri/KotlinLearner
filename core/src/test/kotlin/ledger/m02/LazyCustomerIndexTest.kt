package ledger.m02

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ledger.nullsafety.LazyCustomerIndex
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m02")
class LazyCustomerIndexTest {

    @Test
    fun `does not build the index in the constructor`() {
        val index = LazyCustomerIndex()

        index.indexBuilds shouldBe 0
    }

    @Test
    fun `builds the index once on first access`() {
        val index = LazyCustomerIndex()

        index.all.size shouldBe 4
        index.indexBuilds shouldBe 1

        index.find("c-1") shouldNotBe null
        index.all.size shouldBe 4
        index.indexBuilds shouldBe 1
    }

    @Test
    fun `returns the same list instance`() {
        val index = LazyCustomerIndex()

        (index.all === index.all) shouldBe true
    }

    @Test
    fun `finds customers by id`() {
        val index = LazyCustomerIndex()

        index.find("c-2")?.name shouldBe "Bob"
        index.find("missing") shouldBe null
    }
}
