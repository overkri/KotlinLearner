package ledger.m02

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import ledger.nullsafety.Customer
import ledger.nullsafety.firstNonBlank
import ledger.nullsafety.normalizeName
import ledger.nullsafety.parsePort
import ledger.nullsafety.requireEmail
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("m02")
class NullSafetyKataTest {

    @Test
    fun `returns the first non-blank value`() {
        firstNonBlank(null, "", "  ", "x", "y") shouldBe "x"
        firstNonBlank("first", "second") shouldBe "first"
    }

    @Test
    fun `returns null when every value is blank`() {
        firstNonBlank(null, "", "  ") shouldBe null
        firstNonBlank() shouldBe null
    }

    @Test
    fun `parses a valid port`() {
        parsePort("8080") shouldBe 8080
        parsePort(" 9090 ") shouldBe 9090
        parsePort("65535") shouldBe 65535
    }

    @Test
    fun `falls back to 8080 for invalid ports`() {
        parsePort(null) shouldBe 8080
        parsePort("") shouldBe 8080
        parsePort("0") shouldBe 8080
        parsePort("65536") shouldBe 8080
        parsePort("-1") shouldBe 8080
        parsePort("abc") shouldBe 8080
    }

    @Test
    fun `requires a non-blank email`() {
        val customer = Customer("c-1", "Alice", "alice@example.com", null)

        requireEmail(customer) shouldBe "alice@example.com"
    }

    @Test
    fun `rejects a customer without email`() {
        val withoutEmail = Customer("c-2", "Bob", null, null)
        val blankEmail = Customer("c-3", "Carol", "  ", null)

        shouldThrow<IllegalStateException> { requireEmail(withoutEmail) } shouldHaveMessage
            "customer has no email"
        shouldThrow<IllegalStateException> { requireEmail(blankEmail) } shouldHaveMessage
            "customer has no email"
    }

    @Test
    fun `normalizes a name`() {
        normalizeName(" Alice ") shouldBe "Alice"
        normalizeName(null) shouldBe "anonymous"
        normalizeName("   ") shouldBe "anonymous"
    }
}
