package ledger.springbridge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LedgerSpringApplication

fun main(args: Array<String>) {
    runApplication<LedgerSpringApplication>(*args)
}
