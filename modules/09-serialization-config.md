# Модуль 09 — Сериализация и конфигурация

## Цель
Научиться описывать JSON-контракты через `kotlinx.serialization`, делать кастомные сериализаторы, маппить DTO ↔ домен и загружать типизированный конфиг из environment.

## Предусловия
Модули 03–06. `Money`, `Transfer`, `TransferRequest`, `TransferError` готовы; плагин `kotlin-serialization` применён к `:core`.

## Шпаргалка
`@Serializable`, `@SerialName`, `KSerializer`, `Json { }`, `ignoreUnknownKeys`, `explicitNulls`, `classDiscriminator`, DTO ↔ domain, `loadConfig(env)`, fail-fast, секреты.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/serialization/MoneySerializer.kt` | `MoneyAsStringSerializer` | Формат `"1234 USD"`, все валюты, malformed/negative -> `SerializationException` |
| `core/src/main/kotlin/ledger/serialization/TransferDto.kt` | `toDomain`, `toDto`, `toErrorDto` | Валидный маппинг, invalid currency/id/amount, коды ошибок |
| `core/src/main/kotlin/ledger/serialization/JsonConfig.kt` | `ledgerJson()` | Флаги `ignoreUnknownKeys`, `explicitNulls`, `classDiscriminator`, unknown keys, null-поля |
| `core/src/main/kotlin/ledger/config/AppConfigLoader.kt` | `loadConfig(env)` | Дефолты, обязательные ключи, порт, pool size, trim |

## Рефакторинги
1. Добавьте `@Serializable`-обёртку для `Money` с `@Serializable(with = MoneyAsStringSerializer::class)` и сравните JSON с DTO-вариантом.
2. Сделайте `AppConfig` `@Serializable` и загружайте его напрямую из JSON-файла; какие проверки останутся?
3. Введите `RedactedString` для пароля и проверьте, что `toString()` не раскрывает секрет.

## Квиз
`quizzes/09-serialization-config.md` — 8 вопросов про `@Serializable`, `explicitNulls`, `classDiscriminator`, Double и секреты.

## Веха
JSON-контракты переводов и конфиг из env готовы: невалидный конфиг падает при старте, DTO не пропускают мусор в домен.

## Чеклист
- [ ] 9.1 `MoneyAsStringSerializer` решён.
- [ ] 9.2 `TransferDto` решён.
- [ ] 9.3 `ledgerJson` решён.
- [ ] 9.4 `loadConfig` решён.
- [ ] `./gradlew test -Pmodule=09` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 09` — зелёный.
