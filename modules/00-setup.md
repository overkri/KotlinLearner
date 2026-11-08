# Модуль 00 — Старт и инструменты

## Цель
Собрать проект, понять, как Kotlin компилируется в JVM-байткод, и сделать первый вызов Java-кода из Kotlin. Научиться запускать тесты одного модуля и пользоваться MockK.

## Предусловия
- JDK 21 (Gradle скачает toolchain автоматически через Foojay, если JDK 21 нет локально).
- Docker Desktop — понадобится начиная с модуля 12, но лучше проверить сразу: `docker version`.
- IntelliJ IDEA с включённым Kotlin-плагином.
- Интернет для первой загрузки зависимостей Gradle.

## Шпаргалка
- Проект — multi-module Gradle: `core`, `service`, `spring-bridge`, `migration-kata`.
- Версии зависимостей — только в `gradle/libs.versions.toml` (version catalog).
- Запуск всех тестов модуля: `./gradlew test -Pmodule=00` (Windows: `.\gradlew.bat test -Pmodule=00`).
- Тест помечается `@Tag("m00")`; корневой `build.gradle.kts` при `-Pmodule=00` включает только этот тег.
- Kotlin-файлы лежат в `src/main/kotlin`, Java — в `src/main/java`; компилятор Kotlin видит оба source set.
- `TODO("m00.1.1")` — заглушка, которая бросает `NotImplementedError`. Пока решение не написано, тесты падают именно на ней.
- Решения — в `solutions/` с зеркальной структурой. Студент сначала пытается сам, потом сверяется.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/basics/FeeCalculatorBridge.kt` | Обёртка над Java `LegacyFeeCalculator`: default-аргумент валюты, вызов Java-перегрузки, безопасная обработка `null` из `describe` | Комиссии для USD/EUR/GBP/JPY, совпадение с Java, `"Unknown currency"` вместо null |
| `core/src/main/kotlin/ledger/basics/ReceiptPrinter.kt` | `fun interface Clock` + печать квитанции с внедрёнными часами и валидацией суммы | Формат строки, ровно один вызов `clock.now()`, `IllegalArgumentException` на отрицательную сумму |
| `core/src/main/kotlin/ledger/basics/Environment.kt` | `environmentSummary()`: версия Kotlin и JVM из `KotlinVersion.CURRENT` и системного свойства | Строка `Kotlin 2.4.x / JVM 21` |

## Рефакторинги
1. Замените в `FeeCalculatorBridge` default-аргумент на Java-подобные перегрузки — сравните читаемость и количество кода.
2. Уберите инъекцию `Clock` и вызовите `Instant.now()` внутри `print` — объясните, почему тест стал недетерминированным.
3. Перепишите `require(amountMinor > 0)` на `check` и сравните тип исключения и смысл проверки.

## Квиз
`quizzes/00-setup.md` — 8 вопросов про `val/var`, platform types, default-аргументы, теги и MockK.

## Веха
`./gradlew build -x test` зелёный; `./gradlew test -Pmodule=00` показывает падения только с `NotImplementedError`; после решения — все тесты модуля зелёные. В IntelliJ виден вызов Java-класса из Kotlin без ошибок.

## Чеклист
- [ ] Проект открывается в IntelliJ, Gradle sync проходит.
- [ ] `./gradlew build -x test` зелёный.
- [ ] `./gradlew test -Pmodule=00` падает на `TODO` (ожидаемо).
- [ ] Все три упражнения решены, тесты зелёные.
- [ ] Прочитана шпаргалка `docs/00-setup.md`.
- [ ] Пройден квиз.

## Gate
1. `./gradlew build -x test` — зелёный.
2. `./gradlew test -Pmodule=00` на заглушках — только `NotImplementedError`.
3. `scripts/verify-solutions.ps1 -Module 00` (или `.sh 00`) — зелёный.
4. Только после этого переходим к модулю 01.
