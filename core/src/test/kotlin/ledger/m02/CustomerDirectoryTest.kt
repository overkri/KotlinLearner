package ledger.m02

import io.kotest.matchers.shouldBe
import ledger.nullsafety.Customer
import ledger.nullsafety.CustomerDirectory
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m02")
class CustomerDirectoryTest {

    private val directory = CustomerDirectory()

    @Test
    fun `finds an existing customer`() {
        directory.find("c-1") shouldBe Customer("c-1", "Alice", "alice@example.com", "ali")
    }

    @Test
    fun `returns null for an unknown customer`() {
        directory.find("missing") shouldBe null
    }

    @Test
    fun `falls back to id when the name is blank`() {
        directory.displayName("c-1") shouldBe "Alice"
        directory.displayName("c-3") shouldBe "c-3"
        directory.displayName("c-4") shouldBe "c-4"
    }

    @Test
    fun `uses a stable label for an unknown customer`() {
        directory.displayName("missing") shouldBe "Unknown customer"
    }

    @Test
    fun `extracts the email domain`() {
        directory.emailDomain("c-1") shouldBe "example.com"
        directory.emailDomain("c-3") shouldBe "example.org"
    }

    @Test
    fun `returns null when the email is missing`() {
        directory.emailDomain("c-2") shouldBe null
        directory.emailDomain("missing") shouldBe null
    }
}
