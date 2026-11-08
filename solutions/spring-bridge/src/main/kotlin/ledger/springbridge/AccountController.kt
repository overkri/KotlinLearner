package ledger.springbridge

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val service: SpringAccountService,
) {
    @GetMapping("/{id}")
    fun find(@PathVariable id: String): AccountResponse {
        val account = service.find(id) ?: throw AccountNotFoundException(id)
        return account.toResponse()
    }

    @PostMapping("/{id}/freeze")
    fun freeze(@PathVariable id: String): AccountResponse {
        val account = service.freeze(id) ?: throw AccountNotFoundException(id)
        return account.toResponse()
    }
}
