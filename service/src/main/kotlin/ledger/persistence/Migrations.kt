package ledger.persistence

import org.flywaydb.core.Flyway
import javax.sql.DataSource

/**
 * ═══════════════════════════════════════════════════════════
 *  МОДУЛЬ 12 · УПРАЖНЕНИЕ 12.5 — Flyway
 * ═══════════════════════════════════════════════════════════
 *  Тема:           миграции, идемпотентность, versioned SQL
 *  Java-аналогия:  Flyway/Liquibase в Spring Boot
 *  Тесты:          ./gradlew :service:test -Pmodule=12 --tests "ledger.m12.MigrationsTest"
 *  Уровень:        ★★☆
 *
 *  ЗАДАНИЕ
 *  Накатите миграции из classpath:db/migration.
 *
 *  ТРЕБОВАНИЯ
 *  1. Flyway.configure().dataSource(dataSource).
 *  2. locations = "classpath:db/migration".
 *  3. Вернуть количество применённых миграций (migrationsExecuted).
 *  4. Повторный вызов на той же БД не должен падать и вернуть 0.
 *  5. Не выполнять миграции вручную через SQL в этом методе.
 *
 *  ПОДСКАЗКИ
 *  - `Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load()`.
 *  - `.migrate().migrationsExecuted`.
 *  - Flyway создаёт таблицу flyway_schema_history.
 *
 *  ЛОВУШКА
 *  Почему нельзя менять уже применённую миграцию? Что такое checksum и
 *  почему Flyway упадёт при изменении V1?
 * ═══════════════════════════════════════════════════════════
 */
fun migrate(dataSource: DataSource): Int = TODO("m12.5.1")
