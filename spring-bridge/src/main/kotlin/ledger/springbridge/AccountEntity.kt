package ledger.springbridge

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * JPA-сущность. Обратите внимание на плагины `kotlin-jpa` (`noarg`) и
 * `kotlin-allopen` в `spring-bridge/build.gradle.kts`: Hibernate требует
 * no-arg конструктор и не-final класс/методы.
 */
@Entity
@Table(name = "accounts")
class AccountEntity(
    @Id
    @Column(name = "id", nullable = false, length = 64)
    var id: String = "",

    @Column(name = "owner_name", nullable = false)
    var ownerName: String = "",

    @Column(name = "status", nullable = false, length = 16)
    var status: String = "ACTIVE",

    @Column(name = "balance_minor", nullable = false)
    var balanceMinor: Long = 0,

    @Column(name = "currency", nullable = false, length = 8)
    var currency: String = "USD",

    @Column(name = "version", nullable = false)
    var version: Long = 0,
)
