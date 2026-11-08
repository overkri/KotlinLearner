package ledger.m14

import io.kotest.matchers.shouldBe
import ledger.springbridge.AccountEntity
import ledger.springbridge.AccountJpaRepository
import ledger.springbridge.LedgerProperties
import ledger.springbridge.LedgerSpringApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [LedgerSpringApplication::class])
@AutoConfigureMockMvc
@Tag("m14")
class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var repository: AccountJpaRepository

    @Autowired
    lateinit var properties: LedgerProperties

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
        repository.save(
            AccountEntity(
                id = "acc-1",
                ownerName = "Alice",
                status = "ACTIVE",
                balanceMinor = 10_000,
                currency = "USD",
                version = 0,
            ),
        )
    }

    @Test
    fun `returns an account by id`() {
        mockMvc.perform(get("/api/accounts/acc-1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("acc-1"))
            .andExpect(jsonPath("$.ownerName").value("Alice"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.balanceMinor").value(10_000))
    }

    @Test
    fun `returns 404 for a missing account`() {
        mockMvc.perform(get("/api/accounts/missing"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("account_not_found"))
    }

    @Test
    fun `freezes an account and bumps the version`() {
        mockMvc.perform(post("/api/accounts/acc-1/freeze"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("FROZEN"))
            .andExpect(jsonPath("$.version").value(1))

        repository.findById("acc-1").orElse(null)?.status shouldBe "FROZEN"
    }

    @Test
    fun `rejects freezing a closed account`() {
        repository.save(
            AccountEntity(
                id = "closed",
                ownerName = "Bob",
                status = "CLOSED",
                balanceMinor = 0,
                currency = "USD",
                version = 0,
            ),
        )

        mockMvc.perform(post("/api/accounts/closed/freeze"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("invalid_request"))
    }

    @Test
    fun `binds configuration properties`() {
        properties.defaultCurrency shouldBe "USD"
        properties.maxTransferMinor shouldBe 1_000_000
    }
}
