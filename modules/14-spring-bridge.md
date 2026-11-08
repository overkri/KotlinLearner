# Модуль 14 — Spring Boot + Kotlin, ревью, миграция

## Цель
Перенести срез LedgerKt на Spring Boot 4 + Kotlin, применить `kotlin-spring`/`kotlin-jpa`, настроить JPA-срез, exception handler и configuration properties. Переписать Java-код на Kotlin и провести code review.

## Предусловия
Модули 03–09, 12. `spring-bridge` (Spring Boot 4.1.2, H2, JPA) и `migration-kata` готовы.

## Шпаргалка
`kotlin-spring`/allopen; `kotlin-jpa`/noarg; `@Service`, `@Transactional`; `JpaRepository`; `@RestControllerAdvice`; `@ConfigurationProperties`; MockMvc; `@JvmOverloads`; `@Throws`; Spring Boot 4 package changes.

## Упражнения

| Файл | Что реализовать | Что проверяют тесты |
|---|---|---|
| `spring-bridge/src/main/kotlin/ledger/springbridge/SpringAccountService.kt` | `find`, `freeze` с `@Transactional` | 200/404, freeze, version, closed -> 400 |
| `spring-bridge/src/main/kotlin/ledger/springbridge/AccountController.kt` | GET/freeze endpoints | DTO, 200, 404 |
| `spring-bridge/src/main/kotlin/ledger/springbridge/ApiExceptionHandler.kt` | 404/400 ErrorResponse | `code`/`message` |
| `migration-kata/src/main/kotlin/ledger/migration/PricingEngine.kt` | Java → Kotlin pricing | Совпадение с Java, округление, overflow |
| `migration-kata/src/main/kotlin/ledger/migration/ReviewKata.kt` | Review kata | Completed, dedup по key (latest), сортировка |
| `migration-kata/src/main/kotlin/ledger/migration/JvmAnnotationsKata.kt` | `@JvmOverloads`, `@Throws` | Java-тест видит overload и exception |

## Рефакторинги
1. Замените `version += 1` на JPA `@Version` — что изменится в обработке конфликтов?
2. Вынесите `AccountResponse` в отдельный API-модуль и сравните с текущим.
3. Перепишите `ReviewKata` через `associateBy` + `maxByOrNull` и сравните читаемость.
4. Добавьте `@EntityGraph` для загрузки связей и проверьте SQL.

## Квиз
`quizzes/14-spring-bridge.md` — 8 вопросов про allopen/noarg, `@Transactional`, Entity/DTO, `@JvmOverloads`, Spring Boot 4.

## Веха
Spring Boot-срез запускается на H2: `GET /api/accounts/{id}` и `POST /api/accounts/{id}/freeze` работают, ошибки имеют единый формат, Java-версия pricing переписана на Kotlin без изменения поведения.

## Чеклист
- [ ] 14.1 `SpringAccountService` решён.
- [ ] 14.2 `AccountController` решён.
- [ ] 14.3 `ApiExceptionHandler` решён.
- [ ] 14.4 `PricingEngine` решён.
- [ ] 14.5 `ReviewKata` решён.
- [ ] 14.6 `JvmAnnotationsKata` решён.
- [ ] `./gradlew test -Pmodule=14` зелёный.
- [ ] Квиз пройден.

## Gate
`./gradlew build -x test` — зелёный; на заглушках тесты падают только на `NotImplementedError`; `scripts/verify-solutions.ps1 -Module 14` — зелёный.
