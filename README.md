# LedgerKt — курс «Kotlin для бэкенд-разработки»

Учебный проект-тренажёр для Java-бэкендера, который переходит на Kotlin. Один сквозной FinTech-сервис — учётные записи, двойная запись, переводы, идемпотентность, outbox и Kafka — растёт от модуля к модулю: от `val` и `data class` до Ktor, Exposed, Redis и Spring Boot.

## Зачем этот курс
- Показать Kotlin **без магии Spring**: сначала руками, потом фреймворк.
- Дать практику на реальном домене: `Money`, проводки, переводы, ошибки, конкурентность.
- Научить читать и ревьюить чужой Kotlin (Spring Boot + Kotlin) в модуле 14.
- Закрыть разрывы Java→Kotlin: null-safety, свойства, `sealed`, корутины, DSL, interop.

## Стек
| Область | Технологии |
|---|---|
| Язык / сборка | Kotlin 2.4.21, Gradle 9.5.1 (Kotlin DSL), JDK 21 toolchain |
| HTTP | Ktor 3.6.1 (Netty), StatusPages, JWT, Micrometer |
| БД | PostgreSQL 17, HikariCP, Flyway, Exposed 1.6.1 |
| Redis / Kafka | Lettuce 7.9, `kafka-clients` 4.3 |
| Сериализация | kotlinx.serialization 1.12 |
| Тесты | JUnit Jupiter, kotlin-test, MockK, Kotest assertions, Testcontainers 2.0.5 |
| Spring | Spring Boot 4.1.2 + Kotlin (модуль 14) |

## Быстрый старт
```bash
# 1. Сборка без выполнения тестов
./gradlew build -x test

# 2. Инфраструктура для модулей 12–13
docker compose up -d

# 3. Тесты конкретного модуля (NN = 00..14)
./gradlew test -Pmodule=00

# 4. Проверка эталонных решений (временная копия проекта)
./scripts/verify-solutions.sh 00
```

Windows PowerShell:
```powershell
.\gradlew.bat build -x test
.\gradlew.bat test -Pmodule=00
.\scripts\verify-solutions.ps1 -Module 00
```

> JDK 21 не обязателен локально: Gradle provisioning скачает Eclipse Temurin 21 через Foojay. Docker нужен только для интеграционных тестов модулей 12–13 и ручного запуска `compose.yaml`.

## Как проходить модуль
1. Прочитайте `modules/NN-*.md` и `docs/NN-*.md`.
2. Откройте файлы упражнений из таблицы в спецификации модуля.
3. Замените `TODO("mNN.x.y")` на реализацию. Сигнатуры и контракты менять нельзя.
4. Запускайте `./gradlew test -Pmodule=NN` до зелёного.
5. Пройдите квиз `quizzes/NN-*.md`.
6. Только после этого смотрите `solutions/` и сравнивайте подходы.
7. Отметьте пункты в `PROGRESS.md` и переходите к следующему модулю.

Подсказки в KDoc раскрываются по одной: сначала попробуйте без них. Тесты сформулированы как предложения (`rejects transfer to the same account`) — читайте сообщения assert'ов, они подсказывают, что именно не так.

## Структура
```
core/            чистый Kotlin: синтаксис, типы, коллекции, корутины, сериализация
service/         Ktor-сервис, БД, Redis, Kafka
spring-bridge/   Spring Boot 4 + Kotlin, JPA, ревью
migration-kata/  смешанный Java/Kotlin и миграционные kata
solutions/       эталонные решения с зеркальной структурой
docs/            шпаргалки
modules/         спецификации модулей
quizzes/         вопросы с ответами под <details>
scripts/         запуск тестов и проверка решений
```

## Программа
| № | Модуль | Часы |
|---|---|---|
| 00 | Старт и инструменты | 2 |
| 01 | Синтаксис и выражения | 3 |
| 02 | Null-safety и Java interop | 3 |
| 03 | Типы и модель домена | 5 |
| 04 | Коллекции и Sequence | 5 |
| 05 | Функции, расширения, DSL | 6 |
| 06 | Ошибки и Result | 4 |
| 07 | Корутины | 7 |
| 08 | Flow, каналы, общее состояние | 5 |
| 09 | Сериализация и конфигурация | 4 |
| 10 | Голый HTTP → Ktor | 6 |
| 11 | Ktor для продакшена | 7 |
| 12 | База данных | 8 |
| 13 | Redis, идемпотентность, Kafka, outbox | 8 |
| 14 | Spring Boot + Kotlin, ревью, миграция | 6 |

Подробная таблица с темами и вехами — в `plan.md` и `modules/`.

## FAQ
**Docker не запущен, интеграционные тесты падают.** Запустите Docker Desktop и проверьте `docker run --rm hello-world`. Модули 00–11 работают без Docker.

**Порты заняты.** `compose.yaml` использует 5432 (Postgres), 6379 (Redis), 9092 (Kafka). Остановите локальные сервисы или измените порты в compose.

**Windows/WSL и Testcontainers.** Docker Desktop должен быть доступен из WSL (включите integration). Для PowerShell-скриптов используйте `.\scripts\...ps1`; bash-скрипты рассчитаны на Git Bash/WSL/macOS/Linux.

**Почему `build -x test` зелёный, а тесты падают?** Так и задумано: заглушки компилируются, но бросают `NotImplementedError`. Это точка старта упражнения.

**Где решения?** `solutions/`. Они не подключены к сборке студента: `scripts/verify-solutions.*` копирует проект во временную папку и накладывает решения сверху.

## Лицензия
Учебный материал; используйте свободно для обучения.
