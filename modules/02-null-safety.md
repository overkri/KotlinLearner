# Модуль 02 — Null-safety и Java interop

## Цель
Научиться безопасно работать с nullable-значениями и platform types из Java, использовать `?.`, `?:`, `takeIf`, `lazy` и адаптеры на границе Java/Kotlin.

## Предусловия
Модули 00–01. Java-классы `LegacyCustomerDirectory` и `LegacyCustomer` уже лежат в `core/src/main/java/ledger/legacy`.

## Шпаргалка
- `String` vs `String?`; `?.`, `?:`, `!!`; smart cast.
- Platform type `T!` — «может быть null, но компилятор не заставляет проверять».
- `isNullOrBlank`, `toIntOrNull`, `takeIf`, `requireNotNull`, `checkNotNull`, `error`.
- `by lazy` — ленивая потокобезопасная инициализация.
- `lateinit` — отложенная инициализация non-null поля.
- Адаптер — единственное место, где platform types превращаются в Kotlin-контракт.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `core/src/main/kotlin/ledger/nullsafety/CustomerDirectory.kt` | `find`, `displayName`, `emailDomain` поверх Java-справочника | Существующий/отсутствующий клиент, blank-имя, отсутствующий email, пустой домен |
| `core/src/main/kotlin/ledger/nullsafety/LazyCustomerIndex.kt` | Ленивый индекс клиентов с `by lazy` и счётчиком построений | Индекс не строится в конструкторе, строится один раз, `find` по id |
| `core/src/main/kotlin/ledger/nullsafety/NullSafetyKata.kt` | `firstNonBlank`, `parsePort`, `requireEmail`, `normalizeName` | Все nullable-кейсы, fallback 8080, `IllegalStateException` без email |

## Рефакторинги
1. Замените `?: "Unknown customer"` на `requireNotNull` — что изменится для вызывающего кода?
2. Перепишите `find` через `legacy.find(id)?.toCustomer()` и сравните читаемость с ранним выходом.
3. Сделайте `LazyCustomerIndex` с `LazyThreadSafetyMode.NONE` и объясните, почему в серверном коде это опасно.

## Квиз
`quizzes/02-null-safety.md` — 8 вопросов про platform types, `!!`, smart cast, `lateinit` и `lazy`.

## Веха
`CustomerDirectory().displayName("missing")` возвращает `"Unknown customer"` без исключения; `LazyCustomerIndex` строит индекс ровно один раз.

## Чеклист
- [ ] 2.1 `CustomerDirectory` решён.
- [ ] 2.2 `LazyCustomerIndex` решён.
- [ ] 2.3 `NullSafetyKata` решён.
- [ ] `./gradlew test -Pmodule=02` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 02` — зелёный.
