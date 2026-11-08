# Квиз 12 — База данных

## 1. Зачем нужен пул соединений?
<details><summary>Ответ</summary>Соединение с БД дорого открывать на каждый запрос. Пул переиспользует соединения, ограничивает конкурентность и защищает БД от перегрузки. Размер пула умножается на количество подов.</details>

## 2. Что произойдёт при `UPDATE accounts SET balance=? WHERE id=?` без version?
<details><summary>Ответ</summary>Возможен lost update: два конкурентных перевода прочитают один баланс, оба запишут свой результат, одно списание потеряется. Optimistic locking добавляет `AND version = ?`.</details>

## 3. Почему `executeUpdate() == 0` — не всегда «нет записи»?
<details><summary>Ответ</summary>Это может быть конфликт версии: запись есть, но `version` не совпала. Нужно различать 404 и 409, а не слепо возвращать 404.</details>

## 4. Что делает Flyway checksum?
<details><summary>Ответ</summary>Flyway хранит хэш применённой миграции. Если изменить уже применённый файл, checksum не совпадёт и приложение не стартует. Новые изменения — только новой миграцией.</details>

## 5. Почему Exposed 1.x импорты отличаются от старых примеров?
<details><summary>Ответ</summary>В 1.0 пакеты переехали в `org.jetbrains.exposed.v1.core` и `org.jetbrains.exposed.v1.jdbc`; `selectAll`, `insert`, `update`, `transaction` — extension-функции в `v1.jdbc`. Примеры 0.4x не скомпилируются.</details>

## 6. Что такое N+1 и как его поймать тестом?
<details><summary>Ответ</summary>Один запрос за списком + N запросов за связанными данными. В тесте оборачивают DataSource счётчиком (`QueryCounter`) и проверяют, что `prepareStatement` вызван один раз; в проде — pg_stat_statements/лог SQL/APM.</details>

## 7. Почему `suspendTransaction` лучше blocking `transaction` в Ktor?
<details><summary>Ответ</summary>`transaction` блокирует поток; в корутине это может исчерпать пул диспетчера. `suspendTransaction` приостанавливает корутину и не блокирует поток. Внутри всё равно нельзя делать долгие CPU-операции.</details>

## 8. Как тестировать БД без `docker compose up`?
<details><summary>Ответ</summary>Testcontainers поднимает PostgreSQL в Docker на время тестов, Flyway накатывает схему, тесты помечаются `@Tag("integration")`. Это ближе к проду, чем H2, и не требует ручного compose.</details>
